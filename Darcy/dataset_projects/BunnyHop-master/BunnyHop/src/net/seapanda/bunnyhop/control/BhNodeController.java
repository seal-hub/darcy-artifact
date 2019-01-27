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
package net.seapanda.bunnyhop.control;

import java.util.ArrayList;

import javafx.event.Event;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.message.MsgProcessor;
import net.seapanda.bunnyhop.message.MsgTransporter;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.VoidNode;
import net.seapanda.bunnyhop.model.Workspace;
import net.seapanda.bunnyhop.model.WorkspaceSet;
import net.seapanda.bunnyhop.model.connective.ConnectiveNode;
import net.seapanda.bunnyhop.modelhandler.BhNodeHandler;
import net.seapanda.bunnyhop.modelhandler.DelayedDeleter;
import net.seapanda.bunnyhop.modelprocessor.UnscopedNodeCollector;
import net.seapanda.bunnyhop.root.BunnyHop;
import net.seapanda.bunnyhop.undo.UserOperationCommand;
import net.seapanda.bunnyhop.view.BhNodeView;

/**
 * BhNode のコントローラクラスに共通の処理をまとめたクラス
 * @author K.Koike
 * */
public class BhNodeController implements MsgProcessor {

	private final BhNode model;
	private final BhNodeView view;
	private final DragAndDropEventInfo ddInfo = this.new DragAndDropEventInfo();

	/**
	 * コンストラクタ
	 * @param model 管理するモデル
	 * @param view 管理するビュー
	 * */
	protected BhNodeController(BhNode model, BhNodeView view) {
		this.model = model;
		this.view = view;
		setMouseEventHandlers();
	}

	/**
	 * 移動可能なノードであるかどうかを調べる
	 * @return 移動可能なノードである場合 true
	 */
	private boolean isMovable() {
		return model.isRemovable() || (model.getState() == BhNode.State.ROOT_DIRECTLY_UNDER_WS);
	}

	/**
	 * 引数で指定したノードに対応するビューにGUIイベントを伝播する
	 * @param node イベントを伝播したいBhNodeView に対応するBhNode
	 * @param event 伝播したいイベント
	 */
	private void propagateGUIEvent(BhNode node, Event event) {

		if (node == null)
			return;
		BhNodeView nodeView = MsgTransporter.INSTANCE.sendMessage(BhMsg.GET_VIEW, node).nodeView;
		nodeView.getEventManager().propagateEvent(event);
		event.consume();
	}

	/**
	 * View が走査されたときのイベントハンドラをセットする
	 * */
	private void setMouseEventHandlers() {

		//マウスボタン押下
		view.getEventManager().setOnMousePressedHandler(mouseEvent -> {
			//model.show(0);	//for debug
			if (!isMovable()) {
				ddInfo.propagateEvent = true;
			}

			if (ddInfo.propagateEvent) {
				propagateGUIEvent(model.findParentNode(), mouseEvent);
				return;
			}

			if (ddInfo.userOpeCmd == null)	//BhNode の新規追加の場合, すでにundo用コマンドオブジェクトがセットされている
				ddInfo.userOpeCmd = new UserOperationCommand();

			if (mouseEvent.isShiftDown()) {
				if (model.isSelected())
					model.getWorkspace().removeSelectedNode(model, ddInfo.userOpeCmd);
				else
					model.getWorkspace().addSelectedNode(model, ddInfo.userOpeCmd);
			}
			else {
				model.getWorkspace().setSelectedNode(model, ddInfo.userOpeCmd);
			}

			javafx.geometry.Point2D mousePressedPos = view.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
			ddInfo.mousePressedPos = new Point2D(mousePressedPos.getX(), mousePressedPos.getY());
			ddInfo.posOnWorkspace = view.getPositionManager().getPosOnWorkspace();
			view.setMouseTransparent(true);
			view.getAppearanceManager().toForeGround();
			mouseEvent.consume();
		});

		//ドラッグ中
		view.getEventManager().setOnMouseDraggedHandler(mouseEvent -> {

			if (ddInfo.propagateEvent) {
				propagateGUIEvent(model.findParentNode(), mouseEvent);
				return;
			}

			if (ddInfo.dragging) {
				double diffX = mouseEvent.getX() - ddInfo.mousePressedPos.x;
				double diffY = mouseEvent.getY() - ddInfo.mousePressedPos.y;
				Point2D newPos = view.getPositionManager().move(diffX, diffY);
				view.getPositionManager().updateAbsPos(newPos.x, newPos.y);	//4分木空間での位置更新
				highlightOverlappedNode();	// ドラッグ検出されていない場合、強調は行わない. 子ノードがダングリングになっていないのに、重なったノード (入れ替え対象) だけが検出されるのを防ぐ
				openCloseTrashbox(mouseEvent.getSceneX(), mouseEvent.getSceneY());
			}
			mouseEvent.consume();
		});

		//ドラッグを検出 (先にsetOnMouseDraggedが呼ばれ、ある程度ドラッグしたときにこれが呼ばれる)
		view.getEventManager().setOnDragDetectedHandler(mouseEvent -> {

			if (ddInfo.propagateEvent) {
				propagateGUIEvent(model.findParentNode(), mouseEvent);
				return;
			}

			ddInfo.dragging = true;
			if(model.isRemovable()) {	//子ノードでかつ取り外し可能 -> 親ノードから切り離し, ダングリング状態へ
				ddInfo.latestParent = model.findParentNode();
				ddInfo.latestRoot = model.findRootNode();
				BhNodeHandler.INSTANCE.removeChild(model, ddInfo.userOpeCmd);
			}
			mouseEvent.consume();
		});

        //マウスボタン離し
		view.getEventManager().setOnMouseReleasedHandler(mouseEvent -> {

			if (ddInfo.propagateEvent) {
				propagateGUIEvent(model.findParentNode(), mouseEvent);
				ddInfo.propagateEvent = false;
				return;
			}

			if (ddInfo.currentOverlapped != null) {
				MsgTransporter.INSTANCE.sendMessage(
					BhMsg.SWITCH_PSEUDO_CLASS_ACTIVATION,
					new MsgData(false, BhParams.CSS.PSEUDO_OVERLAPPED),
					ddInfo.currentOverlapped);
			}

			if ((model.getState() == BhNode.State.ROOT_DANGLING) && ddInfo.currentOverlapped == null) {	//子ノード -> ワークスペース
				toWorkspace(model.getWorkspace());
			}
			else if (ddInfo.currentOverlapped != null) {	//(ワークスペース or 子ノード) -> 子ノード
				toChildNode(ddInfo.currentOverlapped);
			}
			else {	//同一ワークスペース上で移動
				toSameWorkspace();
			}

			DelayedDeleter.INSTANCE.deleteCandidates(ddInfo.userOpeCmd);
			deleteUnscopedNodes(model);
			deleteUnscopedNodes(ddInfo.currentOverlapped);
			if (model.getState() == BhNode.State.ROOT_DIRECTLY_UNDER_WS)
				getRidOfNode(mouseEvent.getSceneX(), mouseEvent.getSceneY());		//ゴミ箱に捨てる
			BunnyHop.INSTANCE.pushUserOpeCmd(ddInfo.userOpeCmd);
			ddInfo.reset();
			view.setMouseTransparent(false);	// 処理が終わったので、元に戻しておく。
			mouseEvent.consume();
		});
	}

	/**
	 * (ワークスペース or 子ノード) から 子ノード に移動する
	 * @param oldChildNode 入れ替え対象の古い子ノード
     **/
	private void toChildNode(BhNode oldChildNode) {

		//ワークスペースから移動する場合
		if(model.getState() == BhNode.State.ROOT_DIRECTLY_UNDER_WS)
			ddInfo.userOpeCmd.pushCmdOfSetPosOnWorkspace(ddInfo.posOnWorkspace.x, ddInfo.posOnWorkspace.y, model);

		ConnectiveNode oldParentOfReplaced = oldChildNode.findParentNode();	//入れ替えられるノードの親ノード
		BhNode oldRootOfReplaced = oldChildNode.findRootNode();	//入れ替えられるノードのルートノード
		BhNodeHandler.INSTANCE.replaceChild(oldChildNode, model, ddInfo.userOpeCmd);	//重なっているノードをこのノードと入れ替え
		model.execScriptOnMovedToChild(
			ddInfo.latestParent,
			ddInfo.latestRoot,
			oldChildNode,
			ddInfo.userOpeCmd);	//接続変更時のスクリプト実行

		Point2D posOnWS = Util.getPosOnWS(oldChildNode);
		double newXPosInWs = posOnWS.x + BhParams.REPLACED_NODE_POS;
		double newYPosInWs = posOnWS.y + BhParams.REPLACED_NODE_POS;
		BhNodeHandler.INSTANCE.moveToWS(oldChildNode.getWorkspace(), oldChildNode, newXPosInWs, newYPosInWs, ddInfo.userOpeCmd);	//重なっているノードをWSに移動
		oldChildNode.execScriptOnMovedFromChildToWS(
			oldParentOfReplaced,
			oldRootOfReplaced,
			model,
			false,
			ddInfo.userOpeCmd);	//接続変更時のスクリプト実行

		if (oldChildNode instanceof VoidNode) { 	//VoidNodeは消す
			BhNodeHandler.INSTANCE.deleteNode(oldChildNode, ddInfo.userOpeCmd);
		}

//		BhNode outerEnd = model.findOuterEndNode();
//		if(outerEnd instanceof VoidNode && outerEnd.isReplaceable(replacedNode) && !(replacedNode instanceof VoidNode)) {
//			BhNodeHandler.INSTANCE.removeFromWS(replacedNode, ddInfo.userOpeCmd);
//			BhNodeHandler.INSTANCE.replaceChild(outerEnd, replacedNode, ddInfo.userOpeCmd);
//			BhNodeHandler.INSTANCE.deleteNode(outerEnd, ddInfo.userOpeCmd);
//		}
	}

	/**
	 * 子ノードからワークスペースに移動する
	 * @param ws 移動先のワークスペース
	 **/
	private void toWorkspace(Workspace ws) {

		Point2D absPosInWS = view.getPositionManager().getPosOnWorkspace();
		BhNodeHandler.INSTANCE.moveToWS(ws, model, absPosInWS.x, absPosInWS.y, ddInfo.userOpeCmd);
		model.execScriptOnMovedFromChildToWS(
			ddInfo.latestParent,
			ddInfo.latestRoot,
			model.getLastReplaced(),
			true,
			ddInfo.userOpeCmd);	//接続変更時のスクリプト実行
	}

	/**
	 * 同一ワークスペースへの移動処理
	 */
	private void toSameWorkspace() {

		if (ddInfo.dragging && (model.getState() == BhNode.State.ROOT_DIRECTLY_UNDER_WS))
			ddInfo.userOpeCmd.pushCmdOfSetPosOnWorkspace(ddInfo.posOnWorkspace.x, ddInfo.posOnWorkspace.y, model);
		view.getAppearanceManager().updateStyle(null);
	}

	/**
	 * スコープ外のノードを削除する
	 * @param topNode このノード以下のスコープ外のノードを削除する. null OK.
	 */
	private void deleteUnscopedNodes(BhNode topNode) {

		if (topNode == null)
			return;

		UnscopedNodeCollector unscopedNodeCollector = new UnscopedNodeCollector();
		topNode.accept(unscopedNodeCollector);
		BhNodeHandler.INSTANCE.deleteNodes(unscopedNodeCollector.getUnscopedNodeList(), ddInfo.userOpeCmd);
	}

	/**
	 * viewと重なっているBhNodeViewを強調する
	 */
	private void highlightOverlappedNode() {

		if (ddInfo.currentOverlapped != null) {
			MsgTransporter.INSTANCE.sendMessage(
				BhMsg.SWITCH_PSEUDO_CLASS_ACTIVATION,
				new MsgData(false, BhParams.CSS.PSEUDO_OVERLAPPED), //前回重なっていたものをライトオフ
				ddInfo.currentOverlapped);
		}
		ddInfo.currentOverlapped = null;

		ArrayList<BhNode> overlappedList = view.getRegionManager().searchOverlappedModel();	//このノードとコネクタ部分が重なっている
		for (BhNode overlapped : overlappedList) {
			if (overlapped.canBeReplacedWith(model)) {	//このノードと入れ替え可能
				MsgTransporter.INSTANCE.sendMessage(
					BhMsg.SWITCH_PSEUDO_CLASS_ACTIVATION,
					new MsgData(true, BhParams.CSS.PSEUDO_OVERLAPPED),
					overlapped);	//今回重なっているものをライトオン
				ddInfo.currentOverlapped = overlapped;
				break;
			}
		}
	}

	/**
	 * ゴミ箱の開閉を行う
	 * @param sceneX Scene上でのマウスポインタのX位置
	 * @param sceneY Scene上でのマウスポインタのY位置
	 */
	private void openCloseTrashbox(double sceneX, double sceneY) {

		boolean inTrashboxArea = MsgTransporter.INSTANCE.sendMessage(
			BhMsg.IS_IN_TRASHBOX_AREA,
			new MsgData(sceneX, sceneY),
			model.getWorkspace().getWorkspaceSet()).bool;

		MsgTransporter.INSTANCE.sendMessage(
			BhMsg.OPEN_TRAHBOX,
			new MsgData(inTrashboxArea),
			model.getWorkspace().getWorkspaceSet());
	}

	/**
	 * 引数で指定した位置がゴミ箱エリアにあった場合, そのノードを削除する
	 */
	private void getRidOfNode(double sceneX, double sceneY) {

		WorkspaceSet wss = model.getWorkspace().getWorkspaceSet();
		MsgTransporter.INSTANCE.sendMessage(
			BhMsg.OPEN_TRAHBOX,
			new MsgData(false),
			wss);

		boolean inTrashboxArea = MsgTransporter.INSTANCE.sendMessage(
			BhMsg.IS_IN_TRASHBOX_AREA,
			new MsgData(sceneX, sceneY),
			wss).bool;

		if (inTrashboxArea) {
			BhNodeHandler.INSTANCE.deleteNode(model, ddInfo.userOpeCmd);
		}
	}

	/**
	 * 受信したメッセージを処理する
	 * @param msg メッセージの種類
	 * @param data メッセージの種類に応じて処理するデータ
	 * @return メッセージを処理した結果返すデータ
	 * */
	@Override
	public MsgData processMsg(BhMsg msg, MsgData data) {

		Point2D pos;

		switch (msg) {

			case ADD_ROOT_NODE: // model がWorkSpace のルートノードとして登録された
			case REMOVE_ROOT_NODE:
				return  new MsgData(model, view);

			case ADD_QT_RECTANGLE:
				return new MsgData(view);

			case REMOVE_QT_RECTANGLE:
				view.getRegionManager().removeQtRectable();
				break;

			case GET_POS_ON_WORKSPACE:
				pos = view.getPositionManager().getPosOnWorkspace();
				return new MsgData(pos.x, pos.y);

			case SET_POS_ON_WORKSPACE:
				view.getPositionManager().setRelativePosFromParent(data.doublePair._1, data.doublePair._2);
				break;

			case GET_VIEW_SIZE_WITH_OUTER:
				Point2D size = view.getRegionManager().getBodyAndOuterSize(data.bool);
				return new MsgData(size.x, size.y);

			case UPDATE_ABS_POS:
				pos = view.getPositionManager().getPosOnWorkspace();	//workspace からの相対位置を計算
				view.getPositionManager().updateAbsPos(pos.x, pos.y);
				break;

			case REPLACE_NODE_VIEW:	//このコントローラの管理するノードビューを引数のノードビューと入れ替える (古いノードのGUIツリーからの削除は行わない)
				BhNodeView newView = data.nodeView;
				view.getTreeManager().replace(newView);	//新しいノードビューに入れ替え
				newView.getAppearanceManager().updateStyle(null);
				break;

			case SWITCH_PSEUDO_CLASS_ACTIVATION:
				view.getAppearanceManager().switchPseudoClassActivation(data.bool, data.text);
				break;

			case GET_VIEW:
				return new MsgData(view);

			case SET_USER_OPE_CMD:
				ddInfo.userOpeCmd = data.userOpeCmd;
				break;

			case REMOVE_FROM_GUI_TREE:
				view.getTreeManager().removeFromGUITree();
				break;

			default:
				throw new AssertionError("receive an unknown msg " + msg);
		}

		return null;
	}

	/**
	 * D&D操作で使用する一連のイベントハンドラがアクセスするデータをまとめたクラス
	 **/
	private class DragAndDropEventInfo {
		Point2D mousePressedPos = null;
		Point2D posOnWorkspace = null;
		BhNode currentOverlapped = null;	//現在重なっているView
		boolean propagateEvent = false;	//!< イベントを親ノードに伝播する場合true
		boolean dragging = false;	//!< ドラッグ中ならtrue
		ConnectiveNode latestParent = null;	//!< 最後につながっていた親ノード
		BhNode latestRoot = null;	//!< 最後に子孫であったルートノード
		UserOperationCommand userOpeCmd;	//!< D&D操作のundo用コマンド

		/**
		 * D&Dイベント情報を初期化する
		 */
		public void reset() {
			mousePressedPos = null;
			posOnWorkspace = null;
			currentOverlapped = null;
			propagateEvent = false;
			dragging = false;
			latestParent = null;
			latestRoot = null;
			userOpeCmd = null;
		}
	}
}










