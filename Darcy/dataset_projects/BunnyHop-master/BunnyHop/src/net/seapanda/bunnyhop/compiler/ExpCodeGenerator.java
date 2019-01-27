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
import java.util.Arrays;
import java.util.List;

import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.SyntaxSymbol;
import net.seapanda.bunnyhop.model.TextNode;
import net.seapanda.bunnyhop.model.imitation.Imitatable;

/**
 * 式のコード生成を行うクラス
 * @author K.Koike
 */
public class ExpCodeGenerator {

	private final CommonCodeGenerator common;

	public ExpCodeGenerator(CommonCodeGenerator common) {
		this.common = common;
	}

	/**
	 * 式を作成する
	 * @param code 途中式の格納先
	 * @param expNode 式のノード
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @return 式もしくは式の評価結果を格納した変数
	 */
	public String genExpression(
		StringBuilder code,
		SyntaxSymbol expNode,
		int nestLevel,
		CompileOption option) {

		String expSymbolName = expNode.getSymbolName();
		if (SymbolNames.BinaryExp.LIST.contains(expSymbolName)) {
			return genBinaryExp(code, expNode, nestLevel, option);
		}
		else if (SymbolNames.UnaryExp.LIST.contains(expSymbolName)) {
			return genUnaryExp(code, expNode, nestLevel, option);
		}
		else if (SymbolNames.VarDecl.VAR_LIST.contains(expSymbolName)) {
			Imitatable varNode = (Imitatable)expNode;
			return common.genVarName(varNode.getOriginalNode());
		}
		else if (SymbolNames.Literal.LIST.contains(expSymbolName)) {
			return genLiteral(expNode);
		}
		else if (SymbolNames.PreDefFunc.PREDEF_FUNC_CALL_EXP_LIST.contains(expSymbolName)) {
			return genPreDefFuncCallExp(code, expNode, nestLevel, option, true);
		}
		else if (SymbolNames.Array.GET_EXP_LIST.contains(expSymbolName)) {
			return genArrayGetExp(code, expNode, nestLevel, option);
		}
		else if (SymbolNames.Array.LENGTH_EXP_LIST.contains(expSymbolName)) {
			return genArrayLenExp(code, expNode, nestLevel, option);
		}
		return null;
	}

	/**
	 * 二項演算式を作成する
	 * @param code 途中式の格納先
	 * @param binaryExpNode 二項式のノード
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @return 式もしくは式の評価結果を格納した変数
	 */
	private String genBinaryExp(
		StringBuilder code,
		SyntaxSymbol binaryExpNode,
		int nestLevel,
		CompileOption option) {

		SyntaxSymbol leftExp = binaryExpNode.findSymbolInDescendants("*",	SymbolNames.BinaryExp.LEFT_EXP, "*");
		String leftExpCode = genExpression(code, leftExp, nestLevel, option);
		SyntaxSymbol rightExp = binaryExpNode.findSymbolInDescendants("*", SymbolNames.BinaryExp.RIGHT_EXP, "*");
		String rightExpCode = genExpression(code, rightExp, nestLevel, option);
		String operatorCode = null;
		if (binaryExpNode.getSymbolName().equals(SymbolNames.BinaryExp.MOD_EXP)) {
			operatorCode = " % ";
		}
		else {
			TextNode operator = (TextNode)binaryExpNode.findSymbolInDescendants("*", SymbolNames.BinaryExp.OPERATOR, "*");
			operatorCode = SymbolNames.BinaryExp.OPERATOR_MAP.get(operator.getText());
		}

		if (leftExp == null || rightExp == null)
			return null;

		String tmpVar = common.genVarName(binaryExpNode);
		code.append(common.indent(nestLevel))
			.append(BhCompiler.Keywords.JS._const)
			.append(tmpVar)
			.append(" = ")
			.append(leftExpCode)
			.append(operatorCode)
			.append(rightExpCode)
			.append(";")
			.append(Util.LF);

		String tmpVarResult = tmpVar;
		String funcName = SymbolNames.PreDefFunc.PREDEF_FUNC_NAME_MAP.get(Arrays.asList(SymbolNames.PreDefFunc.IS_FINITE));
		if (option.handleException) {
			if (SymbolNames.BinaryExp.ARITH_EXCEPTION_EXP.contains(binaryExpNode.getSymbolName())) {
				tmpVarResult = "_" + tmpVar;
				code.append(common.indent(nestLevel))
					.append(BhCompiler.Keywords.JS._const)
					.append(tmpVarResult)
					.append(" = ")
					.append("(")
					.append(common.genFuncCallCode(funcName, tmpVar))
					.append(") ? ")
					.append(tmpVar)
					.append(" : ")
					.append(leftExpCode)
					.append(";")
					.append(Util.LF);
			}
		}
		return tmpVarResult;
	}

	/**
	 * 単項演算式を作成する
	 * @param code 途中式の格納先
	 * @param unaryExpNode 単項式のノード
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @return 式もしくは式の評価結果を格納した変数
	 */
	private String genUnaryExp(
		StringBuilder code,
		SyntaxSymbol unaryExpNode,
		int nestLevel,
		CompileOption option) {

		SyntaxSymbol primaryExp = unaryExpNode.findSymbolInDescendants("*",	SymbolNames.UnaryExp.PRIMARY_EXP, "*");
		String primaryExpCode = genExpression(code, primaryExp, nestLevel, option);
		String operatorCode = SymbolNames.UnaryExp.OPERATOR_MAP.get(unaryExpNode.getSymbolName());

		if (primaryExp == null)
			return null;

		String tmpVar = common.genVarName(unaryExpNode);
		code.append(common.indent(nestLevel))
			.append(BhCompiler.Keywords.JS._const)
			.append(tmpVar)
			.append(" = ")
			.append(operatorCode)
			.append(primaryExpCode)
			.append(";")
			.append(Util.LF);

		return tmpVar;
	}

	/**
	 * リテラルを作成する
	 * @param literalNode リテラルのノード
	 * @return リテラルのソースコード
	 */
	private String genLiteral(SyntaxSymbol literalNode) {

		if (SymbolNames.Literal.LIST_TYPES.contains(literalNode.getSymbolName()))
			return "([])";	//空リスト

		String inputText = "";
		if (literalNode instanceof TextNode)
			inputText = ((TextNode)literalNode).getText();

		switch (literalNode.getSymbolName()) {
			case SymbolNames.Literal.NUM_LITERAL:
				return "(" + inputText + ")";

			case SymbolNames.Literal.STR_LITERAL:
				return "('" + inputText.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'") + "')";

			case SymbolNames.Literal.LINE_FEED:
				return "('\\n')";

			case SymbolNames.Literal.BOOL_LITERAL:
				return "(" + inputText + ")";

			default:
				throw new AssertionError("invalid literal " + literalNode.getSymbolName());
		}
	}

	/**
	 * 定義済み関数の呼び出し式を作成する
	 * @param code 関数呼び出し式の格納先
	 * @param funcCallNode 関数呼び出し式のノード
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @param storeRetVal 戻り値を変数に格納するコードを出力する場合true.
	 * @return 式もしくは式の評価結果を格納した変数. storeRetValがfalseの場合はnull.
	 */
	public String genPreDefFuncCallExp(
		StringBuilder code,
		SyntaxSymbol funcCallNode,
		int nestLevel,
		CompileOption option,
		boolean storeRetVal) {

		List<String> argList = new ArrayList<>();
		List<String> outArgList = new ArrayList<>();
		genPreDefFuncArgs(code, funcCallNode, argList, false, nestLevel, option);
		genPreDefFuncArgs(code, funcCallNode, outArgList, true, nestLevel, option);
		argList.addAll(outArgList);
		List<String> funcIdentifier = buildFuncIdentifier(funcCallNode);

		String retValName = null;
		code.append(common.indent(nestLevel));
		if (storeRetVal) {
			retValName = common.genVarName(funcCallNode);
			code.append(BhCompiler.Keywords.JS._const)
				.append(retValName)
				.append(" = ");
		}

		String funcName = SymbolNames.PreDefFunc.PREDEF_FUNC_NAME_MAP.get(funcIdentifier);
		String[] argArray =  argList.toArray(new String[argList.size()]);
		String funcCallCode = common.genFuncCallCode(funcName, argArray);
		code.append(funcCallCode)
			.append(";")
			.append(Util.LF);
		genOutArgCopy(code, outArgList, nestLevel);
		return retValName;
	}

	/**
	 * 定義済み関数の実引数部分を作成する
	 * @param code 関数呼び出し式の格納先
	 * @param funcCallNode 関数呼び出し式のノード
	 * @param argList 引数の格納先
	 * @param outArgList 出力引数の格納先
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	private void genPreDefFuncArgs(
		StringBuilder code,
		SyntaxSymbol funcCallNode,
		List<String> argList,
		boolean outArg,
		int nestLevel,
		CompileOption option) {

		int idArg = 0;
		while (true) {
			String argCnctrName = (outArg ? SymbolNames.PreDefFunc.OUT_ARG : SymbolNames.PreDefFunc.ARG) + idArg;
			SyntaxSymbol argExp = funcCallNode.findSymbolInDescendants("*", argCnctrName, "*");
			if (argExp == null)
				break;

			if (outArg)
				argList.add(genOutArg(code, argExp, nestLevel, option));
			else
				argList.add(genExpression(code, argExp, nestLevel, option));
			++idArg;
		}
	}

	/**
	 * 関数特定用のリストを作成する
	 * @return 関数特定用のリスト
	 */
	private List<String> buildFuncIdentifier(SyntaxSymbol funcCallNode) {
		//呼び出しオプションを探す
		int idOption = 0;
		List<String> funcIdentifier = new ArrayList<>(Arrays.asList(funcCallNode.getSymbolName()));
		while (true) {
			String optionCnctrName = SymbolNames.PreDefFunc.OPTION + idOption;
			SyntaxSymbol optionExp = funcCallNode.findSymbolInDescendants("*", optionCnctrName, "*");
			if (optionExp == null)
				break;
			if (optionExp instanceof TextNode) {
				String callOption = ((TextNode)optionExp).getText();
				funcIdentifier.add(callOption);
			}
			++idOption;
		}
		return funcIdentifier;
	}

	/**
	 * ユーザー定義関数の呼び出し式を生成する
	 * @param code 生成したコードの格納先
	 * @param funcCallNode 関数呼び出しのノード
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @param storeRetVal 戻り値を変数に格納するコードを出力する場合true.
	 * @return 式もしくは式の評価結果を格納した変数. storeRetValがfalseの場合はnull.
	 */
	public String genUserDefFuncCallExp(
		StringBuilder code,
		SyntaxSymbol funcCallNode,
		int nestLevel,
		CompileOption option,
		boolean storeRetVal) {

		String funcName = common.genFuncName(((Imitatable)funcCallNode).getOriginalNode());
		SyntaxSymbol arg = funcCallNode.findSymbolInDescendants("*", SymbolNames.UserDefFunc.ARG, "*");
		SyntaxSymbol outArg = funcCallNode.findSymbolInDescendants("*", SymbolNames.UserDefFunc.OUT_ARG, "*");
		String argArray[];
		List<String> argList = new ArrayList<>();
		List<String> outArgList = new ArrayList<>();

		if (!arg.getSymbolName().equals(SymbolNames.UserDefFunc.ARG_VOID))
			genArgList(code, arg, argList, false, nestLevel, option);

		if (!outArg.getSymbolName().equals(SymbolNames.UserDefFunc.ARG_VOID))
			genArgList(code, outArg, outArgList, true, nestLevel, option);

		argList.addAll(outArgList);
		argArray = argList.toArray(new String[argList.size()]);
		String funcCallCode = common.genFuncCallCode(funcName, argArray);
		String retValName = null;
		code.append(common.indent(nestLevel));
		if (storeRetVal) {
			retValName = common.genVarName(funcCallNode);
			code.append(BhCompiler.Keywords.JS._const)
				.append(retValName)
				.append(" = ");
		}
		code.append(funcCallCode)
			.append(";")
			.append(Util.LF);
		genOutArgCopy(code, outArgList, nestLevel);

		return retValName;
	}

	/**
	 * 出力変数をコピーするコードを作成する
	 * @param code 生成したコードの格納先
	 * @param outArgList 出力変数名
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	private void genOutArgCopy(
		StringBuilder code,
		List<String> outArgList,
		int nestLevel) {

		for (int argIdx = 0; argIdx < outArgList.size(); ++argIdx) {
			code.append(common.indent(nestLevel))
				.append(outArgList.get(argIdx))
				.append(" = ")
				.append(SymbolNames.PreDefVars.OUT_ARGS).append("[").append(argIdx).append("];\n");
		}
	}

	/**
	 * ユーザ定義関数の引数リストを作成する
	 * @param code 生成したコードの格納先
	 * @param argNode 引数ノード
	 * @param argList 引数の格納先
	 * @param outArg 出力引数だった場合true
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 */
	private void genArgList(
		StringBuilder code,
		SyntaxSymbol argNode,
		List<String> argList,
		boolean outArg,
		int nestLevel,
		CompileOption option) {

		SyntaxSymbol argument = argNode.findSymbolInDescendants("*", SymbolNames.UserDefFunc.ARG, "*");
		if (outArg)
			argList.add(genOutArg(code, argument, nestLevel, option));
		else
			argList.add(genExpression(code, argument, nestLevel, option));

		SyntaxSymbol nextArg = argNode.findSymbolInDescendants("*", SymbolNames.UserDefFunc.NEXT_ARG, "*");
		if (!nextArg.getSymbolName().equals(SymbolNames.UserDefFunc.ARG_VOID))
			genArgList(code, nextArg, argList, outArg, nestLevel, option);
	}

	/**
	 * 配列の要素参照式を作成する
	 * @param code 生成したコードの格納先
	 * @param arrayGetNode 配列参照のノード
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @return 式もしくは式の評価結果を格納した変数.
	 */
	private String genArrayGetExp(
		StringBuilder code,
		SyntaxSymbol arrayGetNode,
		int nestLevel,
		CompileOption option) {

		SyntaxSymbol arrayExp = arrayGetNode.findSymbolInDescendants("*", SymbolNames.Array.ARRAY, "*");
		String arrayExpCode = genExpression(code, arrayExp, nestLevel, option);
		SyntaxSymbol indexExp = arrayGetNode.findSymbolInDescendants("*", SymbolNames.Array.INDEX, "*");
		String indexExpCode;
		if (indexExp == null) {	//最後の要素取得の場合はindexExp == null
			indexExpCode = "(" + arrayExpCode + ".length - 1)";
		}
		else {
			indexExpCode = genExpression(code, indexExp, nestLevel, option);
		}
		String literalWhenUndef = SymbolNames.Undefined.SUBSTITUTE_LITERAL_MAP.get(arrayGetNode.getSymbolName());	//配列取得結果がundefinedになる場合の代替値
		String tmpVar = common.genVarName(arrayGetNode);

		String funcName = SymbolNames.PreDefFunc.PREDEF_FUNC_NAME_MAP.get(Arrays.asList(arrayGetNode.getSymbolName()));
		String[] argArray = {arrayExpCode, indexExpCode, literalWhenUndef};
		String funcCallCode = common.genFuncCallCode(funcName, argArray);

		code.append(common.indent(nestLevel))
			.append(BhCompiler.Keywords.JS._const)
			.append(tmpVar)
			.append(" = ")
			.append(funcCallCode)
			.append(";")
			.append(Util.LF);
		return tmpVar;
	}

	/**
	 * 配列の長さを取得するコードを作成する
	 * @param code 生成したコードの格納先
	 * @param arrayLenNode 配列長取得ノード
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @return 式もしくは式の評価結果を格納した変数.
	 */
	private String genArrayLenExp(
		StringBuilder code,
		SyntaxSymbol arrayLenNode,
		int nestLevel,
		CompileOption option) {

		SyntaxSymbol arayExp = arrayLenNode.findSymbolInDescendants("*", SymbolNames.Array.ARRAY, "*");
		String arrayExpCode = genExpression(code, arayExp, nestLevel, option);

		String tmpVar = common.genVarName(arrayLenNode);
		code.append(common.indent(nestLevel))
			.append(BhCompiler.Keywords.JS._const)
			.append(tmpVar)
			.append(" = ")
			.append(arrayExpCode)
			.append(".length;")
			.append(Util.LF);
		return tmpVar;
	}

	/**
	 * 出力変数を作成する
	 * @param code 生成したコードの格納先
	 * @param varNode 変数ノード
	 * @param nestLevel ソースコードのネストレベル
	 * @param option コンパイルオプション
	 * @return 出力変数
	 */
	private String genOutArg(
		StringBuilder code,
		SyntaxSymbol varNode,
		int nestLevel,
		CompileOption option) {

		if (SymbolNames.VarDecl.VAR_LIST.contains(varNode.getSymbolName())) {
			return genExpression(code, varNode, nestLevel, option);
		}
		else if (SymbolNames.VarDecl.VAR_VOID_LIST.contains(varNode.getSymbolName())){	//出力引数に変数指定がなかった場合
			String varName = common.genVarName(varNode);
			code.append(common.indent(nestLevel))
				.append(BhCompiler.Keywords.JS._let)
				.append(varName)
				.append(" = ")
				.append(SymbolNames.VarDecl.INIT_VAL_MAP.get(varNode.getSymbolName()))
				.append(";\n");
			return varName;
		}
		else {
			assert false;
		}
		return "";
	}
}
