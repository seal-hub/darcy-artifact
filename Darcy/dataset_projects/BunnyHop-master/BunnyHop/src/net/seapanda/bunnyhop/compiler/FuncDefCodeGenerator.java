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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.SyntaxSymbol;
import net.seapanda.bunnyhop.model.TextNode;

/**
 * 関数定義のコード生成を行うクラス
 * @author K.Koike
 */
public class FuncDefCodeGenerator {
	
	private final CommonCodeGenerator common;
	private final StatCodeGenerator statCodeGen;
	private final VarDeclCodeGenerator varDeclCodeGen;
	
	public FuncDefCodeGenerator(
		CommonCodeGenerator common,
		StatCodeGenerator statCodeGen,
		VarDeclCodeGenerator varDeclCodeGen) {
		this.common = common;
		this.statCodeGen = statCodeGen;
		this.varDeclCodeGen = varDeclCodeGen;
	}
	
	/**
	 * 関数定義のコードを作成する
	 * @param compiledNodeList コンパイル対象のノードリスト
	 * @param code 生成したコードの格納先
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	public void genFuncDefs(
		List<? extends SyntaxSymbol> compiledNodeList,
		StringBuilder code, 
		int nestLevel,
		CompileOption option) {
		
		compiledNodeList.forEach(symbol -> {
			if (SymbolNames.UserDefFunc.USER_DEF_FUNC_LIST.contains(symbol.getSymbolName())) {
				genFuncDef(symbol, code, nestLevel, option);
			}
		});
	}
	
	/**
	 * 関数定義のコードを作成する
	 * @param funcDefNode 関数定義のノード
	 * @param code 生成したコードの格納先
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	private void genFuncDef(
		SyntaxSymbol funcDefNode,
		StringBuilder code, 
		int nestLevel,
		CompileOption option) {
	
		String funcName = common.genFuncName(funcDefNode);
		code.append(common.indent(nestLevel))
			.append(BhCompiler.Keywords.JS._function)
			.append(" ")
			.append(funcName)
			.append("(");

		SyntaxSymbol param = funcDefNode.findSymbolInDescendants("*", "*", SymbolNames.UserDefFunc.PARAM_DECL, "*");
		SyntaxSymbol outParam = funcDefNode.findSymbolInDescendants("*", "*", SymbolNames.UserDefFunc.OUT_PARAM_DECL, "*");
		Optional<Integer> outParamIdxOpt = varDeclCodeGen.genParamList(param, outParam, code, nestLevel + 1, option);
		code.append(") {")
			.append(Util.LF);
		genFuncDefInner(funcDefNode, code, nestLevel, option);
		outParamIdxOpt.ifPresent(outParamIdx -> genOutArgCopy(code, outParamIdx, nestLevel + 1));
		
		code.append(common.indent(nestLevel))
			.append("}")
			.append(Util.LF)
			.append(Util.LF);
	}
	
	/**
	 * 関数定義のコードの内部関数部分を作成する
	 * @param funcDefNode 関数定義のノード
	 * @param code 生成したコードの格納先
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	private void genFuncDefInner(
		SyntaxSymbol funcDefNode,
		StringBuilder code, 
		int nestLevel,
		CompileOption option) {
		
		code.append(common.indent(nestLevel + 1))
			.append("(")
			.append(BhCompiler.Keywords.JS._function)
			.append("(){");
		if (option.withComments) {
			TextNode funcNameNode = ((TextNode)funcDefNode.findSymbolInDescendants("*", "*", SymbolNames.UserDefFunc.FUNC_NAME, "*"));
			code.append("/*")
				.append(funcNameNode.getText())
				.append("*/");
		}
		code.append(Util.LF);
		
		SyntaxSymbol stat = funcDefNode.findSymbolInDescendants("*", "*", SymbolNames.Stat.STAT_LIST, "*");
		statCodeGen.genStatement(stat, code, nestLevel + 2, option);
		code.append(common.indent(nestLevel+1))
			.append("})();")
			.append(Util.LF);	
	}
	
	/**
	 * 出力引数のコピーコードを作成する
	 * @param code 生成したコードの格納先
	 * @param outParamIdx 出力引数の先頭インデックス
	 * @param nestLevel ソースコードのネストレベル
	 */
	private void genOutArgCopy(StringBuilder code, int outParamIdx, int nestLevel) {
			
		String copyArgsFuncName = SymbolNames.PreDefFunc.PREDEF_FUNC_NAME_MAP.get(Arrays.asList(SymbolNames.PreDefFunc.COPY_ARGS));
		String copyArgsCode = common.genFuncCallCode(
			copyArgsFuncName, 
			SymbolNames.PreDefVars.OUT_ARGS, 
			BhCompiler.Keywords.JS._arguments, 
			outParamIdx + "");
		code.append(common.indent(nestLevel))
			.append(copyArgsCode)
			.append(";")
			.append(Util.LF);
	}
}











