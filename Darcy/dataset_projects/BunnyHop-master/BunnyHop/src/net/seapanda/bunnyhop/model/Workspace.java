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

import net.seapanda.bunnyhop.model.imitation.Imitatable;
import net.seapanda.bunnyhop.modelhandler.BhNodeHandler;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.message.MsgProcessor;
import net.seapanda.bunnyhop.message.MsgTransporter;
import net.seapanda.bunnyhop.undo.UserOperationCommand;
import net.seapanda.bunnyhop.message.MsgReceptionWindow;

/**
 * ワークスペースクラス
 * @author K.Koike
 * */
public class Workspace implements MsgReceptionWindow, Serializable {
	
	private final HashSet<BhNode> rootNodeList = new HashSet<>();	//!< ワークスペースのルートノードのリスト
	private final HashSet<BhNode> selectedList = new HashSet<>();	//!< 選択中のノード
	private final String workspaceName;	//!< ワークスペース名
	transient private WorkspaceSet workspaceSet;	//!< このワークスペースを持つワークスペースセット
	transient private MsgProcessor msgProcessor;	//!< このオブジェクト宛てに送られたメッセージを処理するオブジェクト
	
	/**
	 * コンストラクタ
	 * @param workspaceName ワークスペース名
	 */
	public Workspace(String workspaceName) {
		this.workspaceName = workspaceName;
	}
	
	/**
	 *ルートノードを追加する
	 * @param node 追加するBhノード
	 * */
	public void addRootNode(BhNode node) {
		rootNodeList.add(node);
	}

	/**
	 * ルートノードを削除する
	 * @param node 削除するノード
	 * */
	public void removeRootNode(BhNode node) {
		rootNodeList.remove(node);
	}
	
	/**
	 * このワークスペースを持つワークスペースセットをセットする
	 * @param wss このワークスペースを持つワークスペースセット
	 */
	public void setWorkspaceSet(WorkspaceSet wss) {
		workspaceSet = wss;
	}
	
	/**
	 * このワークスペースを持つワークスペースセットを返す
	 * @return このワークスペースを持つワークスペースセット
	 */
	public WorkspaceSet getWorkspaceSet() {
		return workspaceSet;
	}
	
	/**
	 * ロードのための初期化処理をする
	 */
	public void initForLoad() {
		rootNodeList.clear();
		selectedList.clear();
	}

	/**
	 * 引数で指定したノードをルートノードとして持っているかどうかチェックする
	 * @param node WS直下のルートノードかどうかを調べるノード
	 * @return 引数で指定したノードをルートノードとして持っている場合 true
	 */
	public boolean containsAsRoot(BhNode node) {
		return rootNodeList.contains(node);
	}

	/**
	 * ワークスペース内のルートBhNode の集合を返す
	 * @return ワークスペース内のルートBhNode の集合
	 * */
	public Collection<BhNode> getRootNodeList() {
		return rootNodeList;
	}
	
	/**
	 * 選択されたノードをセットする. すでに選択されているノードは非選択にする
	 * @param selected 新たに選択されたノード
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * */
	public void setSelectedNode(BhNode selected, UserOperationCommand userOpeCmd) {

		if ((selectedList.size() == 1) && selectedList.contains(selected))
			return;
		
		clearSelectedNodeList(userOpeCmd);
		addSelectedNode(selected, userOpeCmd);
	}

	/**
	 * 選択されたノードを選択済みリストに追加する
	 * @param added 追加されるノード
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * */
	public void addSelectedNode(BhNode added, UserOperationCommand userOpeCmd) {
		MsgTransporter.INSTANCE.sendMessage(
			BhMsg.SWITCH_PSEUDO_CLASS_ACTIVATION,
			new MsgData(true, BhParams.CSS.PSEUDO_SELECTED),
			added);
		selectedList.add(added);
		if (added instanceof Imitatable) {
			List<Imitatable> imitationList = ((Imitatable)added).getImitationInfo().getImitationList();
			imitationList.forEach(imitation -> {
				MsgTransporter.INSTANCE.sendMessage(
					BhMsg.SWITCH_PSEUDO_CLASS_ACTIVATION,
					new MsgData(true, BhParams.CSS.PSEUDO_HIGHLIGHT_IMIT),
					imitation);
			});
		}		
		userOpeCmd.pushCmdOfAddSelectedNode(this, added);
	}
	
	/**
	 * 選択中のBhNodeのリストを返す
	 * @return 選択中のBhNodeのリスト
	 */
	public HashSet<BhNode> getSelectedNodeList() {
		return selectedList;
	}
	
	/**
	 * 引数で指定したノードを選択済みリストから削除する
	 * @param removed 選択済みリストから削除するBhNode
	 * @param userOpeCmd undo用コマンドオブジェクト
	 */
	public void removeSelectedNode(BhNode removed, UserOperationCommand userOpeCmd) {
		
		MsgTransporter.INSTANCE.sendMessage(
			BhMsg.SWITCH_PSEUDO_CLASS_ACTIVATION, 
			new MsgData(false, BhParams.CSS.PSEUDO_SELECTED),
			removed);
		selectedList.remove(removed);
		if (removed instanceof Imitatable) {
			List<Imitatable> imitationList = ((Imitatable)removed).getImitationInfo().getImitationList();
			imitationList.forEach(imitation -> {
				MsgTransporter.INSTANCE.sendMessage(
					BhMsg.SWITCH_PSEUDO_CLASS_ACTIVATION,
					new MsgData(false, BhParams.CSS.PSEUDO_HIGHLIGHT_IMIT),
					imitation);
			});
		}
		userOpeCmd.pushCmdOfRemoveSelectedNode(this, removed);
	}
	
	/**
	 * 選択中のノードをすべて非選択にする
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * */
	public void clearSelectedNodeList(UserOperationCommand userOpeCmd) {
		BhNode[] nodesToDeselect = selectedList.toArray(new BhNode[selectedList.size()]);
		for (BhNode node : nodesToDeselect) {
			removeSelectedNode(node, userOpeCmd);
		}
	}
	
	/**
	 * 引数で指定したノードを消去する
	 * @param nodeToDeleteList 消されるノードのリスト
	 * @param userOpeCmd undo用コマンドオブジェクト
	 */
	public void deleteNodes(Collection<BhNode> nodeToDeleteList, UserOperationCommand userOpeCmd) {
		BhNodeHandler.INSTANCE.deleteNodes(nodeToDeleteList, userOpeCmd);
	}
	
	/**
	 * ワークスペース名を取得する
	 * @return ワークスペース名
	 */
	public String getWorkspaceName() {
		return workspaceName;
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














