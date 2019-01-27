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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.SyntaxSymbol;
import net.seapanda.bunnyhop.model.TextNode;

/**
 * @author K.Koike
 * 変数定義のコードを生成するクラス
 */
final public class VarDeclCodeGenerator {
	
	private final CommonCodeGenerator common;
		
	public VarDeclCodeGenerator(CommonCodeGenerator common) {
		this.common = common;
	}
	
	/**
	 * 変数定義のコードを作成する
	 * @param nodeListToCompile コンパイル対象のノードリスト
	 * @param code 生成したコードの格納先
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	public void genVarDecls(
		List<? extends SyntaxSymbol> nodeListToCompile,
		StringBuilder code,
		int nestLevel, 
		CompileOption option) {
		
		List<VarDeclCodeGenerator.VarDeclInfo> varDeclInfoList = new ArrayList<>();
		nodeListToCompile.forEach(node -> {
			if (SymbolNames.VarDecl.VAR_DECL_LIST.contains(node.getSymbolName())) {
				genVarDeclInfos(node, varDeclInfoList);
			}
		});
		genVarDecls(code, varDeclInfoList, nestLevel, option);
	}
	
	/**
	 * 変数定義のコードを作成する
	 * @param varDeclNode 変数定義ノード
	 * @param code 生成したコードの格納先
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	public void genVarDecls(
		SyntaxSymbol varDeclNode,
		StringBuilder code,
		int nestLevel, 
		CompileOption option) {
		
		List<VarDeclCodeGenerator.VarDeclInfo> varDeclInfoList = new ArrayList<>();
		genVarDeclInfos(varDeclNode, varDeclInfoList);
		genVarDecls(code, varDeclInfoList, nestLevel, option);
	}
	
	/**
	 * 仮引数リストを作成する
	 * @param paramNode 仮引数のノード
	 * @param outParamNode 出力引数ノード
	 * @param code 生成したコードの格納先
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @return 出力引数のインデックス. emptyの場合は出力引数なし.
	 */	
	public Optional<Integer> genParamList(
		SyntaxSymbol paramNode,
		SyntaxSymbol outParamNode,
		StringBuilder code,
		int nestLevel,
		CompileOption option) {
		
		List<VarDeclCodeGenerator.VarDeclInfo> varDeclInfoList = new ArrayList<>();
		List<VarDeclCodeGenerator.VarDeclInfo> outVarDeclInfoList = new ArrayList<>();
		genVarDeclInfos(paramNode, varDeclInfoList);
		genVarDeclInfos(outParamNode, outVarDeclInfoList);
		varDeclInfoList.addAll(outVarDeclInfoList);
		
		if (varDeclInfoList.size() >= 1)
			code.append(Util.LF);
		
		for (int i = 0; i < varDeclInfoList.size(); ++i) {
			
			VarDeclCodeGenerator.VarDeclInfo varDeclInfo = varDeclInfoList.get(i);
			code.append(common.indent(nestLevel))
				.append(varDeclInfo.varName);
			boolean isLastParam = i == (varDeclInfoList.size() - 1);
			if (!isLastParam) {
				code.append(",");
			}
			if (option.withComments) {
				code.append(" /*").append(varDeclInfo.comment).append("*/");
			}
			if (!isLastParam) {
				code.append(Util.LF);
			}
		}
		
		if (outVarDeclInfoList.isEmpty())
			return Optional.empty();
		
		return Optional.of(varDeclInfoList.size() - outVarDeclInfoList.size());
	}
	
	
	/**
	 * 変数定義ノードから変数定義リストを取得する
	 * @param varDeclNode 変数定義ノード
	 * @param varDeclInfoList 変数定義に必要な情報のリスト
	 */
	private void genVarDeclInfos(SyntaxSymbol varDeclNode, List<VarDeclInfo> varDeclInfoList) {
	
		if (!SymbolNames.VarDecl.VAR_DECL_LIST.contains(varDeclNode.getSymbolName()))
			return;
				
		TextNode varIdTextNode = (TextNode)varDeclNode.findSymbolInDescendants("*", SymbolNames.VarDecl.VAR_NAME, "*");
		if (varIdTextNode == null)
			varIdTextNode = (TextNode)varDeclNode.findSymbolInDescendants("*", SymbolNames.VarDecl.LIST_NAME, "*");
		
		String varName = common.genVarName(varDeclNode);
		String comment = varIdTextNode.getText();
		String initVal = SymbolNames.VarDecl.INIT_VAL_MAP.get(varDeclNode.getSymbolName());
		varDeclInfoList.add(new VarDeclInfo(varName, initVal, comment));
		
		SyntaxSymbol nextVarDecl = varDeclNode.findSymbolInDescendants("*", SymbolNames.VarDecl.NEXT_VAR_DECL, "*");
		genVarDeclInfos(nextVarDecl, varDeclInfoList);
	}
	
	/**
	 * 変数定義リストのコードを生成する
	 * @param code 生成したコードの格納先
	 * @param varDeclInfoList 変数定義に必要な情報のリスト
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	private void genVarDecls(
		StringBuilder code,
		List<VarDeclInfo> varDeclInfoList, 
		int nestLevel, 
		CompileOption option) {
		
		varDeclInfoList.forEach(varDeclInfo -> {
			
			code.append(common.indent(nestLevel))
				.append(BhCompiler.Keywords.JS._let)
				.append(varDeclInfo.varName)
				.append(" = ")
				.append(varDeclInfo.initVal)
				.append(";");
			
			if (option.withComments) {
				code.append(" /*")
					.append(varDeclInfo.comment)
					.append("*/");
			}
			code.append(Util.LF);
		});
	}
	
	/**
	 * 変数定義に必要な情報
	 */
	private static class VarDeclInfo {
		
		public final String varName;	//!< 変数名
		public final String initVal;	//!< 初期値
		public final String comment;	//!< コメント (デバッグ用)
		
		public VarDeclInfo(String varName, String initVal, String comment) {
			this.varName = varName;
			this.initVal = initVal;
			this.comment = comment;
		}
	}
}
