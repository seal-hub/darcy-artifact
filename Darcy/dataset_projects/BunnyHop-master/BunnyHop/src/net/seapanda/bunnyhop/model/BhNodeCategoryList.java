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

import java.nio.file.Path;
import java.nio.file.Paths;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.TreeNode;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.configfilereader.BhScriptManager;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.message.MsgProcessor;
import net.seapanda.bunnyhop.message.MsgReceptionWindow;
import net.seapanda.bunnyhop.model.templates.BhNodeTemplates;

/**
 * BhNode のカテゴリ一覧を表示している部分のmodel
 * @author K.Koike
 * */
public class BhNodeCategoryList implements MsgReceptionWindow {

	private TreeNode<String> templateTreeRoot;
	private MsgProcessor msgProcessor;	//!< このオブジェクト宛てに送られたメッセージを処理するオブジェクト
	
	public BhNodeCategoryList(){};

	/**
	 * ノードテンプレートの配置情報が記されたファイルを読み込み
	 * テンプレートリストを作成する
	 * @return テンプレートの作成に成功した場合true
	 * */
	public boolean genNodeCategoryList() {

		Path filePath = Paths.get(Util.EXEC_PATH, BhParams.Path.BH_DEF_DIR, BhParams.Path.TEMPLATE_LIST_DIR, BhParams.Path.NODE_TEMPLATE_LIST_JSON);
		ScriptObjectMirror jsonObj = BhScriptManager.INSTANCE.parseJsonFile(filePath);
		templateTreeRoot = new TreeNode<>("root");
		return addChildren(jsonObj, templateTreeRoot, filePath.toString());
	}

	/**
	 * テンプレートツリーに子ノードを追加する.<br>
	 * 子ノードが葉だった場合、対応するBhNode があるかどうかを調べ, ない場合はfalse を返す
	 * @param jsonObj このオブジェクトのメンバーが追加すべき子ノードとなる
	 * @param parent 子ノードを追加したいノード
	 * @return 下位の葉ノードに対応するBhNode があった場合true
	 * */
	private boolean addChildren(ScriptObjectMirror jsonObj, TreeNode<String> parent, String fileName) {
		
		boolean bhNodeForLeafExists = true;
		for(String key : jsonObj.keySet()) {
		
			Object val = jsonObj.get(key);
			switch (key) {
				case BhParams.NodeTemplateList.KEY_CSS_CLASS:	//cssクラスのキー
					if (!(val instanceof String))
						continue;
					TreeNode<String> cssClass = new TreeNode<>(BhParams.NodeTemplateList.KEY_CSS_CLASS);
					cssClass.children.add(new TreeNode<>(val.toString()));
					parent.children.add(cssClass);
					break;
					
				case BhParams.NodeTemplateList.KEY_CONTENTS:	//ノードIDの配列のキー
					if (!(val instanceof ScriptObjectMirror))
						continue;
					if (((ScriptObjectMirror)val).isArray()) {
						TreeNode<String> contents = new TreeNode<>(BhParams.NodeTemplateList.KEY_CONTENTS);
						bhNodeForLeafExists &= addBhNodeID((ScriptObjectMirror)val, contents, fileName);
						parent.children.add(contents);
					}	break;
					
				default:					//カテゴリ名
					if (!(val instanceof ScriptObjectMirror))
						continue;
					if (!((ScriptObjectMirror)val).isArray()) {
						TreeNode<String> child = new TreeNode<>(key);
						parent.children.add(child);
						bhNodeForLeafExists &= addChildren((ScriptObjectMirror)val, child, fileName);
					}	break;
			}
		}
		return bhNodeForLeafExists;
	}

	/**
	 * JsonArray からBhNode の ID を読み取って、対応するノードがあるIDのみ子ノードとして追加する
	 * @param bhNodeIDList BhNode の ID のリスト
	 * @param parent IDを子ノードとして追加する親ノード
	 * @return 各IDに対応するBhNodeがすべて見つかった場合true
	 * */
	private boolean addBhNodeID(ScriptObjectMirror bhNodeIDList, TreeNode<String> parent, String fileName) {

		boolean allBhNodesExist = true;
		for (Object bhNodeID : bhNodeIDList.values()) {
			if (String.class.isAssignableFrom(bhNodeID.getClass())) {	// 配列内の文字列だけをBhNode の IDとみなす

				String bhNodeIDStr = (String)bhNodeID;
				if (BhNodeTemplates.INSTANCE.bhNodeExists(BhNodeID.createBhNodeID(bhNodeIDStr))) {	//IDに対応する BhNode がある
					parent.children.add(new TreeNode<>(bhNodeIDStr));
				}
				else {
					allBhNodesExist &= false;
					MsgPrinter.INSTANCE.errMsgForDebug(bhNodeIDStr + " に対応する " + BhParams.BhModelDef.ELEM_NAME_NODE + " が存在しません.\n" + "(" + fileName + ")");
				}
			}
		}
		return allBhNodesExist;
	}
	/**
	 * BhNode選択リストのルートノードを返す
	 * @return BhNode選択リストのルートノード
	 **/
	public TreeNode<String> getRootNode() {
		return templateTreeRoot;
	}
	
	@Override
	public void setMsgProcessor(MsgProcessor processor) {
		msgProcessor = processor;
	}
	
	@Override
	public MsgData passMsg(BhMsg msg, MsgData data) {
		return msgProcessor.processMsg(msg, data);
	}
}





















