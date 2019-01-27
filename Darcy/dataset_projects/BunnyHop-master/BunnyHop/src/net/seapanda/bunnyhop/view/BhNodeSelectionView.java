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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.configfilereader.FXMLCollector;

/**
 * ノードカテゴリ選択時にワークスペース上に現れるBhNode 選択パネル
 * @author K.Koike
 * */
public class BhNodeSelectionView extends ScrollPane {

	@FXML Pane nodeSelectionPanel;	//VBoxにしない
	@FXML Pane nodeSelectionPanelWrapper;
	@FXML ScrollPane nodeSelectionPanelBase;
	private int zoomLevel = 0;
	private boolean nodeHeightHasChanged = true;	//!< ノードを並べた後に, 表示するノードの高さが変わった場合true
	
	public BhNodeSelectionView() {}

	/**
	 * GUI初期化
	 * @param categoryName このビューに関連付けられたBhNodeリストのカテゴリ名
	 * @param cssClass ビューに適用するcssクラス名
	 * @param categoryListView このビューを保持しているカテゴリリストのビュー
	 */
	public void init(String categoryName, String cssClass, BhNodeCategoryListView categoryListView) {
		
		try {
			Path filePath = FXMLCollector.INSTANCE.getFilePath(BhParams.Path.NODE_SELECTION_PANEL_FXML);
			FXMLLoader loader = new FXMLLoader(filePath.toUri().toURL());
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("failed to initialize "  + BhNodeSelectionView.class.getSimpleName());
		}
		
		nodeSelectionPanel.getTransforms().add(new Scale());
		
		//拡大縮小処理
		this.addEventFilter(ScrollEvent.ANY, event -> {
			
			if (event.isControlDown()) {
				event.consume();
				boolean zoomIn = event.getDeltaY() >= 0;
				categoryListView.zoomAll(zoomIn);
			}
		});
		getStyleClass().add(cssClass);
		nodeSelectionPanel.getStyleClass().add(cssClass);
		
	}
	
	/**
	 * テンプレートリストに表示するBhNode のビューを追加する
	 * @param view テンプレートリストに表示するBhNodeのビュー
	 * */
	public void addBhNodeView(BhNodeView view) {

		nodeSelectionPanel.getChildren().add(view);
		view.heightProperty().addListener((observable, oldVal, newVal) -> {
			nodeHeightHasChanged = true;
		});
	}
	
	/**
	 * ノード選択ビューのズーム処理を行う
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
		nodeSelectionPanel.getTransforms().clear();
		nodeSelectionPanel.getTransforms().add(scale);
		adjustWrapperSize(nodeSelectionPanel.getWidth(), nodeSelectionPanel.getHeight());
	}
	
	/**
	 * 表示するノードを並べる
	 */
	public void arrange() {
		
		if (!nodeHeightHasChanged)
			return;
		
		double panelWidth = 0.0;
		double panelHeight = 0.0;
		double offset = 0.0;
		
		for (int i = 0; i < nodeSelectionPanel.getChildren().size(); ++i) {
			BhNodeView nodeToShift = (BhNodeView)nodeSelectionPanel.getChildren().get(i);
			Point2D wholeBodySize = nodeToShift.getRegionManager().getBodyAndOuterSize(true);
			Point2D bodySize = nodeToShift.getRegionManager().getBodyAndOuterSize(false);
			double upperCnctrHeight = wholeBodySize.y - bodySize.y;
			nodeToShift.setTranslateY(offset + upperCnctrHeight);
			offset += wholeBodySize.y + BhParams.BHNODE_SPACE_ON_SELECTION_PANEL;
			panelWidth = Math.max(panelWidth, wholeBodySize.x);
		}
		
		panelHeight = (offset - BhParams.BHNODE_SPACE_ON_SELECTION_PANEL) 
					+ nodeSelectionPanel.getPadding().getTop() 
					+ nodeSelectionPanel.getPadding().getBottom();
		panelWidth += nodeSelectionPanel.getPadding().getRight() + nodeSelectionPanel.getPadding().getLeft();
		nodeSelectionPanel.setMinSize(panelWidth, panelHeight);
		nodeHeightHasChanged = false;
		adjustWrapperSize(panelWidth, panelHeight);	//バインディングではなく, ここでこのメソッドを呼ばないとスクロールバーの稼働域が変わらない
	}
	
	/**
	 * スクロールバーの可動域が変わるようにノード選択パネルのラッパーのサイズを変更する
	 * @param panelWidth ノード選択パネルの幅
	 * @param panelHeight ノード選択パネルの高さ
	 */
	private void adjustWrapperSize(double panelWidth, double panelHeight) {
		
		double wrapperSizeX = panelWidth * nodeSelectionPanel.getTransforms().get(0).getMxx();
		double wrapperSizeY = panelHeight * nodeSelectionPanel.getTransforms().get(0).getMyy();
		nodeSelectionPanelWrapper.setMinSize(wrapperSizeX, wrapperSizeY);	//スクロール時にスクロールバーの可動域が変わるようにする
		nodeSelectionPanelWrapper.setMaxSize(wrapperSizeX, wrapperSizeY);
		double maxWidth = wrapperSizeX + nodeSelectionPanelBase.getPadding().getLeft() + nodeSelectionPanelBase.getPadding().getRight();		
		if (nodeSelectionPanelBase.getScene() != null)
			maxWidth = Math.min(maxWidth, nodeSelectionPanelBase.getScene().getWidth() * 0.5);
		nodeSelectionPanelBase.setMaxWidth(maxWidth);
	}
}











