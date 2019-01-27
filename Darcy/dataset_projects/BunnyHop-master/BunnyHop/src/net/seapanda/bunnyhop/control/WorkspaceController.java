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

import java.lang.reflect.Field;

import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.message.MsgProcessor;
import net.seapanda.bunnyhop.model.Workspace;
import net.seapanda.bunnyhop.modelhandler.DelayedDeleter;
import net.seapanda.bunnyhop.quadtree.QuadTreeManager;
import net.seapanda.bunnyhop.root.BunnyHop;
import net.seapanda.bunnyhop.undo.UserOperationCommand;
import net.seapanda.bunnyhop.view.WorkspaceView;


/**
 * ワークスペースとそれに関連するビューのコントローラ
 * @author K.Koike
 */
public class WorkspaceController implements MsgProcessor {

	private Workspace model; // 操作対象のモデル
	private WorkspaceView view;

	/**
	 * コンストラクタ
	 * @param model コントローラが操作するモデル
	 * @param view コントローラが操作するビュー
	 */
	public WorkspaceController(Workspace model, WorkspaceView view) {
		this.model = model;
		this.view = view;

		this.view.setOnMousePressedEvent(
			event -> {
				UserOperationCommand userOpeCmd = new UserOperationCommand();
				BunnyHop.INSTANCE.hideTemplatePanel();
				model.clearSelectedNodeList(userOpeCmd);
				BunnyHop.INSTANCE.pushUserOpeCmd(userOpeCmd);
			});
	}

	/**
	 * メッセージ受信
	 * @param msg メッセージの種類
	 * @param data メッセージの種類に応じて処理するもの
	 * */
	@Override
	public MsgData processMsg(BhMsg msg, MsgData data) {

		switch (msg) {

			case ADD_ROOT_NODE:
				model.addRootNode(data.node);
				view.addNodeView(data.nodeView);
				break;

			case REMOVE_ROOT_NODE:
				model.removeRootNode(data.node);
				view.removeNodeView(data.nodeView);
				break;

			case ADD_QT_RECTANGLE:
				view.addRectangleToQTSpace(data.nodeView);
				break;

			case CHANGE_WORKSPACE_VIEW_SIZE:
				view.changeWorkspaceViewSize(data.bool);
				break;

			case SCENE_TO_WORKSPACE:
				javafx.geometry.Point2D pos = view.sceneToWorkspace(data.doublePair._1, data.doublePair._2);
				return new MsgData(pos.getX(), pos.getY());

			case ZOOM:
				view.zoom(data.bool);
				break;

			case GET_WORKSPACE_SIZE:
				Point2D size = view.getWorkspaceSize();
				return new MsgData(size.x, size.y);

			case ADD_WORKSPACE:
				return new MsgData(model, view, data.userOpeCmd);

			case DELETE_WORKSPACE:
				model.deleteNodes(model.getRootNodeList(), data.userOpeCmd);
				return new MsgData(model, view, data.userOpeCmd);

			default:
				throw new AssertionError("receive an unknown msg " + msg);
		}

		return null;
	};

	//デバッグ用
	private void printDebugInfo() {

		//4分木登録ノード数表示
		Class<WorkspaceView> c = WorkspaceView.class;
		Field f = null;
		try {
			f = c.getDeclaredField("quadTreeMngForConnector");
			f.setAccessible(true);
			QuadTreeManager quadTreeMngForConnector = (QuadTreeManager)f.get(view);
			MsgPrinter.INSTANCE.msgForDebug("num of QuadTreeNodes " + quadTreeMngForConnector.calcRegisteredNodeNum());
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
			MsgPrinter.INSTANCE.errMsgForDebug(e.toString());
		}

		MsgPrinter.INSTANCE.msgForDebug("num of root nodes " + model.getRootNodeList().size());
		MsgPrinter.INSTANCE.msgForDebug("num of deletion candidates " + DelayedDeleter.INSTANCE.getDeletionCadidateList());
		MsgPrinter.INSTANCE.msgForDebug("num of selected nodes " + model.getSelectedNodeList().size());
	}
}












