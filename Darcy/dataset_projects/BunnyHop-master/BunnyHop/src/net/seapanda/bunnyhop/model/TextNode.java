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
import java.util.List;
import java.util.Map;

import javax.script.CompiledScript;
import javax.script.ScriptException;

import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.configfilereader.BhScriptManager;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.message.MsgTransporter;
import net.seapanda.bunnyhop.model.imitation.Imitatable;
import net.seapanda.bunnyhop.model.imitation.ImitationID;
import net.seapanda.bunnyhop.model.imitation.ImitationInfo;
import net.seapanda.bunnyhop.model.templates.BhNodeTemplates;
import net.seapanda.bunnyhop.modelprocessor.BhModelProcessor;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * 文字情報を持つ終端BhNode
 * @author K.Koike
 */
public class TextNode  extends Imitatable implements Serializable {

	private String text = "";	//!< このノードの管理する文字列データ
	private final String scriptNameOnTextInput;	//!< テキスト入力時に実行されるスクリプト
	private ImitationInfo<TextNode> imitInfo;	//!< イミテーションノードに関連する情報がまとめられたオブジェクト

	/**
	 * コンストラクタ<br>
	 * ノード内部・外部とは描画位置のこと
	 * @param bhID ノードID (\<Node\> タグの bhID)
	 * @param symbolName  終端, 非終端記号名
	 * @param type xml のtype属性
	 * @param initString 初期文字列
	 * @param scopeName イミテーションノードがオリジナルノードと同じスコープにいるかチェックする際の名前
	 * @param scriptNameOnTextInput 表示文字列のパターンチェックスクリプトの名前
	 * @param scriptNameOnMovedFromChildToWS ワークスペース移動時に実行されるスクリプトの名前
	 * @param scriptNameOnMovedToChild 子ノードとして接続されたときに実行されるスクリプトの名前
	 * @param imitID_imitNodeID イミテーションIDとそれに対応するイミテーションノードIDのマップ
	 * @param canCreateImitManually このノードがイミテーション作成機能を持つ場合true
	 * */
	public TextNode(
			BhNodeID bhID,
			String symbolName,
			String type,
			String initString,
			String scopeName,
			String scriptNameOnTextInput,
			String scriptNameOnMovedFromChildToWS,
			String scriptNameOnMovedToChild,
			Map<ImitationID, BhNodeID> imitID_imitNodeID,
			boolean canCreateImitManually) {
		super(
			bhID,
			symbolName,
			type,
			scriptNameOnMovedFromChildToWS,
			scriptNameOnMovedToChild);
		this.scriptNameOnTextInput = scriptNameOnTextInput;
		imitInfo = new ImitationInfo<>(imitID_imitNodeID, canCreateImitManually, scopeName);
		text = initString;
	}

	/**
	 * コピーコンストラクタ
	 * @param org コピー元オブジェクト
	 */
	private TextNode(TextNode org) {
		super(org);
		text = org.text;
		scriptNameOnTextInput = org.scriptNameOnTextInput;
	}

	@Override
	public TextNode copy(UserOperationCommand userOpeCmd) {
		TextNode newNode = new TextNode(this);
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
	 * このノードが保持している文字列を返す
	 * @return 表示文字列
	 */
	public String getText() {
		return text;
	}

	/**
	 * 引数の文字列をセットする
	 * @param text セットする文字列
	 * */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * 引数の文字列がセット可能かどうか判断する
	 * @param text セット可能かどうか判断する文字列
	 * @return 引数の文字列がセット可能だった
	 * */
	public boolean isTextAcceptable(String text) {

		CompiledScript onTextInput = BhScriptManager.INSTANCE.getCompiledScript(scriptNameOnTextInput);
		if (onTextInput == null)
			return true;

		scriptScope.put(BhParams.JsKeyword.KEY_BH_TEXT, text);
		Object jsReturn = null;
		try {
			jsReturn = onTextInput.eval(scriptScope);
		} catch (ScriptException e) {
			MsgPrinter.INSTANCE.errMsgForDebug(TextNode.class.getSimpleName() +  ".isTextSettable   " + scriptNameOnTextInput + "\n" + e.toString() + "\n");
		}

		if(jsReturn instanceof Boolean)
			return (Boolean)jsReturn;

		return false;
	}

	/**
	 * イミテーションノードにこのノードを模倣させる
	 */
	public void getImitNodesToImitateContents() {
		MsgData mvText = MsgTransporter.INSTANCE.sendMessage(BhMsg.GET_MODEL_AND_VIEW_TEXT, this);
		imitInfo.getImitationList().forEach(
			imit -> MsgTransporter.INSTANCE.sendMessage(BhMsg.IMITATE_TEXT, new MsgData(mvText.strPair._1, mvText.strPair._2), imit));
	}

	/**
	 * モデルの構造を表示する
	 * @param depth 表示インデント数
	 * */
	@Override
	public void show(int depth) {

		String parentHash = "";
		if (parentConnector != null)
			parentHash = parentConnector.hashCode() + "";

		String lastReplacedHash = "";
		if (getLastReplaced() != null)
			lastReplacedHash =  getLastReplaced().hashCode() + "";

		MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<TextNode" + "string=" + text + "  bhID=" + getID() + "  parent="+ parentHash + "> " + this.hashCode());
		MsgPrinter.INSTANCE.msgForDebug(indent(depth+1) + "<" + "ws " + workspace + "> ");
		MsgPrinter.INSTANCE.msgForDebug(indent(depth+1) + "<" + "last replaced " + lastReplacedHash + "> ");
		MsgPrinter.INSTANCE.msgForDebug(indent(depth+1) + "<" + "scopeName " + imitInfo.scopeName + "> ");
		MsgPrinter.INSTANCE.msgForDebug(indent(depth+1) + "<" + "imitation" + "> ");
		imitInfo.getImitationList().forEach(imit -> {
			MsgPrinter.INSTANCE.msgForDebug(indent(depth+2) + "imit " + imit.hashCode());
		});

	}

	@Override
	public TextNode createImitNode(UserOperationCommand userOpeCmd, ImitationID imitID) {

		//イミテーションノード作成
		BhNode imitationNode = BhNodeTemplates.INSTANCE.genBhNode(imitInfo.getImitationNodeID(imitID), userOpeCmd);

		//オリジナルとイミテーションの関連付け
		TextNode textImit = (TextNode)imitationNode; //ノードテンプレート作成時に整合性チェックしているのでキャストに問題はない
		imitInfo.addImitation(textImit, userOpeCmd);
		textImit.getImitationInfo().setOriginal(this, userOpeCmd);
		return textImit;
	}

	@Override
	public ImitationInfo<TextNode> getImitationInfo() {
		return imitInfo;
	}

	@Override
	public BhNode findOuterEndNode() {
		return this;
	}

	@Override
	public void findSymbolInDescendants(int hierarchyLevel, boolean toBottom, List<SyntaxSymbol> foundSymbolList, String... symbolNames) {

		if (hierarchyLevel == 0)
			for (String symbolName : symbolNames)
				if (Util.equals(getSymbolName(), symbolName))
					foundSymbolList.add(this);
	}

	@Override
	public TextNode getOriginalNode() {
		return imitInfo.getOriginal();
	}
}



















