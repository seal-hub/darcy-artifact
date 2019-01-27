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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.message.MsgTransporter;
import net.seapanda.bunnyhop.model.BhNode;

/**
 * @author K.Koike
 */
public class Util {

	public static final String EXEC_PATH;	//実行時jarパス
	public static final String JAVA_PATH;
	public static final String LF;
	private static int serialID = 0;

	static {

		String pathStr = System.getProperty("jdk.module.path");
		if (pathStr == null) {	//for self-contained app
			String path = System.getProperty("java.class.path");
			File jarFile = new File(path);
			Path jarPath = Paths.get(jarFile.getAbsolutePath());
			String root = (jarPath.getRoot() == null) ? "" : jarPath.getRoot().toString();
			EXEC_PATH = root + jarPath.subpath(0, jarPath.getNameCount()-1).toString();
		}
		else {
			EXEC_PATH = Paths.get(pathStr).toAbsolutePath().toString();
		}

		//EXEC_PATH = System.getProperty("user.dir");
		LF = System.getProperty("line.separator");
		String fs = System.getProperty("file.separator");
		JAVA_PATH = System.getProperty("java.home") + fs + "bin" + fs + "java";
	}

	/**
	 * ワイルドカード比較機能つき文字列一致検査.
	 * @param whole 比較対象の文字列. wildcard指定不可.
	 * @param part 比較対象の文字列. wildcard指定可.
	 * @return partにwildcard がある場合, wholeがpartを含んでいればtrue. <br>
	 * partにwildcard が無い場合, wholeとpartが一致すればtrue.
	 */
	public static boolean equals(String whole, String part) {

		if (whole == null || part == null)
			return false;

		if (!part.contains("*"))
			return whole.equals(part);

		return whole.contains(part.substring(0, part.indexOf('*')));
	}

	/**
	 * 引数で指定した文字列の表示幅を計算する
	 * @param str 表示幅を計算する文字列
	 * @param font 表示時のフォント
	 * @return 文字列を表示したときの幅
	 */
	public static double calcStrWidth(String str, Font font) {
        Text text = new Text(str);
        text.setFont(font);
		return text.getBoundsInLocal().getWidth();
	}

	/**
	 * シリアルIDを取得する
	 * @return シリアルID
	 */
	public static String genSerialID() {
		return Integer.toHexString(serialID++) + "";
	}

	/**
	 * 引数で指定したパスのファイルが存在しない場合作成する
	 * @param filePath 作成するファイルのパス
	 * @return 作成に失敗した場合false. 作成しなかった場合はtrue
	 */
	public static boolean createFileIfNotExists(Path filePath) {
		try {
			if (!Files.exists(filePath))
				Files.createFile(filePath);
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.msgForDebug("create file err " + filePath + "\n" + e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 引数で指定したパスのディレクトリが存在しない場合作成する
	 * @param dirPath 作成するファイルのパス
	 * @return 作成に失敗した場合false. 作成しなかった場合はtrue
	 */
	public static boolean createDirectoryIfNotExists(Path dirPath) {
		try {
			if (!Files.isDirectory(dirPath))
				Files.createDirectory(dirPath);
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.msgForDebug("create dir err " + dirPath + "\n" + e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 引数で指定したノードのワークスペース上での位置を取得する
	 * */
	public static Point2D getPosOnWS(BhNode node) {
		MsgData curPos = MsgTransporter.INSTANCE.sendMessage(BhMsg.GET_POS_ON_WORKSPACE, node);
		return new Point2D(curPos.doublePair._1, curPos.doublePair._2);
	}
}






