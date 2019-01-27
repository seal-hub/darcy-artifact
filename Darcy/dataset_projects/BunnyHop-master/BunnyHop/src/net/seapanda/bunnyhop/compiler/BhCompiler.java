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
package net.seapanda.bunnyhop.compiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.modelprocessor.SyntaxSymbolIDCreator;

/**
 * BhNode をコンパイルするクラス.
 * @author K.Koike
 */
public class BhCompiler {

	public static final BhCompiler INSTANCE = new BhCompiler() {};	//!< シングルトンインスタンス
	private final VarDeclCodeGenerator varDeclCodeGen;
	private final FuncDefCodeGenerator funcDefCodeGen;
	private final StatCodeGenerator statCodeGen;
	private String commonCode;
	private String remoteCommonCode;
	private String localCommonCode;

	private BhCompiler(){
		CommonCodeGenerator common = new CommonCodeGenerator();
		ExpCodeGenerator expCodeGen = new ExpCodeGenerator(common);
		varDeclCodeGen = new VarDeclCodeGenerator(common);
		statCodeGen = new StatCodeGenerator(common, expCodeGen, varDeclCodeGen);
		funcDefCodeGen = new FuncDefCodeGenerator(common, statCodeGen, varDeclCodeGen);
	}
	
	/**
	 * コンパイルに必要な初期化処理を行う.
	 * @return 初期化に成功した場合true
	 */
	public boolean init() {
		
		Path commonCodePath = Paths.get(Util.EXEC_PATH, 
			BhParams.Path.BH_DEF_DIR, 
			BhParams.Path.FUNCTIONS_DIR, 
			BhParams.Path.lib, 
			BhParams.Path.COMMON_CODE_JS);
		Path remoteCommonCodePath = Paths.get(Util.EXEC_PATH, 
			BhParams.Path.BH_DEF_DIR, 
			BhParams.Path.FUNCTIONS_DIR, 
			BhParams.Path.lib, 
			BhParams.Path.REMOTE_COMMON_CODE_JS);
		Path localCommonCodePath = Paths.get(Util.EXEC_PATH, 
			BhParams.Path.BH_DEF_DIR, 
			BhParams.Path.FUNCTIONS_DIR, 
			BhParams.Path.lib, 
			BhParams.Path.LOCAL_COMMON_CODE_JS);
		try {
			byte[] content = Files.readAllBytes(commonCodePath);
			commonCode = new String(content, StandardCharsets.UTF_8);
			content = Files.readAllBytes(remoteCommonCodePath);
			remoteCommonCode = new String(content, StandardCharsets.UTF_8);
			content = Files.readAllBytes(localCommonCodePath);
			localCommonCode = new String(content, StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("failed to initialize + " + BhCompiler.class.getSimpleName() + "\n" + e.toString());
			return false;
		}		
		return true;
	}
	
	/**
	 * ワークスペース中のノードをコンパイルし, 作成されたファイルのパスを返す
	 * @param execNode 実行するノード
	 * @param compiledNodeList コンパイル対象のノードリスト (execNodeは含まない)
	 * @param option コンパイルオプション
	 * @return コンパイルした結果作成されたファイルのパス(コンパイルできた場合). <br>
	 *          コンパイルできなかった場合はOptional.empty
	 */
	public Optional<Path> compile(
		BhNode execNode, 
		List<BhNode> compiledNodeList, 
		CompileOption option) {
		
		if (!isExecutable(execNode))
			return Optional.empty();
		
		SyntaxSymbolIDCreator idCreator = new SyntaxSymbolIDCreator();
		execNode.accept(idCreator);
		compiledNodeList.forEach(compiledNode -> compiledNode.accept(idCreator));
		
		StringBuilder code = new StringBuilder();
		genCode(code, execNode, compiledNodeList, option);
		
		Util.createDirectoryIfNotExists(Paths.get(Util.EXEC_PATH, BhParams.Path.COMPILED_DIR));
		Path appFilePath = Paths.get(Util.EXEC_PATH, BhParams.Path.COMPILED_DIR, BhParams.Path.APP_FILE_NAME_JS);
		try (BufferedWriter writer = 
			Files.newBufferedWriter(
				appFilePath, 
				StandardCharsets.UTF_8, 
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING, 
				StandardOpenOption.WRITE)) {
			writer.write(code.toString());
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.alert(
				Alert.AlertType.ERROR,
				"ファイル書き込みエラー", 
				null,
				e.toString() + "\n" + appFilePath.toString());			
			return Optional.empty();
		}
		MsgPrinter.INSTANCE.msgForUser("\n-- コンパイル成功 --\n");
		return Optional.of(appFilePath);
	}
	
	/**
	 * プログラム全体のコードを生成する.
	 * @param code 生成したソースコードの格納先
	 * @param execNode 実行するノード
	 * @param compiledNodeList コンパイル対象のノードリスト (execNodeは含まない)
	 * @param option コンパイルオプション
	 */
	private void genCode(
		StringBuilder code, 
		BhNode execNode, 
		List<BhNode> compiledNodeList, 
		CompileOption option) {
		
		code.append("(")
			.append(Keywords.JS._function)
			.append("(){")
			.append(Util.LF);
		code.append(commonCode);
		if (option.local)
			code.append(localCommonCode);
		else
			code.append(remoteCommonCode);
		varDeclCodeGen.genVarDecls(compiledNodeList, code, 1, option);
		funcDefCodeGen.genFuncDefs(compiledNodeList, code, 1, option);
		statCodeGen.genStatement(execNode, code, 1, option);
		code.append("})();");
	}
	
	/**
	 * 引数で指定したノードが実行可能なノードかどうか判断する.
	 */
	private boolean isExecutable(BhNode node) {
		
		if (node.getState() != BhNode.State.ROOT_DIRECTLY_UNDER_WS) {
			MsgPrinter.INSTANCE.alert(
				Alert.AlertType.ERROR, 
				"実行ノードエラー",
				null,
				"処理の途中からは実行できません.");
			return false;
		}
		return true;
	}
					
	public static class Keywords {
		public static final String varPrefix = "_v";
		public static final String funcPrefix = "_f";
		
		public static class JS {
			public static final String _if = "if ";
			public static final String _else = "else ";
			public static final String _while = "while ";
			public static final String _for = "for ";
			public static final String _break = "break";
			public static final String _continue = "continue";
			public static final String _let = "let ";
			public static final String _const = "const ";
			public static final String _function = "function";
			public static final String _true = "true";
			public static final String _false = "false";
			public static final String _undefined = "undefined";
			public static final String _arguments = "arguments";
			public static final String _return = "return";
		}
	}
}


