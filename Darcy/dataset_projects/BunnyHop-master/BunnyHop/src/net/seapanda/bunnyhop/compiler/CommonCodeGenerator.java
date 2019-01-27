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

import net.seapanda.bunnyhop.model.SyntaxSymbol;

/**
 * @author K.Koike
 * 式や文のコード生成に必要な共通の機能を持つクラス
 */
public class CommonCodeGenerator {
	
	/**
	 * 変数定義から変数名を生成する
	 * @param varDecl 変数定義ノード
	 * @return 変数名
	 */
	public String genVarName(SyntaxSymbol varDecl) {
		return BhCompiler.Keywords.varPrefix + varDecl.getSymbolID();
	}
	
	/**
	 * 関数定義から関数名を生成する
	 * @param funcDef
	 * @return 関数名
	 */
	public String genFuncName(SyntaxSymbol funcDef) {
		return BhCompiler.Keywords.funcPrefix + funcDef.getSymbolID();
	}
		
	/**
	 * 関数呼び出しのコードを作成する
	 * @param funcName 関数名
	 * @param argNames 引数名のリスト
	 * @return 関数呼び出しのコード
	 */
	public String genFuncCallCode(String funcName, String... argNames) {
		
		StringBuilder code = new StringBuilder();
		code.append(funcName)
			.append("(");
		for (int i  = 0; i < argNames.length - 1; ++i) {
			code.append(argNames[i]).append(",");
		}
		
		if (argNames.length == 0) {
			code.append(")");
		}
		else {
			code.append(argNames[argNames.length-1])
				.append(")");
		}		
		return code.toString();
	}
	
	public String indent(int depth) {
		
		switch(depth) {
			case 0: return "";
			case 1: return "	";
			case 2: return "		";
			case 3: return "			";
			case 4: return "				";
			case 5: return "					";
			case 6: return "						";
			case 7: return "							";
			case 8: return "								";
			case 9: return "									";
			case 10: return "										";
			case 11: return "											";
			case 12: return "												";
			default:{
				StringBuilder ret = new StringBuilder("");
				for (int i = 0; i < depth; ++i)
					ret.append("	");
				return ret.toString();
			}
		}
	}
}
