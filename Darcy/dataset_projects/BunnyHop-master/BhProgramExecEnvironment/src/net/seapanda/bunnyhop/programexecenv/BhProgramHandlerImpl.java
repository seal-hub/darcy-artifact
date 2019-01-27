/**
 * Copyright 2017 K.Koike
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.seapanda.bunnyhop.programexecenv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.seapanda.bunnyhop.bhprogram.common.BhProgramData;
import net.seapanda.bunnyhop.bhprogram.common.BhProgramHandler;

/**
 * スクリプトとBunnyHop間でデータを送受信するクラス
 * @author K.Koike
 */
public class BhProgramHandlerImpl implements BhProgramHandler {

	private final ExecutorService bhProgramExec = Executors.newSingleThreadExecutor();
	private final ExecutorService recvDataProcessor = Executors.newSingleThreadExecutor();
	private final BlockingQueue<BhProgramData> sendDataList = new ArrayBlockingQueue<>(BhParams.MAX_QUEUE_SIZE);	//!< to BunnyHop
	private final BlockingQueue<BhProgramData> recvDataList = new ArrayBlockingQueue<>(BhParams.MAX_QUEUE_SIZE);	//!< from BunnyHop
	private final AtomicBoolean connected = new AtomicBoolean(false);	//!< BunnyHopとの通信が有効な場合true
	private final ScriptInOut scriptIO = new ScriptInOut(sendDataList, connected);	//!< BhProgramの入出力用オブジェクト

	public BhProgramHandlerImpl(){}

	/**
	 * 初期化する
	 */
	public boolean init() {

		recvDataProcessor.submit(() ->{
			processRecvData();
		});
		return true;
	}

	@Override
	public boolean runScript(String fileName) {

		ScriptEngine engine = (new NashornScriptEngineFactory()).getScriptEngine("--language=es6");
		Path scriptPath = Paths.get(Util.EXEC_PATH, BhParams.Path.SCRIPT_DIR, fileName);
		boolean success = true;

		try (BufferedReader reader = Files.newBufferedReader(scriptPath, StandardCharsets.UTF_8)){
			CompiledScript cs = ((Compilable)engine).compile(reader);
			bhProgramExec.submit(() -> {
				try {
					Bindings binding = engine.createBindings();
					binding.put(BhParams.BhProgram.INOUT_MODULE_NAME, scriptIO);
					binding.put(BhParams.BhProgram.EXEC_PATH, Util.EXEC_PATH);
					cs.eval(binding);
				}
				catch (ScriptException e) {
					LogManager.INSTANCE.errMsgForDebug("runScript 1 " +  e.toString() + " " + fileName);

				}
			});
		}
		catch (IOException | ScriptException e) {
			LogManager.INSTANCE.errMsgForDebug("runScript 2 " +  e.toString() + " " + fileName);
			success = false;
		}
		return success;
	}

	@Override
	public boolean sendDataToScript(BhProgramData data) {

		boolean success = false;
		try {
			success = recvDataList.offer(data, BhParams.PUSH_RECV_DATA_TIMEOUT, TimeUnit.SECONDS);
		}
		catch(InterruptedException e) {}
		return success;
	}

	@Override
	public BhProgramData recvDataFromScript() {
		BhProgramData data = null;
		try {
			data = sendDataList.poll(BhParams.POP_SEND_DATA_TIMEOUT, TimeUnit.SECONDS);
		}
		catch(InterruptedException e) {}
		return data;
	}

	@Override
	public void connect() {
		connected.set(true);
	}

	@Override
	public void disconnect() {
		connected.set(false);
		sendDataList.clear();
	}

	/**
	 * BunnyHopから受信したデータを処理し続ける
	 */
	private void processRecvData() {

		while(true) {

			BhProgramData data = null;
			try {
				data = recvDataList.take();
			}
			catch(InterruptedException e) {
				break;
			}

			switch (data.type) {
				case INPUT_STR:
					scriptIO.addStdInData(data.str);
					break;
				default:
			}
		}
	}

}
