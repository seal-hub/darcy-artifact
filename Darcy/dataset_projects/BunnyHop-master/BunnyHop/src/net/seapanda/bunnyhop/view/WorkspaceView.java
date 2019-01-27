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
package net.seapanda.bunnyhop.view;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import net.seapanda.bunnyhop.quadtree.QuadTreeManager;
import net.seapanda.bunnyhop.quadtree.QuadTreeRectangle;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Pair;
import net.seapanda.bunnyhop.model.*;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.root.BunnyHop;
import net.seapanda.bunnyhop.configfilereader.FXMLCollector;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * ワークスペースを表すビュー (タブの中の描画物に対応)
 * @author K.Koike
 * */
public class WorkspaceView extends Tab {

	private @FXML ScrollPane scrollPane;	//!< 操作対象のビュー
	private @FXML Pane wsPane;	//!< 操作対象のビュー
	private @FXML Pane wsWrapper;	//!< wsPane の親ペイン
	private final Workspace workspace;
	private final Point2D minPaneSize = new Point2D(0.0, 0.0);
	private final ArrayList<BhNodeView> rootNodeViewList = new ArrayList<>();	//このワークスペースにあるルートBhNodeViewのリスト
	private QuadTreeManager quadTreeMngForBody;			//!< ノードの本体部分の重なり判定に使う4分木管理クラス
	private QuadTreeManager quadTreeMngForConnector;	//!< ノードのコネクタ部分の重なり判定に使う4分木管理クラス
	int zoomLevel = 0;	//!< ワークスペースの拡大/縮小の段階
	int workspaceSizeLevel = 0;	//!< ワークスペースの大きさの段階
	
	public WorkspaceView(Workspace workspace) {
		this.workspace = workspace;
	}

	/**
	 * 初期化処理を行う
	 * @param width ワークスペースの初期幅
	 * @param height ワークスペースの初期高さ
	 * @return 初期化に成功した場合 true
	 */
	public boolean init(double width, double height) {

		try {
			Path filePath = FXMLCollector.INSTANCE.getFilePath(BhParams.Path.WORKSPACE_FXML);
			FXMLLoader loader = new FXMLLoader(filePath.toUri().toURL());
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("failed to initizlize " + WorkspaceView.class.getSimpleName() + "\n" + e.toString());
			return false;
		}
	
		minPaneSize.x = width;
		minPaneSize.y = height;
		wsPane.setMinSize(minPaneSize.x, minPaneSize.y);	//タブの中の部分の最小サイズを決める		
		wsPane.getTransforms().add(new Scale());
		quadTreeMngForBody = new QuadTreeManager(BhParams.NUM_DIV_OF_QTREE_SPACE, minPaneSize.x, minPaneSize.y);
		quadTreeMngForConnector = new QuadTreeManager(BhParams.NUM_DIV_OF_QTREE_SPACE, minPaneSize.x, minPaneSize.y);
		drawGridLines(minPaneSize.x, minPaneSize.y, quadTreeMngForBody.getNumPartitions());		
		
		//拡大縮小処理
		scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if (event.isControlDown()) {
				event.consume();
				boolean zoomIn = event.getDeltaY() >= 0;
				zoom(zoomIn);
			}
		});

		setOnClosed(event -> {
			UserOperationCommand userOpeCmd = new UserOperationCommand();
			BunnyHop.INSTANCE.deleteWorkspace(workspace, userOpeCmd);
			BunnyHop.INSTANCE.pushUserOpeCmd(userOpeCmd);
		});
		
		setOnCloseRequest(event -> {
			
			if (workspace.getRootNodeList().isEmpty())	//空のワークスペース削除時は警告なし
				return;
			
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("ワークスペースの削除");
			alert.setHeaderText(null);
			alert.setContentText("ワークスペースを削除します.");
			alert.getDialogPane().getStylesheets().addAll(BunnyHop.INSTANCE.getAllStyles());
			Optional<ButtonType> buttonType = alert.showAndWait();			
			buttonType.ifPresent(btnType -> {
				if (btnType.equals(ButtonType.OK))
					event.consume();
			});
		});
		
		setText(workspace.getWorkspaceName());
		return true;
	}

	/**
	 * ノードビューを追加する
	 * @param nodeView 追加するノードビュー (nullダメ)
	 * */
	public void addNodeView(BhNodeView nodeView) {
		assert(nodeView != null);
		assert(rootNodeViewList.indexOf(nodeView) == -1);	//すでに登録済みでないかチェック
		wsPane.getChildren().add(nodeView);
		rootNodeViewList.add(nodeView);
	}

	/**
	 * ノードビューを削除する
	 * @param nodeView 削除するビュー (nullダメ)
	 * */
	public void removeNodeView(BhNodeView nodeView) {
		assert(nodeView != null);
		wsPane.getChildren().remove(nodeView);
		rootNodeViewList.remove(nodeView);
	}

	/**
	 * 4分木空間に矩形を登録する
	 * @param nodeView 登録する矩形を持つBhNodeViewオブジェクト
	 * */
	public void addRectangleToQTSpace(BhNodeView nodeView) {

		nodeView.accept(view -> {
			Pair<QuadTreeRectangle, QuadTreeRectangle> body_cnctr = view.getRegionManager().getRegion();
			//quadTreeMngForBody.addQuadTreeObj(body_cnctr._1); // 現状ボディ部分の重なり判定は不要
			quadTreeMngForConnector.addQuadTreeObj(body_cnctr._2);
		});
	}

	/**
	 * WS内でマウスが押された時の処理を登録する
	 * @param handler WS内でマウスが押されたときの処理
	 * */
	public void setOnMousePressedEvent(EventHandler<? super MouseEvent> handler) {
		scrollPane.setOnMousePressed(handler);
	}

	/**
	 * ワークスペースの大きさを返す
	 * @return ワークスペースの大きさ
	 */
	public Point2D getWorkspaceSize() {
		return new Point2D(wsPane.getWidth(), wsPane.getHeight());
	}
	
	/**
	 * このViewに対応しているWorkspace を返す
	 * @return このViewに対応しているWorkspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}
	
	/**
	 * ワークスペースの大きさを変える
	 * @param widen ワークスペースの大きさを大きくする場合true
	 */
	public void changeWorkspaceViewSize(boolean widen) {
		
		if ((workspaceSizeLevel == BhParams.MIN_WORKSPACE_SIZE_LEVEL) && !widen)
			return;
		if ((workspaceSizeLevel == BhParams.MAX_WORKSPACE_SIZE_LEVEL) && widen)
			return;
		
		workspaceSizeLevel = widen ? workspaceSizeLevel + 1 : workspaceSizeLevel - 1;		
		Point2D currentSize = quadTreeMngForBody.getQTSpaceSize();
		double newWsWidth = widen ? currentSize.x * 2.0 : currentSize.x / 2.0;
		double newWsHeight = widen ? currentSize.y * 2.0 : currentSize.y / 2.0;
		
		wsPane.setMinSize(newWsWidth, newWsHeight);
		quadTreeMngForBody = new QuadTreeManager(quadTreeMngForBody, BhParams.NUM_DIV_OF_QTREE_SPACE, newWsWidth, newWsHeight);
		quadTreeMngForConnector = new QuadTreeManager(quadTreeMngForConnector, BhParams.NUM_DIV_OF_QTREE_SPACE, newWsWidth, newWsHeight);
		
		//全ノードの位置更新
		for (BhNodeView rootView : rootNodeViewList) {
			Point2D pos = rootView.getPositionManager().getPosOnWorkspace();	//workspace からの相対位置を計算
			rootView.getPositionManager().updateAbsPos(pos.x, pos.y);
		}
		drawGridLines(newWsWidth, newWsHeight, quadTreeMngForBody.getNumPartitions());
		wsWrapper.setPrefSize(
			wsPane.getMinWidth() * wsPane.getTransforms().get(0).getMxx(),
			wsPane.getMinHeight() * wsPane.getTransforms().get(0).getMyy());	//スクロールバーの可動域が変わるようにする
	}
	
	//デバッグ用
	private void drawGridLines(double width, double height, int numDiv) {
		
		ArrayList<Line> removedList = new ArrayList<>();
		wsPane.getChildren().forEach((content) -> {
			if (content instanceof Line)
				removedList.add((Line)content);
		});
		removedList.forEach((line) -> {
			wsPane.getChildren().remove(line);
		});

		for(int i = 0; i < numDiv; ++i) {
			int x = (int)((width / numDiv) * i);
			wsPane.getChildren().add(0, new Line(x, 0, x, height));
		}
		for(int i = 0; i < numDiv; ++i) {
			int y = (int)((height / numDiv) * i);
			wsPane.getChildren().add(0, new Line(0, y, width, y));
		}
	}
	
	/**
	 * 引数のローカル座標をWorkspace上での位置に変換して返す
	 * @param x Scene座標の変換したいX位置
	 * @param y Scene座標の変換したいY位置
	 * @return 引数の座標のWorkspace上の位置
	 * */
	public javafx.geometry.Point2D sceneToWorkspace(double x, double y) {
		return wsPane.sceneToLocal(x, y);
	}
	
	/**
	 * ワークスペースのズーム処理を行う
	 * @param zoomIn 拡大処理を行う場合true
	 */
	public void zoom(boolean zoomIn) {
		
		if ((BhParams.MIN_ZOOM_LEVEL == zoomLevel) && !zoomIn)
			return;
		
		if ((BhParams.MAX_ZOOM_LEVEL == zoomLevel) && zoomIn)
			return;
		
		Scale scale = new Scale();
		if (zoomIn)
			++zoomLevel;
		else
			--zoomLevel;
		double mag = Math.pow(BhParams.ZOOM_MAGNIFICATION, zoomLevel);
		scale.setX(mag);
		scale.setY(mag);
		wsPane.getTransforms().clear();
		wsPane.getTransforms().add(scale);
		wsWrapper.setPrefSize(
			wsPane.getMinWidth() * wsPane.getTransforms().get(0).getMxx(),
			wsPane.getMinHeight() * wsPane.getTransforms().get(0).getMyy());	//スクロール時にスクロールバーの可動域が変わるようにする
	}
}






