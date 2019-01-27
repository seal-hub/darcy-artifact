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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;

/**
 * Javascriptを管理するクラス
 * @author K.Koike
 * */
public class BhScriptManager {

	public static final BhScriptManager INSTANCE = new BhScriptManager();	//!< シングルトンインスタンス
	private final HashMap<String, CompiledScript> scriptName_script = new HashMap<>();	//!< スクリプト名とコンパイル済みスクリプトのマップ
	private final ScriptEngine engine = (new NashornScriptEngineFactory()).getScriptEngine("--language=es6");
	private Object commonJsObj;	//!< スクリプト共通で使うJavascriptオブジェクト

	/**
	 * Javascript 実行時の変数スコープを新たに作成する
	 * @return Javascript 実行時の変数スコープ
	 */
	public Bindings createScriptScope() {
		return engine.createBindings();
	}
	
	/**
	 * Javascriptのファイルパスからコンパイル済みスクリプトを取得する
	 * @param fileName 取得したいスクリプトのファイル名
	 * @return jsPath で指定したスクリプト
	 * */
	public CompiledScript getCompiledScript(String fileName) {
		return scriptName_script.get(fileName);
	}

	/**
	 * Javascriptファイルを読み込み、コンパイルする
	 * @param dirPaths このフォルダの下にある.jsファイルをコンパイルする
	 * @return ひとつでもコンパイル不能なJSファイルがあった場合 false を返す
	 * */
	public boolean genCompiledCode(Path... dirPaths) {

		boolean success = true;
		for (Path dirPath : dirPaths) {
			Stream<Path> paths;	//読み込むファイルパスリスト
			try {
				paths = Files.walk(dirPath, FOLLOW_LINKS).filter(path -> path.getFileName().toString().endsWith(".js")); //.jsファイルだけ収集
			}
			catch (IOException e) {
				MsgPrinter.INSTANCE.errMsgForDebug(BhParams.Path.FUNCTIONS_DIR + " directory not found " + dirPath);
				success &= false;
				continue;
			}

			success &= paths.map(path -> {
				
				try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
					CompiledScript cs = ((Compilable)engine).compile(reader);
					scriptName_script.put(path.getFileName().toString(), cs);
				}
				catch (IOException e) {
					MsgPrinter.INSTANCE.errMsgForDebug(e.toString() + "  " + path.toString());
					return false;
				}
				catch (ScriptException e) {
					MsgPrinter.INSTANCE.errMsgForDebug(path.toUri() + " がコンパイルできません.");
					MsgPrinter.INSTANCE.errMsgForDebug(e.toString());
					return false;
				}
				return true;
			}).allMatch(bool -> bool);
			
		}
		
		if (scriptName_script.containsKey(BhParams.Path.COMMON_EVENT_JS)) {
			try {
				commonJsObj = scriptName_script.get(BhParams.Path.COMMON_EVENT_JS).eval();
			} catch (ScriptException e) {
				MsgPrinter.INSTANCE.errMsgForDebug("exec " + BhParams.Path.COMMON_EVENT_JS + "\n" + e.toString() + "\n");
				success &= false;
			}
		}
		
		return success;
	}
	
	/**
	 * 引数で指定したスクリプト名に対応するスクリプトが存在するかどうかチェックする
 	 * @param fileName スクリプト名の書いてあるファイル名
	 * @param scriptNames スクリプトが存在するかどうか調べるスクリプト名
	 * @return 引数で指定したスクリプト名に対応するスクリプトが全て見つかった場合true
	 */
	public boolean scriptsExist(String fileName, String... scriptNames) {
		
		Stream<String> scriptNameStream = Stream.of(scriptNames);
		return scriptNameStream.allMatch(scriptName ->{
			boolean found = scriptName_script.get(scriptName) != null;
			if (!found) {
				MsgPrinter.INSTANCE.errMsgForDebug(scriptName + " が見つかりません.  file: " + fileName);
			}	
			return found;
		});
	}
	
	/**
	 * Jsonファイルをパースしてオブジェクトにして返す
	 * @param filePath Jsonファイルのパス
	 * @return Jsonファイルをパースしてできたオブジェクト
	 */
	public ScriptObjectMirror parseJsonFile(Path filePath) {
		
		Object jsonObj = null;
		try {
			byte[] contents = Files.readAllBytes(filePath);
			String jsCode = new String(contents, StandardCharsets.UTF_8);
			jsCode = "var content = " + jsCode + ";" + Util.LF;
			jsCode += "(function () {return content;})();";
			CompiledScript cs = ((Compilable)engine).compile(jsCode);
			jsonObj = cs.eval();
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("cannot read json file.  " + filePath + "\n" + e.toString() + "\n");
			return null;
		}
		catch (ScriptException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("cannot parse json file.  " + filePath + "\n" + e.toString() + "\n");
			return null;
		}
		if (!(jsonObj instanceof ScriptObjectMirror)) {
			MsgPrinter.INSTANCE.errMsgForDebug("cannot parse json file.  " + filePath);
			return null;
		}
		return (ScriptObjectMirror)jsonObj;
	}
	
	/**
	 * スクリプトが共通で使うオブジェクトを返す
	 */
	public Object getCommonJsObj() {
		return commonJsObj;
	}
}
