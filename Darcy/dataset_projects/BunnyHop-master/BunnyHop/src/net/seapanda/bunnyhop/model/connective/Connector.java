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
import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import net.seapanda.bunnyhop.modelprocessor.BhModelProcessor;
import net.seapanda.bunnyhop.modelprocessor.NodeMVCBuilder;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.message.MsgTransporter;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.templates.BhNodeTemplates;
import net.seapanda.bunnyhop.model.SyntaxSymbol;
import net.seapanda.bunnyhop.modelhandler.BhNodeHandler;
import net.seapanda.bunnyhop.configfilereader.BhScriptManager;
import net.seapanda.bunnyhop.model.BhNodeID;
import net.seapanda.bunnyhop.model.imitation.ImitationConnectionPos;
import net.seapanda.bunnyhop.model.imitation.ImitationID;
import net.seapanda.bunnyhop.modelprocessor.TextImitationPrompter;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * ノードとノードをつなぐ部分のクラス
 * @author K.Koike
 * */
public class Connector extends SyntaxSymbol {

	private final ConnectorID id; 				//!< コネクタID (\<Connector\> タグの bhID)
	public final BhNodeID defaultNodeID; 		//!< ノードが取り外されたときに変わりに繋がるノードのID (\<Connector\> タグの bhID)
	public final BhNodeID initNodeID;			//!< 最初に接続されているノードのID
	private BhNode connectedNode;			//!< 接続中のノード. null となるのは、テンプレート構築中とClone メソッドの一瞬のみ
	private ConnectorSection parent;	//!< このオブジェクトを保持する ConnectorSection オブジェクト
	private final boolean fixed;	//!< このコネクタにつながるBhNodeが手動で取り外しや入れ替えができない場合true
	private boolean outer = false;	//!< 外部描画ノードを接続するコネクタの場合true
	private ImitationID imitID;	//!< イミテーション生成時のID
	private ImitationConnectionPos imitCnctPoint;	//!< イミテーション生成時のタグ
	private final String scriptNameOnReplaceabilityChecked;	//!< ノードを入れ替え可能かどうかチェックするスクリプトの名前
	transient protected Bindings scriptScope;

	/**
	 * visitor に次の走査対象に渡す
	 * @param visitor 走査対象を渡すvisitor
	 * */
	@Override
	 public void accept(BhModelProcessor visitor) {
		visitor.visit(this);
	}

	/**
	 * コンストラクタ
	 * @param id コネクタID (\<Connector\> タグの bhID)
	 * @param defaultNodeID ノードが取り外されたときに変わりに繋がるノードのID
	 * @param initialNodeID 最初に接続されているノードのID
	 * @param fixed このコネクタにつながるノードの入れ替えや取り外しができない場合true
	 * @param scriptNameOnReplaceabilityChecked ノードを入れ替え可能かどうかチェックするスクリプトの名前
	 * */
	public Connector(
		ConnectorID id,
		BhNodeID defaultNodeID,
		BhNodeID initialNodeID,
		boolean fixed,
		String scriptNameOnReplaceabilityChecked) {
		super("");
		this.id = id;
		this.scriptNameOnReplaceabilityChecked = scriptNameOnReplaceabilityChecked;
		this.defaultNodeID = defaultNodeID;
		this.fixed = fixed;
		if (initialNodeID.equals(BhNodeID.NONE))
			this.initNodeID = defaultNodeID;
		else
			this.initNodeID = initialNodeID;
	}
	
	/**
	 * コピーコンストラクタ
	 * @param org コピー元オブジェクト
	 * @param name コネクタ名
	 * @param imitID 作成するイミテーションノードの識別子
	 * @param imitCncrPoint イミテーション接続位置の識別子
	 * @param isOuter 外部描画フラグ
	 * @param parent 親コネクタセクション
	 */
	private Connector(
		Connector org,
		String name,
		ImitationID imitID,
		ImitationConnectionPos imitCnctPoint,
		ConnectorSection parent) {
		
		super(name);
		id = org.id;
		defaultNodeID = org.defaultNodeID;
		initNodeID = org.initNodeID;
		scriptNameOnReplaceabilityChecked = org.scriptNameOnReplaceabilityChecked;
		fixed = org.fixed;
		this.imitID = imitID;
		this.imitCnctPoint = imitCnctPoint;
		this.parent = parent;
	}
	
	/**
	 * このノードのコピーを作成して返す
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * @param name コネクタ名
	 * @param imitID 作成するイミテーションの識別子
	 * @param imitCnctPoint イミテーション接続位置の識別子
	 * @param parent 親コネクタセクション
	 * @return このノードのコピー
	 */
	public Connector copy(
		UserOperationCommand userOpeCmd,
		String name,
		ImitationID imitID,
		ImitationConnectionPos imitCnctPoint,
		ConnectorSection parent) {
		
		Connector newConnector = 
			new Connector(
				this,
				name,
				imitID,
				imitCnctPoint,
				parent);
		BhNode newNode = connectedNode.copy(userOpeCmd);
		newConnector.connectNode(newNode, null);
		return newConnector;
	}

	/**
	 * BhModelProcessor を connectedNode に渡す
	 * @param processor connectedNode に渡す BhModelProcessor
	 * */
	public void introduceConnectedNodeTo(BhModelProcessor processor) {
		connectedNode.accept(processor);
	}

	/**
	 * ノードを接続する
	 * @param node 接続されるノード<br> null はダメ
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * */
	public final void connectNode(BhNode node, UserOperationCommand userOpeCmd) {

		assert node != null;	//null接続はダメ
		
		if (userOpeCmd != null)
			userOpeCmd.pushCmdOfConnectNode(connectedNode, this);

		if(connectedNode != null)
			connectedNode.setParentConnector(null);	//古いノードから親を消す
		connectedNode = node;
		node.setParentConnector(this);	//新しいノードの親をセット
	}
	
	/**
	 * このコネクタの親となるノードを返す
	 * @return このコネクタの親となるノード
	 * */
	public ConnectiveNode getParentNode() {
		return parent.findParentNode();
	}

	/**
	 * このコネクタにつながるノードの入れ替えと取り外しができない場合trueを返す
	 * @return このコネクタにつながるノードの入れ替えと取り外しができない場合true
	 */
	public boolean isFixed() {
		return fixed;
	}
	
	/**
	 * 引数で指定したノードが現在つながっているノードと入れ替え可能かどうか調べる
	 * @param newNode 新しく入れ替わるノード
	 * @return 引数で指定したノードが現在つながっているノードと入れ替え可能である場合, true を返す
	 */
	public boolean isConnectedNodeReplaceableWith(BhNode newNode) {

		if (fixed)
			return false;
		
		CompiledScript onReplaceabilityChecked = BhScriptManager.INSTANCE.getCompiledScript(scriptNameOnReplaceabilityChecked);
		if (onReplaceabilityChecked == null)
			return false;
		
		scriptScope.put(BhParams.JsKeyword.KEY_BH_NEW_NODE_ID, newNode.getID());
		scriptScope.put(BhParams.JsKeyword.KEY_BH_OLD_NODE_ID, connectedNode.getID());
		scriptScope.put(BhParams.JsKeyword.KEY_BH_REPLACED_NEW_NODE, newNode);
		scriptScope.put(BhParams.JsKeyword.KEY_BH_REPLACED_OLD_NODE, connectedNode);
		Object replaceable;
		try {
			replaceable = onReplaceabilityChecked.eval(scriptScope);
		} catch (ScriptException e) {
			MsgPrinter.INSTANCE.errMsgForDebug(Connector.class.getSimpleName() +  ".isReplacable   " + scriptNameOnReplaceabilityChecked + "\n" + e.toString() + "\n");
			return false;
		}
		if (replaceable instanceof Boolean)
			return (Boolean)replaceable;

		return false;
	}
	
	/**
	 * 現在繋がっているノードを取り除く
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * @return 現在繋がっているノードを取り除いた結果, 新しくできたノード
	 * */
	public BhNode remove(UserOperationCommand userOpeCmd) {

		assert connectedNode != null;
		
		BhNode newNode = BhNodeTemplates.INSTANCE.genBhNode(defaultNodeID, userOpeCmd);	//デフォルトノードを作成		
		newNode.accept(new NodeMVCBuilder(NodeMVCBuilder.ControllerType.Default));	//MVC構築
		newNode.accept(new TextImitationPrompter());
		connectedNode.replacedWith(newNode, userOpeCmd);
		return newNode;
	}

	public ConnectorID getID() {
		return id;
	}
	
	/**
	 * このコネクタに接続されているBhNode を返す
	 * @return このコネクタに接続されているBhNode
	 */
	public BhNode getConnectedNode() {
		return connectedNode;
	}
	
	/**
	 * スクリプト実行時のスコープ変数を登録する
	 */
	public final void setScriptScope() {
		scriptScope = BhScriptManager.INSTANCE.createScriptScope();
		scriptScope.put(BhParams.JsKeyword.KEY_BH_THIS, this);
		scriptScope.put(BhParams.JsKeyword.KEY_BH_NODE_HANDLER, BhNodeHandler.INSTANCE);
		scriptScope.put(BhParams.JsKeyword.KEY_BH_MSG_TRANSPORTER, MsgTransporter.INSTANCE);
		scriptScope.put(BhParams.JsKeyword.KEY_BH_COMMON, BhScriptManager.INSTANCE.getCommonJsObj());
	}

	/**
	 * イミテーション作成時のIDを取得する
	 * @return イミテーション作成時のID
	 */
	public ImitationID findImitationID() {
		
		if (imitID.equals(ImitationID.NONE)) {
			Connector parentCnctr = getParentNode().getParentConnector();
			if (parentCnctr != null)
				return parentCnctr.findImitationID();			
		}
		return imitID;
	}
	
	/**
	 * イミテーション接続位置の識別子を取得する
	 * @return イミテーション接続位置の識別子
	 */
	public ImitationConnectionPos getImitCnctPoint() {
		return imitCnctPoint;
	}

	/**
	 * 外部描画ノードかどうかを示すフラグをセットする
	 * @param outer このコネクタが外部描画ノードを接続する場合true
	 */
	public void setOuterFlag(boolean outer) {
		this.outer = outer;
	}
	
	/**
	 * 外部描画ノードをつなぐコネクタかどうかを調べる
	 * @return 外部描画ノードをコネクタの場合true
	 * */
	public boolean isOuter() {
		return outer;
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
		
		connectedNode.findSymbolInDescendants(Math.max(0, hierarchyLevel-1), toBottom, foundSymbolList, symbolNames);
	}
	
	@Override
	public SyntaxSymbol findSymbolInAncestors(String symbolName, int hierarchyLevel, boolean toTop) {

		if (hierarchyLevel == 0) {
			if (Util.equals(getSymbolName(), symbolName)) {
				return this;
			}
			if (!toTop) {
				return null;
			}
		}
			
		return parent.findSymbolInAncestors(symbolName, Math.max(0, hierarchyLevel-1), toTop);
	}
	
	@Override
	public boolean isDescendantOf(SyntaxSymbol ancestor) {

		if (this == ancestor)
			return true;

		return parent.isDescendantOf(ancestor);
	}
	
	/**
	 * モデルの構造を表示する
	 * @param depth 表示インデント数
	 * */
	@Override
	public void show(int depth) {
		MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<Connector" + " bhID=" + id + " nodeID=" + connectedNode.getID() + "  parent=" + parent.hashCode() + "> " + this.hashCode());
		connectedNode.show(depth + 1);
	}
}























