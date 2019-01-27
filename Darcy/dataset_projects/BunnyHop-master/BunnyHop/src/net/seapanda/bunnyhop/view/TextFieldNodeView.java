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

import java.util.function.Function;
import java.util.stream.Stream;
import java.io.IOException;
import java.nio.file.Path;
import javafx.scene.control.TextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.TextNode;
import net.seapanda.bunnyhop.configfilereader.FXMLCollector;

/**
 * テキストフィールドを入力フォームに持つビュー
 * @author K.Koike
 */
public class TextFieldNodeView extends BhNodeView implements ImitationCreator {

	private TextField textField = new TextField();
	private final TextNode model;
	private Button imitCreateImitBtn;	//!< イミテーション作成ボタン

	public TextFieldNodeView(TextNode model, BhNodeViewStyle viewStyle) {
		super(viewStyle, model);
		this.model = model;
	}

	/**
	 * GUI部品の読み込みと初期化を行う
	 * @param isTemplate ノード選択パネルに表示されるノードであった場合true
	 */
	public void init(boolean isTemplate) {
		
		initialize();
		String inputControlFileName = BhNodeViewStyle.nodeID_inputControlFileName.get(model.getID());
		if (inputControlFileName != null) {
			Path filePath = FXMLCollector.INSTANCE.getFilePath(inputControlFileName);
			try {
				FXMLLoader loader = new FXMLLoader(filePath.toUri().toURL());
				textField = (TextField)loader.load();
			} catch (IOException | ClassCastException e) {
				MsgPrinter.INSTANCE.errMsgForDebug("failed to initialize " + TextFieldNodeView.class.getSimpleName() + "\n" + e.toString());
			}
		}
		getChildren().add(textField);
		textField.addEventFilter(MouseEvent.ANY, event -> {
			getEventManager().propagateEvent(event);
			if (isTemplate)
				event.consume();
		});
				
		if (model.getImitationInfo().canCreateImitManually) {
			imitCreateImitBtn = loadButton(BhParams.Path.IMIT_BUTTON_FXML, viewStyle.imitation);
			if (imitCreateImitBtn != null)
				getChildren().add(imitCreateImitBtn);
		}		
		initStyle(viewStyle);
		setFuncs(this::updateStyleFunc, null);
	}
	
	private void initStyle(BhNodeViewStyle viewStyle) {
		
		textField.setTranslateX(viewStyle.leftMargin);
		textField.setTranslateY(viewStyle.topMargin);
		textField.getStyleClass().add(viewStyle.textField.cssClass);		
		textField.heightProperty().addListener(observable -> getAppearanceManager().updateStyle(null));
		textField.widthProperty().addListener(observable -> getAppearanceManager().updateStyle(null));
		textField.fontProperty().addListener(observable -> {
			String text = textField.getText();
			setText(text + " "); 
			setText(text);});
		getAppearanceManager().addCssClass(BhParams.CSS.CLASS_TEXT_FIELD_NODE);
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
	 * テキスト変更時のイベントハンドラを登録する
	 * @param checkFormatFunc 入力された文字列の形式が正しいかどうか判断する関数 (テキスト変更時のイベントハンドラから呼び出す)
	 * */
	public void setTextChangeListener(Function<String, Boolean> checkFormatFunc) {

		String wsMargine = Stream.iterate(" ", ws -> ws).limit((long)viewStyle.textField.whiteSpaceMargin).reduce("", String::concat);
		String minWidthWS = Stream.iterate(" ", ws -> ws).limit((long)viewStyle.textField.minWhiteSpace).reduce("", String::concat);

		// テキストの長さに応じてTextField の長さが変わるように
		textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			double newWidth = Util.calcStrWidth(newValue + wsMargine, textField.getFont());
			double minWidth = Util.calcStrWidth(minWidthWS, textField.getFont());
			newWidth = Math.max(newWidth, minWidth);	//最低限の長さは確保する
			newWidth += textField.getPadding().getRight() + textField.getPadding().getLeft();
			textField.setPrefWidth(newWidth);
			
			boolean acceptable = checkFormatFunc.apply(newValue);
			if (acceptable)
				textField.pseudoClassStateChanged(PseudoClass.getPseudoClass(BhParams.CSS.PSEUDO_BHNODE), false);
			else
				textField.pseudoClassStateChanged(PseudoClass.getPseudoClass(BhParams.CSS.PSEUDO_BHNODE), true);
		});
	}

	/**
	 * テキストフィールドのカーソルon/off時のイベントハンドラを登録する
	 * @param changeFocusFunc テキストフィールドのカーソルon/off時のイベントハンドラ
	 * */
	public void setObservableListener(ChangeListener<? super Boolean> changeFocusFunc) {
		textField.focusedProperty().addListener(changeFocusFunc);
	}

	/**
	 * モデルの構造を表示する
	 * @param depth 表示インデント数
	 * */
	@Override
	public void show(int depth) {
		MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<TextNodeView" + ">   " + this.hashCode());
		MsgPrinter.INSTANCE.msgForDebug(indent(depth + 1) + "<content" + ">   " + textField.getText());
	}

	/**
	 * ノードの大きさや見た目を変える関数
	 * */
	private void updateStyleFunc(BhNodeViewGroup child) {

		viewStyle.width = textField.getWidth();
		viewStyle.height = textField.getHeight();
		getAppearanceManager().updatePolygonShape(viewStyle.drawBody);
		if (parent != null) {
			parent.updateStyle();
		}
		else {
			Point2D pos = getPositionManager().getPosOnWorkspace();	//workspace からの相対位置を計算
			getPositionManager().updateAbsPos(pos.x, pos.y);
		}
	}

	public String getText() {
		return textField.getText();
	}

	public void setText(String text) {
		textField.setText(text);
	}

	/**
	 * テキストフィールドが編集可能かどうかをセットする
	 * @param editable テキストフィールドが編集可能なときtrue
	 * */
	public void setEditable(boolean editable) {
		textField.setEditable(editable);
	}
	
	/**
	 * テキストフィールドが編集可能かどうかチェックする
	 * @return テキストフィールドが編集可能な場合 true
	 * */
	public boolean getEditable() {
		return textField.editableProperty().getValue();
	}
	
	@Override
	public Button imitCreateButton() {
		return imitCreateImitBtn;
	}
}
