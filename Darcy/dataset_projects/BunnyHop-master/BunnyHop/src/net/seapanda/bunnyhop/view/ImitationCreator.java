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

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.configfilereader.FXMLCollector;
import net.seapanda.bunnyhop.model.TextNode;
import net.seapanda.bunnyhop.model.connective.ConnectiveNode;
import net.seapanda.bunnyhop.modelhandler.BhNodeHandler;
import net.seapanda.bunnyhop.modelprocessor.ImitationBuilder;
import net.seapanda.bunnyhop.root.BunnyHop;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 *イミテーション作成機能を持つノードビューであることを示すインタフェース
 * @author K.Koike
 * */
public interface ImitationCreator {

	/**
	 *イミテーションノード作成時にクリックするボタンを返す
	 * @return イミテーションノード作成時にクリックするボタン
	 * */
	public Button imitCreateButton();

	/**
	 * このクラス以外からの呼び出し禁止
	 * @param model 作成するイミテーションのオリジナルノード
	 */
	default void imitHandler(net.seapanda.bunnyhop.model.imitation.Imitatable model) {

		UserOperationCommand userOpeCmd = new UserOperationCommand();
		Point2D pos = Util.getPosOnWS(model);
		double x = pos.x + BhParams.REPLACED_NODE_POS;
		double y = pos.y + BhParams.REPLACED_NODE_POS;
		ImitationBuilder imitBuilder = new ImitationBuilder(userOpeCmd, true);
		model.accept(imitBuilder);
		BhNodeHandler.INSTANCE.addRootNode(model.getWorkspace(), imitBuilder.getTopImitation(), x, y, userOpeCmd);
		BunnyHop.INSTANCE.pushUserOpeCmd(userOpeCmd);
	}

	/**
	 * イミテーションノード作成ボタン操作時のイベントを追加する
	 * @param model 作成するイミテーションのオリジナルノード
	 */
	default void setCreateImitHandler(TextNode model) {

		if (imitCreateButton() != null) {
			imitCreateButton().setOnAction(event -> {
				imitHandler(model);
				event.consume();
			});
		}
	}

	/**
	 * イミテーションノード作成ボタン操作時のイベントを追加する
	 * @param model 作成するイミテーションのオリジナルノード
	 */
	default void setCreateImitHandler(ConnectiveNode model) {

		if (imitCreateButton() != null) {
			imitCreateButton().setOnAction((event) -> {
				imitHandler(model);
				event.consume();
			});
		}
	}

	/**
	 * イミテーションノード作成ボタンのスタイルを指定する
	 * @param style イミテーションノード作成ボタンのスタイル情報が格納されたオブジェクト
	 */
	default void setBtnStyle(BhNodeViewStyle.Imitation style) {

		Button imitCreateButton = imitCreateButton();
		if (imitCreateButton != null) {
			imitCreateButton.setTranslateX(style.buttonPosX);
			imitCreateButton.setTranslateY(style.buttonPosY);
			imitCreateButton.getStyleClass().add(style.cssClass);
		}
	}

	/**
	 * FXML ファイルからイミテーション作成ボタンをロードする
	 * @param fileName ボタンをロードするFXMLファイル名
	 * @param buttonStyle ボタンに適用するスタイル
	 * @return ファイルからロードしたイミテーション作成ボタン
	 */
	default Button loadButton(String fileName, BhNodeViewStyle.Imitation buttonStyle) {

		Button imitCreateImitBtn = null;
		Path filePath = FXMLCollector.INSTANCE.getFilePath(fileName);
		try {
			FXMLLoader loader = new FXMLLoader(filePath.toUri().toURL());
			imitCreateImitBtn = (Button)loader.load();
			setBtnStyle(buttonStyle);
		} catch (IOException | ClassCastException e) {
			MsgPrinter.INSTANCE.errMsgForDebug(ImitationCreator.class.getSimpleName() + ".loadButton\n" + e.toString());
		}
		return imitCreateImitBtn;
	}
}
