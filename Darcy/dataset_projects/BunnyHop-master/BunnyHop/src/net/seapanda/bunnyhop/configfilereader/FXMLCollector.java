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
package net.seapanda.bunnyhop.configfilereader;

import java.io.IOException;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;

/**
 * FXMLファイルとそのパスを保存するクラス
 * @author Koike
 */
public class FXMLCollector {
	
	public static final FXMLCollector INSTANCE = new FXMLCollector();
	private static final Map<String, Path> fileName_filePath = new HashMap<>();
	
	private FXMLCollector(){}
	
	/**
	 * FXMLファイルのファイル名とそのパスを集める
	 * @return FXMLファイルのフォルダが見つからなかった場合 falseを返す
	 */
	public boolean collectFXMLFiles() {
		
		Path dirPath = Paths.get(Util.EXEC_PATH, BhParams.Path.VIEW_DIR, BhParams.Path.FXML_DIR);
		Stream<Path> paths;	//読み込むファイルパスリスト
		try {
			paths = Files.walk(dirPath, FOLLOW_LINKS).filter(path -> path.getFileName().toString().endsWith(".fxml")); //.fxmlファイルだけ収集
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("fxml directory not found " + dirPath + "\n" + e.toString());
			return false;
		}
		paths.forEach(filePath -> fileName_filePath.put(filePath.getFileName().toString(), filePath));
		return true;
	}
	
	/**
	 * FXMLファイル名からそのファイルのフルパスを取得する
	 * @param fileName フルパスを知りたいFXMLファイル名
	 * @return fileName で指定したファイルのパスオブジェクト. パスが見つからない場合はnullを返す
	 */
	public Path getFilePath(String fileName) {
		return fileName_filePath.getOrDefault(fileName, null);
	}
}
