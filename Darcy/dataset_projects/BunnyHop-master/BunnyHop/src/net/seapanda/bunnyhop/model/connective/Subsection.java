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
package net.seapanda.bunnyhop.model.connective;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.seapanda.bunnyhop.modelprocessor.BhModelProcessor;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.SyntaxSymbol;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * サブグループとして Section の集合を持つクラス
 * @author K.Koike
 * */
public class Subsection extends Section {

	List<Section> subsectionList = new ArrayList<>();	//!< サブグループリスト

	/**
	 * コンストラクタ
	 * @param symbolName     終端, 非終端記号名
	 * @param subsectionList サブセクションリスト
	 * */
	public Subsection(String symbolName, Collection<Section> subsectionList) {
		super(symbolName);
		this.subsectionList.addAll(subsectionList);
	}
	
	/**
	 * コピーコンストラクタ
	 * @param org コピー元オブジェクト
	 */
	private Subsection(Subsection org) {
		super(org);
	}

	@Override
	public Subsection copy(	UserOperationCommand userOpeCmd) {
		
		Subsection newSubsection = new Subsection(this);
		subsectionList.forEach(section -> {
			Section newSection = section.copy(userOpeCmd);
			newSection.setParent(newSubsection);
			newSubsection.subsectionList.add(newSection);
		});
		return newSubsection;
	}
	
	/**
	 * visitor に自オブジェクトを渡す
	 * @param visitor 自オブジェクトを渡す visitorオブジェクト
	 * */
	@Override
	public void accept(BhModelProcessor visitor) {
		visitor.visit(this);
	}

	/**
	 * visitor をこのセクションの下のサブセクションに渡す
	 * @param visitor サブグループに渡す visitor
	 * */
	public void introduceSubsectionTo(BhModelProcessor visitor) {
		subsectionList.forEach(subsection -> subsection.accept(visitor));
	}
	
	@Override
	public void findSymbolInDescendants(int hierarchyLevel, boolean toBottom, List<SyntaxSymbol> foundSymbolList, String... symbolNames) {
		
		if (hierarchyLevel == 0) {
			for (String symbolName : symbolNames) {
				if (Util.equals(getSymbolName(), symbolName)) {
					foundSymbolList.add(this);
				}
			}
			if (!toBottom) {
				return;
			}
		}
		
		int childLevel = hierarchyLevel - 1;
		for (Section subsection : subsectionList) {
			subsection.findSymbolInDescendants(Math.max(0, childLevel), toBottom, foundSymbolList, symbolNames);
		}
	}

	@Override
	public BhNode findOuterEndNode() {
		
		for (int i = subsectionList.size() - 1; i >= 0; --i) {
			BhNode outerEnd = subsectionList.get(i).findOuterEndNode();
			if (outerEnd != null)
				return outerEnd;
		}
		return null;
	}
		
	/**
	 * モデルの構造を表示する
	 * @param depth 表示インデント数
	 * */
	@Override
	public void show(int depth) {

		int parentHash;
		if (parentNode != null)
			parentHash = parentNode.hashCode();
		else
			parentHash = parentSection.hashCode();

		MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<ConnectorGroup" + " name=" + getSymbolName() + "  parent=" + parentHash + "  > " + this.hashCode());
		subsectionList.forEach((connector -> connector.show(depth + 1)));
	}
}






















