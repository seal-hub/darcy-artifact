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

import java.util.List;
import java.util.Map;

import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.BhNodeID;
import net.seapanda.bunnyhop.model.SyntaxSymbol;
import net.seapanda.bunnyhop.model.imitation.Imitatable;
import net.seapanda.bunnyhop.model.imitation.ImitationID;
import net.seapanda.bunnyhop.model.imitation.ImitationInfo;
import net.seapanda.bunnyhop.model.templates.BhNodeTemplates;
import net.seapanda.bunnyhop.modelprocessor.BhModelProcessor;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * 子ノードと接続されるノード
 * @author K.Koike
 * */
public class ConnectiveNode extends Imitatable {

	private Section childSection;							//!< セクションの集合 (ノード内部に描画されるもの)
	private ImitationInfo<ConnectiveNode> imitInfo;	//!< イミテーションノードに関連する情報がまとめられたオブジェクト

	/**
	 * コンストラクタ<br>
	 * ノード内部・外部とは描画位置のこと
	 * @param id ノードID (\<Node\> タグの bhID)
	 * @param name ノード名 (\<Node\> タグの name)
	 * @param childSection 子セクション
	 * @param scopeName イミテーションノードがオリジナルノードと同じスコープにいるかチェックする際の名前
	 * @param scriptNameOnMovedFromChildToWS ワークスペース移動時に実行されるスクリプトの名前
	 * @param scriptNameOnMovedToChild 子ノードとして接続されたときに実行されるスクリプトの名前
	 * @param imitID_imitNodeID イミテーションタグとそれに対応するイミテーションノードIDのマップ
	 * @param canCreateImitManually このノードがイミテーション作成機能を持つ場合true
	 * */
	public ConnectiveNode(
			BhNodeID id,
			String name,
			Section childSection,
			String scopeName,
			String scriptNameOnMovedFromChildToWS,
			String scriptNameOnMovedToChild,
			Map<ImitationID, BhNodeID> imitID_imitNodeID,
			boolean canCreateImitManually) {
		super(id,
			name,
			BhParams.BhModelDef.ATTR_VALUE_CONNECTIVE,
			scriptNameOnMovedFromChildToWS,
			scriptNameOnMovedToChild);
		this.childSection = childSection;
		imitInfo = new ImitationInfo<>(imitID_imitNodeID, canCreateImitManually, scopeName);
	}

	/**
	 * コピーコンストラクタ
	 * @param org コピー元オブジェクト
	 */
	private ConnectiveNode(ConnectiveNode org) {
		super(org);
	}

	@Override
	public ConnectiveNode copy(UserOperationCommand userOpeCmd) {

		ConnectiveNode newNode = new ConnectiveNode(this);
		newNode.childSection = childSection.copy(userOpeCmd);
		newNode.childSection.setParent(newNode);
		newNode.imitInfo = new ImitationInfo<>(imitInfo, userOpeCmd, newNode);
		return newNode;
	}

	/**
	 * BhModelProcessor に自オブジェクトを渡す
	 * @param processor 自オブジェクトを渡す BhModelProcessorオブジェクト
	 * */
	@Override
	public void accept(BhModelProcessor processor) {
		processor.visit(this);
	}

	/**
	 * BhModelProcessor を子Section に渡す
	 * @param processor 子Section に渡す BhModelProcessor
	 * */
	public void introduceSectionsTo(BhModelProcessor processor) {
		childSection.accept(processor);
	}


	@Override
	public ConnectiveNode createImitNode(UserOperationCommand userOpeCmd, ImitationID imitID) {

		//イミテーションノード作成
		BhNode imitationNode = BhNodeTemplates.INSTANCE.genBhNode(imitInfo.getImitationNodeID(imitID), userOpeCmd);

		//オリジナルとイミテーションの関連付け
		ConnectiveNode connectiveImit = (ConnectiveNode)imitationNode; //ノードテンプレート作成時に整合性チェックしているのでキャストに問題はない
		imitInfo.addImitation(connectiveImit, userOpeCmd);
		connectiveImit.imitInfo.setOriginal(this, userOpeCmd);
		return connectiveImit;
	}

	@Override
	public ImitationInfo<ConnectiveNode> getImitationInfo() {
		return imitInfo;
	}

	@Override
	public ConnectiveNode getOriginalNode() {
		return imitInfo.getOriginal();
	}

	@Override
	public BhNode findOuterEndNode() {
		BhNode outerEnd = childSection.findOuterEndNode();
		if (outerEnd == null)
			return this;

		return outerEnd;
	}

	/**
	 * モデルの構造を表示する
	 * @param depth 表示インデント数
	 * */
	@Override
	public void show(int depth) {

		String parentHashCode = "null";
		if (parentConnector != null)
			parentHashCode = parentConnector.hashCode() + "";

		String lastReplacedHash = "";
		if (getLastReplaced() != null)
			lastReplacedHash =  getLastReplaced().hashCode() + "";

		MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<ConnectiveNode" + "  bhID=" + getID()  + "  parent=" + parentHashCode + "  > " + this.hashCode());
		MsgPrinter.INSTANCE.msgForDebug(indent(depth+1) + "<" + "last replaced " + lastReplacedHash + "> ");
		MsgPrinter.INSTANCE.msgForDebug(indent(depth+1) + "<" + "scopeName " + imitInfo.scopeName + "> ");
		MsgPrinter.INSTANCE.msgForDebug(indent(depth+1) + "<" + "imitation" + "> ");
		imitInfo.getImitationList().forEach(imit -> {
			MsgPrinter.INSTANCE.msgForDebug(indent(depth+2) + "imit " + imit.hashCode());
		});
		childSection.show(depth + 1);
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

		childSection.findSymbolInDescendants(Math.max(0, hierarchyLevel-1), toBottom, foundSymbolList, symbolNames);
	}
}






















