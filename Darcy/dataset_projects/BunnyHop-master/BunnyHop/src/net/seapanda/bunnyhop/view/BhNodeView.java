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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Pair;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.Showable;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgTransporter;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.quadtree.QuadTreeManager;
import net.seapanda.bunnyhop.quadtree.QuadTreeRectangle;
import net.seapanda.bunnyhop.view.BhNodeViewStyle.CNCTR_POS;
import net.seapanda.bunnyhop.view.connectorshape.ConnectorShape;
import net.seapanda.bunnyhop.view.connectorshape.ConnectorShape.CNCTR_SHAPE;

/**
 * ノードのビュークラス <br>
 * 大きさや色などの変更を行うインタフェースを提供 <br>
 * 位置変更のインタフェースを提供
 * View ノード同士の親子関係を処理するインタフェースを提供 <br>
 * イベントハンドラ登録用インタフェースを提供 <br>
 * ノードのシェイプを持つペインを継承している
 * @author K.Koike
 * */
public abstract class BhNodeView extends Pane implements Showable {

	final protected Polygon nodeShape = new Polygon();	//!< 描画されるポリゴン
	final protected BhNodeViewStyle viewStyle;	//!< ノードの見た目のパラメータオブジェクト
	final private BhNode model;
	protected BhNodeViewGroup parent = null;	//!<このノードが子ノードとなっているConnectiveView のグループ

	final protected BhNodeViewConnector connectorPart;	//!< コネクタ部分
	final private ViewRegionManager viewRegionManager = this.new ViewRegionManager();
	final private ViewTreeManager viewTreeManager = this.new ViewTreeManager();
	final private PositionManager positionManager = this.new PositionManager();
	final private EventManager eventHandlerManager = this.new EventManager();
	final private AppearanceManager appearanceManager = this.new AppearanceManager();

	/**
	 * 初期化する
	 */
	protected void initialize() {
		getChildren().add(nodeShape);
	}

	/**
	 * このビューのモデルであるBhNodeを取得する
	 * @return このビューのモデルであるBhNode
	 */
	abstract public BhNode getModel();

	/**
	 * コンストラクタ
	 * @param viewStyle ノードの見た目を決めるパラメータオブジェクト
	 * @param model ビューが表すモデル
	 * */
	protected BhNodeView(BhNodeViewStyle viewStyle, BhNode model) {

		this.setPickOnBounds(false);	//nodeShape 部分だけがMouseEvent を拾うように
		this.viewStyle = viewStyle;
		this.model = model;
		connectorPart = this.new BhNodeViewConnector(viewStyle.connectorShape);
		appearanceManager.addCssClass(viewStyle.cssClass);
		appearanceManager.addCssClass(BhParams.CSS.CLASS_BHNODE);
	}

	/**
	 * BhNodeView を引数にとる関数オブジェクトを実行する<br>
	 * @param visitorFunc BhNodeView を引数にとり処理するオブジェクト
	 * */
	public void accept(Consumer<BhNodeView> visitorFunc) {
		visitorFunc.accept(this);
	}

	/**
	 * ノードの領域関連の処理用インタフェースを返す
	 * @return ノードの領域関連の処理用インタフェース
	 * */
	public ViewRegionManager getRegionManager() {
		return viewRegionManager;
	}

	/**
	 * View の親子関係の処理用インタフェースを返す
	 * @return View の親子関係の処理用インタフェース
	 * */
	public ViewTreeManager getTreeManager() {
		return viewTreeManager;
	}

	/**
	 * 位置変更/取得用インタフェースを返す
	 * @return 位置変更/取得用インタフェース
	 * */
	public PositionManager getPositionManager() {
		return positionManager;
	}

	/**
	 * イベントハンドラ登録用インタフェースを返す
	 * @return イベントハンドラ登録用インタフェース
	 * */
	public  EventManager getEventManager() {
		return eventHandlerManager;
	}

	/**
	 * 見た目変更用インタフェースを返す
	 * @return 見た目変更用インタフェース
	 * */
	public AppearanceManager getAppearanceManager() {
		return appearanceManager;
	}

	/**
	 * コネクタ情報へのアクセス用インタフェースを返す
	 * @return コネクタ情報へのアクセス用インタフェース
	 */
	public BhNodeViewConnector getConnectorManager() {
		return connectorPart;
	}

	/**
	 * サブクラスから処理を指定する必要のある関数のオブジェクトをセットする
	 * @param updateStyleFunc 大きさ変更時に呼ばれる関数. (必ず指定すること)
	 * @param updateAbsPosFunc 絶対位置更新時の関数
	 * */
	final protected void setFuncs(
		Consumer<BhNodeViewGroup> updateStyleFunc,
		BiConsumer<Double, Double> updateAbsPosFunc) {

		if (updateStyleFunc != null)
			appearanceManager.setFuncUpdateStyle(updateStyleFunc);

		if (updateAbsPosFunc != null)
			positionManager.setFuncUpdateAbsPos(updateAbsPosFunc);
	}

	/**
	 * BhNodeのコネクタに関する処理をするクラス
	 */
	public class BhNodeViewConnector {

		private final ConnectorShape connector;	//!< コネクタ部分の形を表すオブジェクト

		/**
		 * コンストラクタ
		 * @param shape コネクタの形
		 * */
		BhNodeViewConnector(CNCTR_SHAPE shape) {
			connector = ConnectorShape.genConnector(shape);
		}

		public Collection<Double> createVertices() {

			double offsetX = 0.0;	// 頂点に加算するオフセットX
			double offsetY = 0.0;	// 頂点に加算するオフセットY

			if (viewStyle.connectorPos == CNCTR_POS.LEFT) {
				offsetX = -viewStyle.connectorWidth;
				offsetY = viewStyle.connectorShift;
			}
			else if (viewStyle.connectorPos == CNCTR_POS.TOP) {
				offsetX = viewStyle.connectorShift;
				offsetY = -viewStyle.connectorHeight;
			}
			return connector.createVertices(offsetX, offsetY, viewStyle.connectorWidth, viewStyle.connectorHeight, viewStyle.connectorPos);
		}

		/**
		 * コネクタの大きさを返す
		 * @return コネクタの大きさ
		 * */
		public Point2D getConnectorSize() {
			return viewStyle.getConnectorSize();
		}

		/**
		 * コネクタの位置を返す
		 * @return コネクタの位置
		 */
		public CNCTR_POS getConnectorPos() {
			return viewStyle.connectorPos;
		}
	}

	/**
	 * 見た目を変更する処理を行うクラス
	 * */
	public class AppearanceManager {

		private Consumer<BhNodeViewGroup> updateStyleFunc;	//!< ノードの形状を更新する関数

		/**
		 * CSSの擬似クラスの有効無効を切り替える
		 * @param activate 擬似クラスを有効にする場合true
		 * @param pseudoClassName 有効/無効を切り替える擬似クラス名
		 * */
		public void switchPseudoClassActivation(boolean activate, String pseudoClassName) {

			if (activate) {
				nodeShape.pseudoClassStateChanged(PseudoClass.getPseudoClass(pseudoClassName), true);
				BhNodeView.this.pseudoClassStateChanged(PseudoClass.getPseudoClass(pseudoClassName), true);
			}
			else {
				nodeShape.pseudoClassStateChanged(PseudoClass.getPseudoClass(pseudoClassName), false);
				BhNodeView.this.pseudoClassStateChanged(PseudoClass.getPseudoClass(pseudoClassName), false);
			}
		}

		/**
		 * 最前面に移動する
		 * */
		public void toForeGround() {
			BhNodeView.this.toFront();
			if (parent != null)
				parent.toForeGround();
		}

		/**
		 * cssクラス名を追加する
		 * @param cssClassName cssクラス名
		 * */
		public void addCssClass(String cssClassName) {
			nodeShape.getStyleClass().add(cssClassName);
			BhNodeView.this.getStyleClass().add(cssClassName + BhParams.CSS.CLASS_SUFFIX_PANE);
		}

		/**
		 * ノードを形作るポリゴンを更新する
		 * @param drawBody ボディを描画する場合 true
		 * */
		protected void updatePolygonShape(boolean drawBody) {

			nodeShape.getPoints().clear();
			Collection<Double> vertices = createVertices(drawBody);
			nodeShape.getPoints().addAll(vertices);

			if ((!drawBody) && (viewStyle.connectorShape == ConnectorShape.CNCTR_SHAPE.CNCTR_SHAPE_NONE)) {
				nodeShape.setVisible(false);
			}
			else {
				nodeShape.setVisible(true);
			}
		}

		/**
		 * ノードを形作る頂点を生成する
		 * */
		private Collection<Double> createVertices(boolean drawBody) {

			Point2D bodySize = getRegionManager().getBodySize(false);
			double nodeWidth = bodySize.x;
			double nodeHeight = bodySize.y;
			ArrayList<Double> bodyVertices = null;

			//本体部分の頂点位置計算
			if (!drawBody) {
				if (viewStyle.connectorPos == CNCTR_POS.LEFT)
					bodyVertices = new ArrayList<>(Arrays.asList(0.0, viewStyle.connectorShift + viewStyle.connectorHeight / 2.0));
				else if (viewStyle.connectorPos == CNCTR_POS.TOP)
					bodyVertices = new ArrayList<>(Arrays.asList(viewStyle.connectorShift + viewStyle.connectorWidth / 2.0, 0.0));
			}
			else {

				if (viewStyle.connectorPos == CNCTR_POS.LEFT) {
					//ノードの形はここの値で決まる
					bodyVertices = new ArrayList<>(Arrays.asList(
						 0.0,                                       0.0 + 0.2 * BhParams.NODE_SCALE,
						 0.0 + 0.2 * BhParams.NODE_SCALE,        0.0,
						 nodeWidth - 0.2 * BhParams.NODE_SCALE,  0.0,
						 nodeWidth,                                 0.0 + 0.2 * BhParams.NODE_SCALE,
						 nodeWidth,                                 nodeHeight - 0.2 * BhParams.NODE_SCALE,
						 nodeWidth - 0.2 * BhParams.NODE_SCALE,  nodeHeight,
					 	 0.0 + 0.2 * BhParams.NODE_SCALE,        nodeHeight,
					 	 0.0,                                       nodeHeight - 0.2 * BhParams.NODE_SCALE));
				}
				else if (viewStyle.connectorPos == CNCTR_POS.TOP) {
					//ノードの形はここの値で決まる
					bodyVertices = new ArrayList<>(Arrays.asList(
						nodeWidth - 0.2 * BhParams.NODE_SCALE, 0.0,
						nodeWidth,                                0.0 + 0.2 * BhParams.NODE_SCALE,
						nodeWidth,                                nodeHeight - 0.2 * BhParams.NODE_SCALE,
						nodeWidth - 0.2 * BhParams.NODE_SCALE, nodeHeight,
						0.0 + 0.2 * BhParams.NODE_SCALE,       nodeHeight,
						0.0,                                      nodeHeight - 0.2 * BhParams.NODE_SCALE,
						0.0,                                      0.0 + 0.2 * BhParams.NODE_SCALE,
						0.0 + 0.2 * BhParams.NODE_SCALE,       0.0));
				}
			}

			if (bodyVertices == null)
				bodyVertices = new ArrayList<>();

			bodyVertices.addAll(connectorPart.createVertices());	//コネクタ部分の頂点位置計算
			return bodyVertices;
		}

		/**
		 * ノードの形状を更新する関数をセットする
		 * */
		public void setFuncUpdateStyle(Consumer<BhNodeViewGroup> updateStyleFunc) {
			this.updateStyleFunc = updateStyleFunc;
		}

		/**
		 * ノードの形状を更新する
		 * @param child 形状が変わった子ノード
		 * */
		public void updateStyle(BhNodeViewGroup child) {
			updateStyleFunc.accept(child);
			//BhNoteSelectionView のBhNode配置に必要
			Point2D wholeBodySize = getRegionManager().getBodyAndOuterSize(true);
			BhNodeView.this.setMaxSize(0.0, 0.0);
			if (BhNodeView.this.heightProperty().get() != wholeBodySize.y)
				BhNodeView.this.setHeight(wholeBodySize.y);
		}
	}

	/**
	 * このノードの画面上での領域に関する処理を行うクラス
	 * */
	public class ViewRegionManager {

		private final QuadTreeRectangle wholeBodyRange = new QuadTreeRectangle(0.0, 0.0, 0.0, 0.0, BhNodeView.this);		//!< ノード全体の範囲
		private final QuadTreeRectangle connectorPartRange = new QuadTreeRectangle(0.0, 0.0, 0.0, 0.0, BhNodeView.this);	//!< コネクタ部分の範囲

		/**
		 * コネクタ部分同士がこのビューに重なっているビューに対応するモデルを探す
		 * @return コネクタ部分同士がこのビューに重なっているビューに対応するモデルのリスト
		 * */
		public ArrayList<BhNode> searchOverlappedModel() {

			ArrayList<BhNode> overlappedList = new ArrayList<>();
			connectorPartRange.searchOverlappedRects().forEach(
				ractangle -> {
					overlappedList.add(ractangle.<BhNodeView>getDrawedObj().model);
				});
			return overlappedList;
		}

		/**
		 * ボディとコネクタ部分の領域を保持するQuadTreeRectangleを返す
		 * @return ボディとコネクタ部分の領域を保持するQuadTreeRectangleオブジェクトのペア
		 * */
		public Pair<QuadTreeRectangle, QuadTreeRectangle> getRegion() {
			return new Pair<>(wholeBodyRange, connectorPartRange);
		}

		public void updateBodyPos(double upperLeftX, double upperLeftY, double lowerRightX, double lowerRightY) {
			wholeBodyRange.updatePos(upperLeftX, upperLeftY, lowerRightX, lowerRightY);
		}

		public void updateConnectorPos(double upperLeftX, double upperLeftY, double lowerRightX, double lowerRightY) {
			connectorPartRange.updatePos(upperLeftX, upperLeftY, lowerRightX, lowerRightY);
		}

		/**
		 * 4分木空間からこのView以下の領域判定オブジェクトを消す
		 */
		public void removeQtRectable() {
			accept(view -> {
				QuadTreeManager.removeQuadTreeObj(view.getRegionManager().connectorPartRange);
			});
		}

		/**
		 * ボディ部分に外部ノードを加えた大きさを返す
		 * @param includeCnctr コネクタ部分を含む大きさを返す場合true
		 * @return 描画ノードの大きさ
		 * */
		public Point2D getBodyAndOuterSize(boolean includeCnctr) {
			return viewStyle.getBodyAndOuterSize(includeCnctr);
		}

		/**
		 * 外部ノードを覗くボディ部分の大きさを返す
		 * @param includeCnctr コネクタ部分を含む大きさを返す場合true
		 * @return 描画ノードの大きさ
		 * */
		public Point2D getBodySize(boolean includeCnctr) {
			return viewStyle.getBodySize(includeCnctr);
		}
	}

	/**
	 * View の木構造を操作するクラス
	 * */
	public class ViewTreeManager {

		/**
		 * NodeView の親をセットする
		 * @param parentGroup このBhNodeViewを保持するBhNodeViewGroup
		 * */
		public void setParentGroup(BhNodeViewGroup parentGroup) {
			parent = parentGroup;
		}

		/**
		 * NodeView の親を取得する
		 * @return このビューの親となるビュー.  <br>
		 * このビューがルートノードの場合は null を返す
		 * */
		public BhNodeView getParentView() {

			if (parent == null)
				return null;

			return parent.getParentView();
		}

		/**
		 * BhNodeViewの木構造上で, このノードを引数のノードと入れ替える. <br>
		 * GUIツリーからは取り除かない.
		 * @param newNode
		 */
		public void replace(BhNodeView newNode) {
			parent.replace(BhNodeView.this, newNode);
		}

		/**
		 * このBhNodeView をGUIツリーから取り除く
		 *  BhNodeView の木構造からは取り除かない
		 */
		public void removeFromGUITree() {
			//この関数を呼ぶときには, BhNodeViewGroupとの親子関係は切れているので, キャストして呼ぶ
			((BhNodeViewGroup)BhNodeView.this.getParent()).removeFromGUITree(BhNodeView.this);
		}
	}

	/**
	 * 位置変更, 取得操作を行うクラス
	 * */
	public class PositionManager {

		private BiConsumer<Double, Double> updateAbsPosFunc = this::defaultUpdateAbsPos;

		/**
		 * ノードの親からの相対位置を指定する
		 * @param posX 親ノードからの相対位置 X
		 * @param posY 親ノードからの相対位置 Y
		 * */
		public final void setRelativePosFromParent(double posX, double posY) {
			BhNodeView.this.setTranslateX(posX);
			BhNodeView.this.setTranslateY(posY);
		}

		/**
		 * ノードの親からの相対位置を取得する
		 * @return ノードの親からの相対位置
		 * */
		public final Point2D getRelativePosFromParent() {
			return new Point2D(BhNodeView.this.getTranslateX(), BhNodeView.this.getTranslateY());
		}

		/**
		 * ワークスペース上での位置を返す
		 * @return ワークスペース上での位置
		 * */
		public Point2D getPosOnWorkspace() {
			return getRelativePos(null, BhNodeView.this);
		}

		/**
		 * このノードの絶対位置を更新する
		 * @param posX 本体部分左上のX位置
		 * @param posY 本体部分左上のY位置
		 * */
		public void updateAbsPos(double posX, double posY) {
			updateAbsPosFunc.accept(posX, posY);
		}

		/**
		 * ノードをGUI上で動かす
		 * @param diffX X方向移動量
		 * @param diffY Y方向移動量
		 * @return 移動後の新しい位置
		 */
		public Point2D move(double diffX, double diffY) {

			Point2D curRelPos = getRelativePosFromParent();
			Point2D posOnWS = getPosOnWorkspace();
			Pair<Double, Double> wsSize = MsgTransporter.INSTANCE.sendMessage(BhMsg.GET_WORKSPACE_SIZE, model.getWorkspace()).doublePair;
			double wsWidth = wsSize._1;
			double wsHeight = wsSize._2;
			double newDiffX = calcNewDiff(wsWidth, posOnWS.x, diffX);
			double newDiffY = calcNewDiff(wsHeight, posOnWS.y, diffY);
			double newPosX = curRelPos.x + newDiffX;
			double newPosY = curRelPos.y + newDiffY;
			setRelativePosFromParent(newPosX, newPosY);	//GUI上での移動
			return getPosOnWorkspace();
		}

		/**
		 * ワークスペースの範囲を元に新しい移動量を算出する
		 * @param targetRange 新しい位置を計算する際に使う範囲
		 * @param curPos 現在のWS上での位置
		 * @param diff 移動量
		 * @return 新しい移動量
		 */
		private double calcNewDiff(double targetRange, double curPos, double diff) {

			boolean curPosIsInTargetRange = (0 < curPos) && (curPos < targetRange);
			if (curPosIsInTargetRange) {
				double newPos = curPos + diff;
				boolean newPosIsInTargetRange = (0 < newPos) && (newPos < targetRange);
				if (!newPosIsInTargetRange) {	//現在範囲内に居て移動後に範囲外に居る場合, 移動させない
					if (newPos < 0)
						return -curPos + 1.0;
					else
						return targetRange - curPos - 1.0;
				}
			}
			return diff;
		}

		/**
		 * このノードの絶対位置を更新する
		 * @param posX 本体部分左上のX位置
		 * @param posY 本体部分左上のY位置
		 * */
		public void defaultUpdateAbsPos(double posX, double posY) {

			Point2D bodySize = getRegionManager().getBodySize(false);
			double bodyUpperLeftX = posX;
			double bodyUpperLeftY = posY;
			double bodyLowerRightX = posX + bodySize.x;
			double bodyLowerRightY = posY + bodySize.y;
			double cnctrUpperLeftX = 0.0;
			double cnctrUpperLeftY = 0.0;
			double cnctrLowerRightX = 0.0;
			double cnctrLowerRightY = 0.0;

			double boundsWidth = viewStyle.connectorWidth * viewStyle.connectorBoundsRate;
			double boundsHeight = viewStyle.connectorHeight * viewStyle.connectorBoundsRate;

			if (viewStyle.connectorPos == CNCTR_POS.LEFT) {
				cnctrUpperLeftX = posX - (boundsWidth + viewStyle.connectorWidth) / 2.0;
				cnctrUpperLeftY = posY - (boundsHeight - viewStyle.connectorHeight) / 2.0 + viewStyle.connectorShift;
				cnctrLowerRightX = cnctrUpperLeftX + boundsWidth;
				cnctrLowerRightY = cnctrUpperLeftY + boundsHeight;
			}
			else if (viewStyle.connectorPos == CNCTR_POS.TOP) {
				cnctrUpperLeftX = posX - (boundsWidth - viewStyle.connectorWidth) / 2.0 + viewStyle.connectorShift;
				cnctrUpperLeftY = posY - (boundsHeight + viewStyle.connectorHeight) / 2.0;
				cnctrLowerRightX = cnctrUpperLeftX + boundsWidth;
				cnctrLowerRightY = cnctrUpperLeftY + boundsHeight;
			}
			viewRegionManager.updateBodyPos(bodyUpperLeftX, bodyUpperLeftY, bodyLowerRightX, bodyLowerRightY);
			viewRegionManager.updateConnectorPos(cnctrUpperLeftX, cnctrUpperLeftY, cnctrLowerRightX, cnctrLowerRightY);
		}

		/**
		 * 絶対位置更新用関数をセットする
		 * */
		void setFuncUpdateAbsPos(BiConsumer<Double, Double> updateAbsPosFunc) {
			this.updateAbsPosFunc = updateAbsPosFunc;
		}
	}

	public class EventManager {

		/**
		 * マウス押下時のイベントハンドラを登録する
		 * @param handler 登録するイベントハンドラ
		 * */
		public void setOnMousePressedHandler(EventHandler<? super MouseEvent> handler) {
			nodeShape.setOnMousePressed(handler);
		}

		/**
		 * マウスドラッグ中のイベントハンドラを登録する
		 * @param handler 登録するイベントハンドラ
		 * */
		public void setOnMouseDraggedHandler(EventHandler<? super MouseEvent> handler) {
			nodeShape.setOnMouseDragged(handler);
		}

		/**
		 * マウスドラッグを検出したときのイベントハンドラを登録する
		 * @param handler 登録するイベントハンドラ
		 * */
		public void setOnDragDetectedHandler(EventHandler<? super MouseEvent> handler) {
			nodeShape.setOnDragDetected(handler);
		}

		/**
		 * マウスボタンを離した時のイベントハンドラを登録する
		 * @param handler 登録するイベントハンドラ
		 * */
		public void setOnMouseReleasedHandler(EventHandler<? super MouseEvent> handler) {
			nodeShape.setOnMouseReleased(handler);
		}

		/**
		 * この view のマウスイベント受け取り対象にイベントを伝える
		 * @param event イベント受信対象に伝えるイベント
		 * */
		public void propagateEvent(Event event) {
			nodeShape.fireEvent(event);
		}
	}

	/**
	 * 親子関係にある2つのノードの相対距離を測る
	 * @param base 基点となるNodeオブジェクト<br> null を入れるとtarget が居るworkspaceからの相対距離が得られる
	 * @param target 基点からの距離を測るオブジェクト
	 * @return target - base で算出される距離
	 * */
	public static Point2D getRelativePos(Node base, Node target) {

		Point2D relativePos = new Point2D(0.0, 0.0);
		Node parent = target;
		while (parent != base && !BhParams.Fxml.ID_WS_PANE.equals(parent.getId())) {
			relativePos.x += parent.getTranslateX();
			relativePos.y += parent.getTranslateY();
			parent = parent.getParent();

			if (parent == null)
				break;
		}
		return relativePos;
	}
}













