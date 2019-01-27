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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.seapanda.bunnyhop.modelprocessor.BhModelProcessor;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.SyntaxSymbol;
import net.seapanda.bunnyhop.model.imitation.ImitationConnectionPos;
import net.seapanda.bunnyhop.model.imitation.ImitationID;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * コネクタ集合を持つグループ
 * @author K.Koike
 * */
public class ConnectorSection extends Section {

	private final List<Connector> cnctrList; //!< コネクタリスト
	private final List<ConnectorSection.CnctrInstantiationParams> cnctrInstantiationParamsList;	//!< コネクタ生成時のパラメータ

	/**
	 * コンストラクタ
	 * @param symbolName  終端, 非終端記号名
	 * @param cnctrList 保持するコネクタのリスト
	 * @param cnctrInstantiationParamsList コネクタ生成時のパラメータ群のリスト
	 * */
	public ConnectorSection (
		String symbolName,
		List<Connector> cnctrList,
		List<ConnectorSection.CnctrInstantiationParams> cnctrInstantiationParamsList) {
		super(symbolName);
		this.cnctrList = cnctrList;
		this.cnctrInstantiationParamsList = cnctrInstantiationParamsList;
	}

	/**
	 * コピーコンストラクタ
	 * @param org コピー元オブジェクト
	 * @param parentNode このセクションを保持する ConnectiveNode オブジェクト
	 * @param parentSection このセクションを保持している Subsection オブジェクト

	 */
	private ConnectorSection(ConnectorSection org) {
		super(org);
		cnctrList = new ArrayList<>();
		cnctrInstantiationParamsList = org.cnctrInstantiationParamsList;
	}
	
	@Override
	public ConnectorSection copy(UserOperationCommand userOpeCmd) {
		
		ConnectorSection newSection = new ConnectorSection(this);
		for (int i = 0; i < cnctrList.size(); ++i) {
			CnctrInstantiationParams cnctrInstParams = cnctrInstantiationParamsList.get(i);
			Connector newConnector = 
				cnctrList.get(i).copy(userOpeCmd,
					cnctrInstParams.cnctrName,
					cnctrInstParams.imitationID,
					cnctrInstParams.imitCnctPoint,
					newSection);
			newSection.cnctrList.add(newConnector);
		}
		return newSection;
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
	 * visitor をコネクタに渡す
	 * @param visitor コネクタに渡す visitor
	 * */
	public void introduceConnectorsTo(BhModelProcessor visitor) {
		cnctrList.forEach(connector -> connector.accept(visitor));
	}
		
	/**
	 * コネクタのリストを返す
	 * @return コネクタのリスト
	 */
	public List<Connector> getConnectorList() {
		return cnctrList;
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
		
		int childLevel = hierarchyLevel-1;
		for (Connector cnctr : cnctrList) {
			cnctr.findSymbolInDescendants(Math.max(0, childLevel), toBottom, foundSymbolList, symbolNames);
		}
	}
	
	@Override
	public BhNode findOuterEndNode() {
		
		for (int i = cnctrList.size() - 1; i >= 0; --i) {
			if (cnctrList.get(i).isOuter()) {
				return cnctrList.get(i).getConnectedNode().findOuterEndNode();
			}
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

		MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<ConnectorGroup  " + "name=" + getSymbolName() + "  parenNode=" + parentHash  + "  > " + this.hashCode());
		cnctrList.forEach((connector -> connector.show(depth + 1)));
	}
	
	/**
	 * コネクタ生成時のパラメータ
	 */
	public static class CnctrInstantiationParams implements Serializable{

		public final String cnctrName;	//!< コネクタ名
		public final ImitationID imitationID;	//!< 作成するイミテーションの識別子
		public final ImitationConnectionPos imitCnctPoint;	//!< イミテーション接続位置

		/**
		 * @param cnctrName コネクタ名
		 * @param imitationID イミテーションID (作成するイミテーションの識別子)
		 * @param imitCnctPoint イミテーション接続位置の識別子
		 */
		public CnctrInstantiationParams(
			String cnctrName, 
			ImitationID imitationID,
			ImitationConnectionPos imitCnctPoint) {
			this.cnctrName = cnctrName;
			this.imitationID = imitationID;
			this.imitCnctPoint = imitCnctPoint;
		}
	}
}




