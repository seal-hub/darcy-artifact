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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.seapanda.bunnyhop.bhprogram.common.BhProgramData;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.configfilereader.BhScriptManager;

/**
 * BunnyHopで作成したプログラムのローカル環境での実行、終了、通信を行うクラス
 * @author K.Koike
 */
public class RemoteBhProgramManager {

	public static final RemoteBhProgramManager INSTANCE = new RemoteBhProgramManager();	//!< シングルトンインスタンス
	private final BhProgramManagerCommon common = new BhProgramManagerCommon();
	private final Bindings bindings = BhScriptManager.INSTANCE.createScriptScope();
	private String[] killCmd;
	private AtomicReference<Boolean> programRunning = new AtomicReference<>(false);	//!< プログラム実行中ならtrue
	private AtomicReference<Boolean> fileCopyIsCancelled = new AtomicReference<>(true);	//!< ファイルがコピー中の場合true

	private RemoteBhProgramManager() {}

	public boolean init() {
		boolean success = common.init();
		success &= BhScriptManager.INSTANCE.scriptsExist(RemoteBhProgramManager.class.getSimpleName(),
			BhParams.Path.REMOTE_EXEC_CMD_GENERATOR_JS,
			BhParams.Path.REMOTE_KILL_CMD_GENERATOR_JS,
			BhParams.Path.COPY_CMD_GENERATOR_JS);

		if (!success)
			MsgPrinter.INSTANCE.errMsgForDebug("failed to initialize " + RemoteBhProgramManager.class.getSimpleName());
		return success;
	}

	/**
	 * BhProgramを実行する
	 * @param filePath BhProgramのファイルパス
	 * @param ipAddr BhProgramを実行するマシンのIPアドレス
	 * @param uname BhProgramを実行するマシンにログインする際のユーザ名
	 * @param password BhProgramを実行するマシンにログインする際のパスワード
	 * @return BhProgram実行開始の完了待ちオブジェクト
	 */
	public Optional<Future<Boolean>> executeAsync(Path filePath, String ipAddr, String uname, String password) {
		return common.executeAsync(() -> execute(filePath, ipAddr, uname, password));
	}

	/**
	 * BhProgramを実行する
	 * @param filePath BhProgramのファイルパス
	 * @param ipAddr BhProgramを実行するマシンのIPアドレス
	 * @return BhProgramの実行に成功した場合true
	 */
	private synchronized boolean execute(Path filePath, String ipAddr, String uname, String password) {

		boolean success = true;

		if (programRunning.get())
			success &= terminate();

		MsgPrinter.INSTANCE.msgForUser("-- プログラム実行準備中 (remote) --\n");
		setScriptBindings(ipAddr, uname, password);
		killCmd = genKillCmd();
		String[] copyCmd = genCopyCmd();
		if (killCmd == null || copyCmd == null)
			success = false;

		if (copyCmd != null) {
			success &= copyFile(copyCmd);
		}

		Process remoteEnvProcess = null;
		if (success)
			remoteEnvProcess = startExecEnvProcess();	//リモート実行環境起動

		if (remoteEnvProcess != null) {
			String fileName = filePath.getFileName().toString();
			success &= common.runBhProgram(fileName, ipAddr, remoteEnvProcess.getInputStream());
			// リモートの実行環境の起動に使ったローカルのプロセスは終了しておく
			success &= common.waitForProcessEnd(remoteEnvProcess, true, BhParams.ExternalApplication.PROGRAM_EXEC_ENV_TERMINATION_TIMEOUT);
		}

		if (!success) {	//リモートでのスクリプト実行失敗
			MsgPrinter.INSTANCE.errMsgForUser("!! プログラム実行準備失敗 (remote) !!\n");
			MsgPrinter.INSTANCE.errMsgForDebug("failed to run " + filePath.getFileName() + " (remote)");
			terminate();
		}
		else {
			MsgPrinter.INSTANCE.msgForUser("-- プログラム実行開始 (remote) --\n");
			programRunning.set(true);
		}

		return success;
	}

	/**
	 * 現在実行中のBhProgramExecEnvironment を強制終了する
	 * @return 強制終了タスクの結果. タスクを実行しなかった場合null.
	 */
	public Optional<Future<Boolean>> terminateAsync() {

		fileCopyIsCancelled.set(true);
		if (!programRunning.get()) {
			MsgPrinter.INSTANCE.errMsgForUser("!! プログラム終了済み (remote) !!\n");
			return Optional.empty();
		}
		return common.terminateAsync(() -> {
			return terminate();
		});
	}

	/**
	 * リモートマシンで実行中のBhProgram実行環境を強制終了する. <br>
	 * BhProgram実行環境を終了済みの場合に呼んでも問題ない.
	 * @return 強制終了に成功した場合true
	 */
	private synchronized boolean terminate() {

		MsgPrinter.INSTANCE.msgForUser("-- プログラム終了中 (remote)  --\n");
		boolean success = common.haltTransceiver();

		if (killCmd != null) {
			Process process = execCmd(killCmd);
			if (process != null)
				success &= common.waitForProcessEnd(process, false, BhParams.ExternalApplication.PROGRAM_EXEC_ENV_TERMINATION_TIMEOUT);
			else
				success = false;
		}

		if (!success) {
			MsgPrinter.INSTANCE.errMsgForUser("!! プログラム終了失敗 (remote)  !!\n");
		}
		else {
			MsgPrinter.INSTANCE.msgForUser("-- プログラム終了完了 (remote)  --\n");
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

		Process process = null;
		CompiledScript cs = BhScriptManager.INSTANCE.getCompiledScript(BhParams.Path.REMOTE_EXEC_CMD_GENERATOR_JS);
		Object retVal = null;
		try {
			retVal = cs.eval(bindings);
		}
		catch(ScriptException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("failed to eval " +  BhParams.Path.REMOTE_EXEC_CMD_GENERATOR_JS + " " + e.toString());
			return null;
		}

		String[] cmdArray = null;
		if (retVal instanceof ScriptObjectMirror)
			cmdArray = convertToStrArray((ScriptObjectMirror)retVal);

		if (cmdArray == null)
			return null;

		ProcessBuilder procBuilder = new ProcessBuilder(cmdArray);
		procBuilder.redirectErrorStream(true);
		try {
			process = procBuilder.start();
		}
		catch (IOException | IndexOutOfBoundsException | SecurityException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("failed to start " +  BhParams.ExternalApplication.BH_PROGRAM_EXEC_ENV_JAR + "\n" + e.toString());
		}
		return process;
	}

	/**
	 * BhProgram実行環境終了のコマンドを作成する
	 * @return BhProgram実行環境終了のコマンド. 作成に失敗した場合null.
	 */
	private String[] genKillCmd() {

		CompiledScript cs = BhScriptManager.INSTANCE.getCompiledScript(BhParams.Path.REMOTE_KILL_CMD_GENERATOR_JS);
		Object retVal;
		try {
			retVal = cs.eval(bindings);
		}
		catch(ScriptException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("failed to eval" +  BhParams.Path.REMOTE_KILL_CMD_GENERATOR_JS + " " + e.toString());
			return null;
		}

		String[] cmdArray = null;
		if (retVal instanceof ScriptObjectMirror)
			cmdArray = convertToStrArray((ScriptObjectMirror)retVal);
		return cmdArray;
	}

	/**
	 * BhProgram実行環境終了のコマンドを作成する
	 * @return BhProgramファイルコピーのコマンド. 作成に失敗した場合null.
	 */
	private String[] genCopyCmd() {

		CompiledScript cs = BhScriptManager.INSTANCE.getCompiledScript(BhParams.Path.COPY_CMD_GENERATOR_JS);
		Object retVal = null;
		try {
			retVal = cs.eval(bindings);
		}
		catch(ScriptException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("failed to eval" +  BhParams.Path.COPY_CMD_GENERATOR_JS + " " + e.toString());
			return null;
		}

		String[] cmdArray = null;
		if (retVal instanceof ScriptObjectMirror)
			cmdArray = convertToStrArray((ScriptObjectMirror)retVal);
		return cmdArray;
	}

	/**
	 * 引数で指定したコマンドを実行する
	 * @param cmd 実行するコマンド
	 * @return コマンドを実行したプロセスのオブジェクト. コマンド実行した場合はnull
	 */
	private Process execCmd(String[] cmd) {

		Process process = null;
		ProcessBuilder procBuilder = new ProcessBuilder(cmd);
		procBuilder.redirectErrorStream(true);
		try {
			process = procBuilder.start();
		}
		catch (IOException | IndexOutOfBoundsException | SecurityException e) {
			String cmdStr = Stream.of(cmd).reduce("", (p1, p2) -> p1 + " " + p2);
			MsgPrinter.INSTANCE.errMsgForDebug("failed to execCmd " +  cmdStr + "\n" + e.toString());
		}
		return process;
	}

	/**
	 * リモート環境にファイルをコピーする
	 * @param copyCmd コピーコマンド
	 * @return コピーが正常に終了した場合true
	 */
	private boolean copyFile(String[] copyCmd) {

		MsgPrinter.INSTANCE.msgForUser("-- プログラム転送中 --\n");
		boolean success = true;

		fileCopyIsCancelled.set(false);

		Process copyProcess = execCmd(copyCmd);
		if (copyProcess != null) {
			success &= showFileCopyProgress(copyProcess);
			boolean terminate = !success;	//進捗表示を途中で終わらせていた場合, ファイルコピープロセスを強制終了する
			success &= common.waitForProcessEnd(copyProcess, terminate, BhParams.ExternalApplication.FILE_COPY_TERMINATION_TIMEOUT);
			success &= (copyProcess.exitValue() == 0);
		}

		if (!success) {
			MsgPrinter.INSTANCE.errMsgForDebug(RemoteBhProgramManager.class.getSimpleName() + ".copyFile  \n");
			MsgPrinter.INSTANCE.errMsgForUser("!! プログラム転送失敗 !!\n");
		}

		return success;
	}

	/**
	 * ファイルコピーの進捗状況を知らせる
	 * @param fileCopyProc ファイルコピーをしているプロセス
	 * @return コピープロセスが終了後にこのメソッドから戻った場合true
	 **/
	private boolean showFileCopyProgress(Process copyProcess) {

		long begin = System.currentTimeMillis();

		boolean success = true;
		List<Character> charCodeList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(copyProcess.getInputStream()))){
			while (true) {

				//ファイル転送プロセスからの応答がない場合, メッセージを出力する
				if (System.currentTimeMillis() - begin > 5000) {
					begin = System.currentTimeMillis();
					MsgPrinter.INSTANCE.errMsgForUser("!! ファイル転送プロセス応答なし !!\n");
				}

				if (fileCopyIsCancelled.get())	{//コピー停止命令を受けた
					success = false;
					break;
				}


				boolean procIsAlice = copyProcess.isAlive();
				if (br.ready()) {	//次の読み出し結果がEOFの場合 false
					int charCode = br.read();
					switch (charCode) {
						case '\r':
						case '\n':	//<- pscp の場合 '\n' は入力されないので不要.
							if (!charCodeList.isEmpty()) {
								MsgPrinter.INSTANCE.msgForUser(charCodeList);
								begin = System.currentTimeMillis();
								charCodeList.clear();
							}
							break;

						default:	//改行以外の文字コード
							charCodeList.add((char)charCode);
					}
				}
				else {
					Thread.sleep(100);
				}

				if (!procIsAlice) {
					MsgPrinter.INSTANCE.msgForUser(charCodeList);
					break;
				}
			}
		}
		catch(IOException | InterruptedException e) {
			success = false;
		}
		return success;
	}

	/**
	 * コマンド生成スクリプトが使う情報をマップオブジェクトにセットする
	 * @param ipAddr 実行環境があるマシンのIPアドレス
	 * @param uname 実行環境があるマシンに接続する際のユーザ名
	 * @param password 実行環境があるマシンに接続する際のパスワード
	 */
	private void setScriptBindings(String ipAddr, String uname, String password) {

		bindings.put(BhParams.JsKeyword.KEY_IP_ADDR, ipAddr);
		bindings.put(BhParams.JsKeyword.KEY_UNAME, uname);
		bindings.put(BhParams.JsKeyword.KEY_PASSWORD, password);
		bindings.put(BhParams.JsKeyword.KEY_BH_PROGRAM_FILE_PATH,
					 Paths.get(Util.EXEC_PATH, BhParams.Path.COMPILED_DIR, BhParams.Path.APP_FILE_NAME_JS).toString());
	}

	/**
	 * ScriptObjectMirrorをString配列に変換する
	 * @param scriptObj 文字列配列に変換するScriptObjectMirror
	 * @return scriptObjを変換したString配列. scriptObjが配列でない場合nullを返す.
	 */
	private String[] convertToStrArray(ScriptObjectMirror scriptObj) {

		if (!scriptObj.isArray())
			return null;

		String[] cmdArray = new String[scriptObj.size()];
		for (String key : scriptObj.keySet()) {
			Object elem = scriptObj.get(key);
			try {
				int idx = Integer.parseInt(key);
				cmdArray[idx] = elem.toString();
			}
			catch (NumberFormatException e) {
			}
		}
		return cmdArray;
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
