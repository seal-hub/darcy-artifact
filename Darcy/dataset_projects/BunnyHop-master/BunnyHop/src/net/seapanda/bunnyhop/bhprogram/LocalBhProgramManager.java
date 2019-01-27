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
package net.seapanda.bunnyhop.bhprogram;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import net.seapanda.bunnyhop.bhprogram.common.BhProgramData;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;

/**
 * BunnyHopで作成したプログラムのローカル環境での実行、終了、通信を行うクラス
 * @author K.Koike
 */
public class LocalBhProgramManager {

	public static final LocalBhProgramManager INSTANCE = new LocalBhProgramManager();	//!< シングルトンインスタンス
	private final BhProgramManagerCommon common = new BhProgramManagerCommon();
	private Process process;
	private final AtomicReference<Boolean> programRunning = new AtomicReference<>(false);	//!< プログラム実行中ならtrue

	private LocalBhProgramManager() {}

	public boolean init() {
		return common.init();
	}

	/**
	 * BhProgramの実行環境を立ち上げ、BhProgramを実行する
	 * @param filePath BhProgramのファイルパス
	 * @param ipAddr BhProgramを実行するマシンのIPアドレス
	 * @return BhProgram実行タスクのFutureオブジェクト
	 */
	public Optional<Future<Boolean>> executeAsync(Path filePath, String ipAddr) {
		return common.executeAsync(() -> execute(filePath, ipAddr));
	}

	/**
	 * BhProgramの実行環境を立ち上げ、BhProgramを実行する
	 * @param filePath BhProgramのファイルパス
	 * @param ipAddr BhProgramを実行するマシンのIPアドレス
	 * @return BhProgramの実行に成功した場合true
	 */
	private synchronized boolean execute(Path filePath, String ipAddr) {

		boolean success = true;

		if (programRunning.get())
			success &= terminate();

		MsgPrinter.INSTANCE.msgForUser("-- プログラム実行準備中 (local) --\n");
		if (success) {
			process = startExecEnvProcess();
			if (process == null) {
				success &= false;
			}
		}

		if (process != null) {
			String fileName = filePath.getFileName().toString();
			success &= common.runBhProgram(fileName, ipAddr, process.getInputStream());
		}

		if (!success) {	//リモートでのスクリプト実行失敗
			MsgPrinter.INSTANCE.errMsgForUser("!! プログラム実行準備失敗 (local) !!\n");
			MsgPrinter.INSTANCE.errMsgForDebug("failed to run " + filePath.getFileName() + " (local)");
			terminate();
		}
		else {
			MsgPrinter.INSTANCE.msgForUser("-- プログラム実行開始 (local) --\n");
			programRunning.set(true);
		}

		return success;
	}

	/**
	 * 現在実行中のBhProgramExecEnvironment を強制終了する
	 * @return BhProgram強制終了タスクのFutureオブジェクト. タスクを実行しなかった場合null.
	 */
	public Optional<Future<Boolean>> terminateAsync() {

		if (!programRunning.get()) {
			MsgPrinter.INSTANCE.errMsgForUser("!! プログラム終了済み (local) !!\n");
			return Optional.empty();
		}

		return common.terminateAsync(() -> terminate());
	}

	/**
	 * 現在実行中のBhProgramExecEnvironment を強制終了する.
	 * BhProgram実行環境を終了済みの場合に呼んでも問題ない.
	 * @return 強制終了に成功した場合true
	 */
	public synchronized boolean terminate() {

		MsgPrinter.INSTANCE.msgForUser("-- プログラム終了中 (local)  --\n");
		boolean success = common.haltTransceiver();

		if (process != null) {
			success &= common.waitForProcessEnd(process, true, BhParams.ExternalApplication.PROGRAM_EXEC_ENV_TERMINATION_TIMEOUT);
		}
		process = null;
		if (!success) {
			MsgPrinter.INSTANCE.errMsgForUser("!! プログラム終了失敗 (local)  !!\n");
		}
		else {
			MsgPrinter.INSTANCE.msgForUser("-- プログラム終了完了 (local)  --\n");
			programRunning.set(false);
		}
		return success;
	}

	/**
	 * BhProgram の実行環境と通信を行うようにする
	 * @return 接続タスクのFutureオブジェクト. タスクを実行しなかった場合null.
	 */
	public Optional<Future<Boolean>> connectAsync() {
		return common.connectAsync();
	}

	/**
	 * BhProgram の実行環境と通信を行わないようにする
	 * @return 切断タスクのFutureオブジェクト. タスクを実行しなかった場合null.
	 */
	public Optional<Future<Boolean>> disconnectAsync() {
		return common.disconnectAsync();
	}

	/**
	 * 引数で指定したデータをBhProgramの実行環境に送る
	 * @param data 送信データ
	 * @return 送信データリストにデータを追加できた場合true
	 */
	public BhProgramExecEnvError sendAsync(BhProgramData data) {
		return common.sendAsync(data);
	}

	/**
	 * BhProgramの実行環境プロセスをスタートする
	 * @return スタートしたプロセスのオブジェクト. スタートに失敗した場合null.
	 */
	private Process startExecEnvProcess() {

		Process proc = null;
		ProcessBuilder procBuilder = new ProcessBuilder(
			Util.JAVA_PATH,
			"-jar",
			Paths.get(Util.EXEC_PATH, BhParams.ExternalApplication.BH_PROGRAM_EXEC_ENV_JAR).toString(),
			"true");	//localFlag == true
		procBuilder.redirectErrorStream(true);

		try {
			proc = procBuilder.start();
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("startExecEnvProcess " +  e.toString());
		}

		return proc;
	}

	/**
	 * 終了処理をする
	 * @return 終了処理が正常に完了した場合true
	 */
	public boolean end() {

		boolean success = terminate();
		success &= common.end();
		return success;
	}
}
