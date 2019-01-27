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
package net.seapanda.bunnyhop.common.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import net.seapanda.bunnyhop.common.BhParams;

/**
 * ログ出力クラス
 * @author K.Koike
 * */
public class LogManager {

	public static final LogManager INSTANCE = new LogManager();	//!< シングルトンインスタンス
	private OutputStream logOutputStream;

	private LogManager() {}

	public boolean init() {
		initLogSystem();	//ログシステムがエラーでも処理は続ける
		return true;
	}

	/**
	 * ログ機能を初期化する
	 */
	private boolean initLogSystem() {

		Path logFilePath = genLogFilePath(0);
		if (!Util.createDirectoryIfNotExists(Paths.get(Util.EXEC_PATH, BhParams.Path.LOG_DIR)))
			return false;

		if (!Util.createFileIfNotExists(logFilePath))
			return false;

		try {
			//ログローテーション
			if (Files.size(logFilePath) > BhParams.LOG_FILE_SIZE_LIMIT)
				if (!renameLogFiles())
					return false;
			logOutputStream = Files.newOutputStream(logFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
		}
		catch (IOException | SecurityException e) {
			return false;
		}
		return true;
	}

	/**
	 * ログファイルにメッセージを書き込む
	 * @param msg ログファイルに書き込むメッセージ
	 */
	public void writeMsgToLogFile(String msg) {

		try {
			if (logOutputStream != null) {
				logOutputStream.write(msg.getBytes(StandardCharsets.UTF_8));
				logOutputStream.flush();
			}
		}
		catch(IOException | SecurityException e) {}
	}

	/**
	 * ログローテンションのため, ログファイルをリネームする
	 * @return リネームに成功した場合true
	 */
	private boolean renameLogFiles() {

		try {
			Path oldestLogFilePath = genLogFilePath(BhParams.MAX_LOG_FILE_NUM - 1);
			if (Files.exists(oldestLogFilePath))
				Files.delete(oldestLogFilePath);

			for (int fileNo = BhParams.MAX_LOG_FILE_NUM - 2; fileNo >= 0; --fileNo) {
				Path oldLogFilePath = genLogFilePath(fileNo);
				Path newLogFilePath = genLogFilePath(fileNo + 1);
				if (Files.exists(oldLogFilePath))
					Files.move(oldLogFilePath, newLogFilePath, StandardCopyOption.ATOMIC_MOVE);
			}
		}
		catch (IOException | SecurityException e) {
			return false;
		}
		return true;
	}

	private Path genLogFilePath(int fileNo) {
		String numStr = ("0000" + fileNo);
		numStr = numStr.substring(numStr.length() - 4, numStr.length());
		String logFileName = BhParams.Path.LOG_FILE_NAME + numStr + ".log";
		return Paths.get(Util.EXEC_PATH, BhParams.Path.LOG_DIR, logFileName);
	}


	/**
	 * 終了処理をする
	 */
	public void end() {
		try {
			if (logOutputStream != null)
				logOutputStream.close();
		}
		catch (IOException e) {}
	}
}