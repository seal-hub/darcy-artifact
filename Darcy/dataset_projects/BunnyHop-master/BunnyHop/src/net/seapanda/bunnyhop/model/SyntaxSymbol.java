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
package net.seapanda.bunnyhop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.seapanda.bunnyhop.common.Showable;
import net.seapanda.bunnyhop.modelprocessor.BhModelProcessor;

/**
 * 終端記号, 非終端記号を表すクラス
 * @author K.Koike
 * */
public abstract class SyntaxSymbol implements Showable, Serializable {

	private String symbolName;	//!< 終端, 非終端記号名
	private String symbolID;	//!< コンパイル対象のSyntaxSymbolオブジェクトが持つユニークなID

	/**
	 * 引数で指定したシンボル名を持つSyntaxSymbolをhierarchyLevel(もしくはそれ以下)の階層のSyntaxSymbolから探す.<br>
	 * @param hierarychyLevel 自分から見てこのレベルの階層もしくはそれ以下を探す.  例(0:自分(もしくはそれ以下)を探す. 1:子(もしくはそれ以下)を探す)
	 * @param downToBottom hierarychyLevel で指定した階層のみ探す場合false. ボトムノードまで探す場合true
	 * @param foundSymbolList 見つかったSyntaxSymbolを格納するリスト
	 * @param symbolNames シンボル名
	 */
	public abstract void findSymbolInDescendants(
		int hierarychyLevel,
		boolean downToBottom,
		List<SyntaxSymbol> foundSymbolList,
		String... symbolNames);

	/**
	 * 引数で指定したシンボル名を持つSyntaxSymbolをhierarychyLevel(もしくはそれ以上)の階層のSyntaxSymbolから探す.<br>
	 * 見つからなかった場合はnull が返る.
	 * @param symbolName シンボル名
	 * @param hierarchyLevel 自分から見てこのレベルの階層もしくはそれ以上を探す.  例(0:自分(もしくはそれ以上)を探す. 1:親(もしくはそれ以上)を探す)
	 * @param upToTop hierarchyLevel で指定した階層のみ探す場合false. トップノードまで探す場合true.
	 * @return シンボル名を持つ SyntaxSymbol オブジェクト
	 */
	public abstract SyntaxSymbol findSymbolInAncestors(String symbolName, int hierarchyLevel, boolean upToTop);

	/**
	 * 引数で指定したシンボル名を持つSyntaxSymbolを子以下のSyntaxSymbolから探す.<br>
	 * 見つからなかった場合はnull が返る.
	 * @param symbolNamePath 子孫ノードのパスに, この名前のリストのとおりに繋がっているパスがある場合, リストの最後の名前のノードを返す.<br>
	 *						  symbolName[0] == childName, symbolName[1] == grandsonName
	 * @return 最後のシンボル名を持つ SyntaxSymbol オブジェクト
	 */
	public SyntaxSymbol findSymbolInDescendants(String... symbolNamePath) {

		assert symbolNamePath.length != 0;

		List<SyntaxSymbol> foundSymbolList = new ArrayList<>();
		findSymbolInDescendants(symbolNamePath.length, false, foundSymbolList, symbolNamePath[symbolNamePath.length - 1]);
		String[] reverseSymbolNamePath = new String[symbolNamePath.length];	//symbolNamePath == (a,b,c)  => reverseSymbolNamePath == (b,a,thisSymbolName)
		for (int i = symbolNamePath.length - 2, j = 0; i >= 0; --i, ++j) {
			reverseSymbolNamePath[j] = symbolNamePath[i];
		}
		reverseSymbolNamePath[reverseSymbolNamePath.length - 1] = symbolName;
		for (SyntaxSymbol foundSymbol : foundSymbolList) {
			if (foundSymbol.findSymbolInAncestors(reverseSymbolNamePath) == this) {
				return foundSymbol;
			}
		}
		return null;
	}

	/**
	 * 引数で指定したシンボル名を持つSyntaxSymbolを親以上のSyntaxSymbolから探す.<br>
	 * 見つからなかった場合はnull が返る.
	 * @param symbolNamePath 先祖ノードがこの名前のリストのとおりにつながっているとき, リストの最後の名前のノードを返す.<br>
	 *						  symbolName[0] == parentName, symbolName[1] == grandParentName
	 * @return 最後のシンボル名を持つ SyntaxSymbol オブジェクト
	 */
	public SyntaxSymbol findSymbolInAncestors(String... symbolNamePath) {

		assert symbolNamePath.length != 0;

		int idx = 0;
		SyntaxSymbol currentLevel = this;

		while (idx < symbolNamePath.length){
			String childName = symbolNamePath[idx];
			++idx;
			currentLevel = currentLevel.findSymbolInAncestors(childName, 1, false);
			if (currentLevel == null) {
				break;
			}
		}

		return currentLevel;
	}

	/**
	 * このSyntaxSymbolが引数で指定したSyntaxSymbol の子孫ノードであった場合trueを返す
	 * @param ancestor このSyntaxSymbolが ancestor の子孫ノードであった場合 true
	 * @return このSyntaxSymbolがancestor 以下のノードであった場合true
	 * */
	public abstract boolean isDescendantOf(SyntaxSymbol ancestor);

	/**
	 * コンストラクタ
	 * @param symbolName
	 * */
	protected SyntaxSymbol(String symbolName) {
		this.symbolName = symbolName;
	}

	/**
	 * コピーコンストラクタ
	 * @param org コピー元オブジェクト
	 */
	protected SyntaxSymbol(SyntaxSymbol org) {
		symbolName = org.symbolName;
	}

	/**
	 * 終端, 非終端記号名を取得する
	 * @return 終端, 非終端記号名
	 * */
	public String getSymbolName() {
		return symbolName;
	}

	/**
	 * シンボルIDをセットする
	 * @param id シンボルID
	 */
	public void setSymbolID(String id) {
		this.symbolID = id;
	}

	/**
	 * シンボルIDをセットする
	 * @return シンボルID
	 */
	public String getSymbolID() {
		return symbolID;
	}

	/**
	 * visitor に次の走査対象に渡す
	 * @param visitor 走査対象を渡すvisitor
	 * */
	public abstract void accept(BhModelProcessor visitor);
}



