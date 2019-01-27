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
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.Single;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.configfilereader.FXMLCollector;
import net.seapanda.bunnyhop.model.TextNode;

/**
 * コンボボックスを入力フォームに持つビュー
 * @author K.Koike
 */
public class ComboBoxNodeView extends BhNodeView implements ImitationCreator {

	private ComboBox<SelectableItem> comboBox = new ComboBox<>();
	private final TextNode model;
	private Button imitCreateImitBtn;	//!< イミテーション作成ボタン
	private final ListCell<SelectableItem> buttonCell = new ComboBoxNodeListCell();
	private final Single<Boolean> dragged = new Single<>(false);

	public ComboBoxNodeView(TextNode model, BhNodeViewStyle viewStyle) {
		super(viewStyle, model);
		this.model = model;
	}

	/**
	 * 初期化する
	 * @param isTemplate ノード選択パネルに表示されるノードであった場合true
	 */
	public void init(boolean isTemplate) {

		initialize();
		String inputControlFileName = BhNodeViewStyle.nodeID_inputControlFileName.get(model.getID());
		if (inputControlFileName != null) {
			Path filePath = FXMLCollector.INSTANCE.getFilePath(inputControlFileName);
			try {
				FXMLLoader loader = new FXMLLoader(filePath.toUri().toURL());
				comboBox = (ComboBox<SelectableItem>)loader.load();
			} catch (IOException | ClassCastException e) {
				MsgPrinter.INSTANCE.errMsgForDebug("failed to initialize " + ComboBoxNodeView.class.getSimpleName() + "\n" + e.toString());
			}
		}
		getChildren().add(comboBox);
		comboBox.addEventFilter(MouseEvent.ANY, event -> {
			getEventManager().propagateEvent(event);
			if (isTemplate || dragged.content)
				event.consume();

			if (event.getEventType().equals(MouseEvent.DRAG_DETECTED))
				dragged.content = true;
			else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
				dragged.content = false;
		});

		if (model.getImitationInfo().canCreateImitManually) {
			imitCreateImitBtn = loadButton(BhParams.Path.IMIT_BUTTON_FXML, viewStyle.imitation);
			if (imitCreateImitBtn != null)
				getChildren().add(imitCreateImitBtn);
		}
		initStyle(viewStyle);
		setFuncs(this::updateStyleFunc, null);

		comboBox.setButtonCell(buttonCell);
		comboBox.setOnShowing(event -> {
			List<String> itemTextList = new ArrayList<>();
			comboBox.getItems().forEach(item -> itemTextList.add(item.getViewText()));
			double maxWidth = calcMaxStrWidth(itemTextList, buttonCell.fontProperty().get());
			ScrollBar scrollBar = getVerticalScrollbar();
			if (scrollBar != null)
				maxWidth += scrollBar.getWidth();
			buttonCell.getListView().setPrefWidth(maxWidth);
		});
	}

	private void initStyle(BhNodeViewStyle viewStyle) {
		comboBox.setTranslateX(viewStyle.leftMargin);
		comboBox.setTranslateY(viewStyle.topMargin);
		comboBox.getStyleClass().add(viewStyle.comboBox.cssClass);
		comboBox.heightProperty().addListener(observable -> getAppearanceManager().updateStyle(null));
		comboBox.widthProperty().addListener(observable -> getAppearanceManager().updateStyle(null));
		if (!comboBox.getItems().isEmpty()) {
			comboBox.setValue(comboBox.getItems().get(0));
		}
		getAppearanceManager().addCssClass(BhParams.CSS.CALSS_COMBO_BOX_NODE);
	}

	/**
	 * このビューのモデルであるBhNodeを取得する
	 * @return このビューのモデルであるBhNode
	 */
	@Override
	public TextNode getModel() {
		return model;
	}

	/**
	 * コンボボックスのアイテム変化時のイベントハンドラを登録する
	 * @param handler コンボボックスのアイテム変化時のイベントハンドラ
	 * */
	public void setTextChangeListener(ChangeListener<SelectableItem> handler) {
		comboBox.valueProperty().addListener(handler);
	}

	/**
	 * モデルの構造を表示する
	 * @param depth 表示インデント数
	 * */
	@Override
	public void show(int depth) {

		try {
			MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<TextNodeView" + ">   " + this.hashCode());
			MsgPrinter.INSTANCE.msgForDebug(indent(depth + 1) + "<content" + ">   " + comboBox.getValue());
		}
		catch (Exception e) {
			MsgPrinter.INSTANCE.msgForDebug("TextNodeView show exception " + e);
		}
	}

	/**
	 * ノードの大きさや見た目を変える関数
	 * */
	private void updateStyleFunc(BhNodeViewGroup child) {

		viewStyle.width = comboBox.getWidth();
		viewStyle.height = comboBox.getHeight();

		getAppearanceManager().updatePolygonShape(viewStyle.drawBody);
		if (parent != null) {
			parent.updateStyle();
		}
		else {
			Point2D pos = getPositionManager().getPosOnWorkspace();	//workspace からの相対位置を計算
			getPositionManager().updateAbsPos(pos.x, pos.y);
		}
	}

	/**
	 * 現在選択中のコンボボックスのアイテムを取得する
	 * @return 現在のコンボボックスのテキスト
	 */
	public SelectableItem getItem() {
		return comboBox.getValue();
	}

	/**
	 * 引数で指定したモデルテキストを持つアイテムを取得する
	 * @param modelText このテキストをモデルテキストとして持つコンボボックスのアイテムを見つける
	 * @return 引数で指定したモデルテキストを持つアイテム
	 */
	public Optional<SelectableItem> getItemByModelText(String modelText) {

		for (SelectableItem item : comboBox.getItems()) {
			if (item.getModelText().equals(modelText))
				return Optional.of(item);
		}
		return Optional.empty();
	}

	/**
	 * コンボボックスのアイテムを設定する
	 * @param text 設定するテキスト
	 */
	public void setItem(SelectableItem item) {
		Platform.runLater(() -> comboBox.setValue(item));
	}

	/**
	 * コンボボックスの垂直スクロールバーを取得する
	 */
	private ScrollBar getVerticalScrollbar() {

		ScrollBar result = null;
		for (Node node : buttonCell.getListView().lookupAll(".scroll-bar")) {
			if (node instanceof ScrollBar) {
				ScrollBar bar = (ScrollBar)node;
				if (bar.getOrientation().equals(Orientation.VERTICAL)) {
					result = bar;
				}
			}
		}
		return result;
	}

	/**
	 * 引数で指定した文字列のリストの最大幅を求める
	 * @param items 文字列のリスト
	 * @param font 文字列の幅を求める際のフォント
	 * @return 引数で指定した文字列のリストの最大幅
	 */
	private double calcMaxStrWidth(List<String> strList, Font font) {

		double width = 0.0;
		for (String str : strList) {
			double strWidth = Util.calcStrWidth(str, font);
			width = Math.max(width, strWidth);
		}
		return width;
	}

	@Override
	public Button imitCreateButton() {
		return imitCreateImitBtn;
	}

	/**
	 * BhNode カテゴリのView.  BhNodeCategoryとの結びつきは動的に変わる
	 * */
	public class ComboBoxNodeListCell extends ListCell<SelectableItem> {

		SelectableItem item;
		public ComboBoxNodeListCell() {}

		@Override
		protected void updateItem(SelectableItem item, boolean empty) {

			super.updateItem(item, empty);
			this.item = item;
			if (!empty) {
				setText(item.getViewText());
				double width = Util.calcStrWidth(item.getViewText(), getFont());
				getListView().setPrefWidth(width);
			}
		}
	}
}
