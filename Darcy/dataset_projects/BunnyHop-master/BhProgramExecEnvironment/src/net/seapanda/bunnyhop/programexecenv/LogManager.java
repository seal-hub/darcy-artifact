package net.seapanda.bunnyhop.programexecenv;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * ログ出力クラス
 * @author K.Koike
 * */
public class LogManager {

	public static final LogManager INSTANCE = new LogManager();	//!< シングルトンインスタンス

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
		}
		catch (IOException | SecurityException e) {
			return false;
		}
		return true;
	}

	/**
	 * デバッグ用メッセージ出力メソッド
	 * */
	public void errMsgForDebug(String msg) {
		msg = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(Calendar.getInstance().getTime()) + "  ERR : " + msg;
		writeMsgToLogFile(msg + "\n");
	}

	/**
	 * デバッグ用メッセージ出力メソッド
	 * */
	public void msgForDebug(String msg) {
		msg = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(Calendar.getInstance().getTime()) + "  MSG : " + msg;
		writeMsgToLogFile(msg + "\n");
	}

	/**
	 * ログファイルにメッセージを書き込む
	 * @param msg ログファイルに書き込むメッセージ
	 */
	private void writeMsgToLogFile(String msg) {

		try(OutputStream logOutputStream =
				Files.newOutputStream(
					genLogFilePath(0),
					StandardOpenOption.CREATE,
					StandardOpenOption.APPEND,
					StandardOpenOption.WRITE);) {
			logOutputStream.write(msg.getBytes(StandardCharsets.UTF_8));
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
}





