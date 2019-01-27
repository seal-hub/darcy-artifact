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
package net.seapanda.bunnyhop.saveandload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import net.seapanda.bunnyhop.common.Pair;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.control.WorkspaceController;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgTransporter;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.Workspace;
import net.seapanda.bunnyhop.modelhandler.BhNodeHandler;
import net.seapanda.bunnyhop.modelprocessor.NodeMVCBuilder;
import net.seapanda.bunnyhop.modelprocessor.TextImitationPrompter;
import net.seapanda.bunnyhop.undo.UserOperationCommand;
import net.seapanda.bunnyhop.view.WorkspaceView;

/**
 * 全ワークスペースの保存に必要なデータを保持するクラス
 * @author Koike
 */
public class ProjectSaveData implements Serializable{

	private final List<WorkspaceSaveData> workspaceSaveList;

	/**
	 * コンストラクタ
	 * @param workspaceList 保存するワークスペースのリスト
	 */
	public ProjectSaveData(Collection<Workspace> workspaceList) {
		workspaceSaveList = workspaceList.stream().map(workspace -> {
			WorkspaceSaveData wsSaveData = this.new WorkspaceSaveData(workspace);
			return wsSaveData;
		})
		.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * ワークスペースのリストをワークスペースセットに追加できる状態にして返す
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * @return ロードしたワークスペースのリスト
	 */
	public List<Workspace> load(UserOperationCommand userOpeCmd) {

		workspaceSaveList.forEach(wsSaveData -> wsSaveData.initBhNodes());
		return workspaceSaveList.stream().map(wsSaveData -> {
			return wsSaveData.load(userOpeCmd);
		})
		.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * ワークスペースの保存に必要なデータを保持するクラス
	 */
	private class WorkspaceSaveData implements Serializable {

		private final Workspace ws;	//!< 保存するワークスペース
		private final Point2D workspaceSize;	//!< ワークスペースのサイズ
		private final List<RootNodeSaveData> rootNodeSaveList;

		public WorkspaceSaveData(Workspace ws){
			this.ws = ws;
			Pair<Double, Double> wsSize = MsgTransporter.INSTANCE.sendMessage(BhMsg.GET_WORKSPACE_SIZE, ws).doublePair;
			workspaceSize = new Point2D(wsSize._1, wsSize._2);
			rootNodeSaveList = ws.getRootNodeList().stream().map(rootNode -> {
				return this.new RootNodeSaveData(rootNode);
			})
			.collect(Collectors.toCollection(ArrayList::new));
		}

		/**
		 * ワークスペース以下の全てのBhNode を初期化する
		 * @param userOpeCmd undo用コマンドオブジェクト
		 */
		public void initBhNodes() {
			rootNodeSaveList.forEach(nodeSaveData -> nodeSaveData.createMVC());
			rootNodeSaveList.forEach(nodeSaveData -> nodeSaveData.imitOrgNode());
		}

		/**
		 * ワークスペースをワークスペースセットに追加できる状態にして返す
		 * @param userOpeCmd undo用コマンドオブジェクト
		 * @return ロードしたワークスペース
		 */
		public Workspace load(UserOperationCommand userOpeCmd) {

			WorkspaceView wsView = new WorkspaceView(ws);
			wsView.init(workspaceSize.x, workspaceSize.y);
			WorkspaceController wsController = new WorkspaceController(ws, wsView);
			ws.setMsgProcessor(wsController);
			ws.initForLoad();
			rootNodeSaveList.forEach(nodeSaveData -> {
				Pair<BhNode, Point2D> rootNode_pos = nodeSaveData.getBhNodeAndPos();
				Point2D pos = rootNode_pos._2;
				BhNode rootNode = rootNode_pos._1;
				BhNodeHandler.INSTANCE.addRootNode(ws, rootNode, pos.x, pos.y, userOpeCmd);
			});
			return ws;
		}

		private class RootNodeSaveData implements Serializable {
			private final BhNode rootNode;	//!<保存するルートノード
			private final Point2D nodePos;	//!< ルートノードの位置

			RootNodeSaveData(BhNode rootNode) {
				this.rootNode = rootNode;
				Point2D pos = Util.getPosOnWS(rootNode);
				nodePos = new Point2D(pos.x, pos.y);
			}

			/**
			 * MVC構築する
			 */
			public void createMVC() {
				rootNode.accept(new NodeMVCBuilder(NodeMVCBuilder.ControllerType.Default));	//MVC構築
			}

			/**
			 * イミテーションノードにオリジナルノードを模倣させる.
			 */
			public void imitOrgNode() {
				rootNode.accept(new TextImitationPrompter());
			}

			/**
			 * BhNodeとその位置を返す
			 * @return ロードしたBhNodeとその位置のペア
			 */
			public Pair<BhNode, Point2D> getBhNodeAndPos() {
				return new Pair<>(rootNode, nodePos);
			}
		}
	}
}
