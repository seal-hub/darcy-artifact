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

import java.io.File;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.WorkspaceSet;
import net.seapanda.bunnyhop.root.BunnyHop;

/**
 * メニューバーのコントローラクラス
 * @author K.Koike
 */
public class MenuBarController {

	@FXML private MenuBar menuBar;
	@FXML private MenuItem loadMenu;
	@FXML private MenuItem saveMenu;
	@FXML private MenuItem saveAsMenu;
	@FXML private MenuItem aboutBunnyHop;
	private File currentSaveFile;	//!< 現在保存対象になっているファイル

	/**
	 * 初期化する
	 * @param wss ワークスペースセット
	 */
	public void init(WorkspaceSet wss) {

		setSaveAsHandler(wss);
		setSaveHandler(wss);
		setLoadHandler(wss);
		setAboutBunnyHopHandler();
	}

	/**
	 * セーブ(新規保存)ボタンのイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setSaveAsHandler(WorkspaceSet wss) {
		saveAsMenu.setOnAction(action -> {
			saveAs(wss);
		});
	}

	/**
	 * 新規保存セーブを行う
	 * @param wss 保存時に操作するワークスペースセット
	 * @return 保存した場合true
	 */
	private boolean saveAs(WorkspaceSet wss) {

		if (wss.getWorkspaceList().isEmpty()) {
			MsgPrinter.INSTANCE.alert(
				Alert.AlertType.INFORMATION,
				"名前を付けて保存",
				null,
				"保存すべきワークスペースがありません");
			return false;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("名前を付けて保存");
		fileChooser.setInitialDirectory(getInitDir());
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("BunnyHop Files", "*.bnh"),
			new FileChooser.ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
		boolean success = false;
		if (selectedFile != null) {
			success = wss.save(selectedFile);
		}
		if (success) {
			currentSaveFile = selectedFile;
		}
		return success;
	}

	/**
	 * セーブ(上書き保存)ボタンのイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setSaveHandler(WorkspaceSet wss) {
		saveMenu.setOnAction(action -> {
			save(wss);
		});
	}

	/**
	 * 上書きセーブを行う
	 * @param wss 保存時に操作するワークスペースセット
	 * @return 保存した場合true
	 */
	public boolean save(WorkspaceSet wss) {

		if (wss.getWorkspaceList().isEmpty()) {
			MsgPrinter.INSTANCE.alert(
				Alert.AlertType.INFORMATION,
				"上書き保存",
				null,
				"保存すべきワークスペースがありません");
			return false;
		}

		boolean fileExists = false;
		if (currentSaveFile != null)
			fileExists = currentSaveFile.exists();

		if (fileExists) {
			return wss.save(currentSaveFile);
		}
		else {
			return saveAs(wss);	//保存対象のファイルが無い場合, 名前をつけて保存
		}
	}

	/**
	 * ロードボタンのイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setLoadHandler(WorkspaceSet wss) {

		loadMenu.setOnAction(action -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("開く");
			fileChooser.setInitialDirectory(getInitDir());
			fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("BunnyHop Files", "*.bnh"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
			File selectedFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
			boolean success = false;
			if (selectedFile != null)
				success = wss.load(selectedFile, this::askIfClearOldWs);

			if (success) {
				currentSaveFile = selectedFile;
			}
			else if (selectedFile != null){
				String fileName = selectedFile.getPath();
				MsgPrinter.INSTANCE.alert(
					Alert.AlertType.INFORMATION,
					"開く",
					null,
					"ファイルを開けませんでした\n" + fileName);
			}
		});
	}

	/**
	 * BunnyHopの基本情報を表示するハンドラを登録する
	 */
	private void setAboutBunnyHopHandler() {
		aboutBunnyHop.setOnAction(action -> {
			String content = "Version: " + BhParams.APP_VERSION;
			MsgPrinter.INSTANCE.alert(
				Alert.AlertType.INFORMATION,
				"BunnyHopについて",
				null,
				content);
		});
	}

	/**
	 * ファイル保存時の初期ディレクトリを返す
	 * @return ファイル保存時の初期ディレクトリ
	 */
	private File getInitDir() {

		if (currentSaveFile != null) {
			File parent = currentSaveFile.getParentFile();
			if (parent != null) {
				if (parent.exists()) {
					return parent;
				}
			}
		}
		return new File(Util.EXEC_PATH);
	}

	/**
	 * ロード方法を確認する
	 * @retval true 既存のワークスペースをすべて削除
	 * @retval false 既存のワークスペースにロードしたワークスペースを追加
	 */
	private boolean askIfClearOldWs() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ファイルのロード方法");
		alert.setHeaderText(null);
		alert.setContentText("既存のワークスペースに追加する場合は" + "[" + ButtonType.YES.getText() + "].\n"
			+ "既存のワークスペースを全て削除する場合は" + "[" + ButtonType.NO.getText() + "].");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert.getDialogPane().getStylesheets().addAll(BunnyHop.INSTANCE.getAllStyles());
		Optional<ButtonType> result = alert.showAndWait();
		return result.get().equals(ButtonType.NO);
	}

		/**
	 * ユーザメニュー操作のイベントを起こす
	 * @param op 基本操作を表す列挙子
	 */
	public void fireEvent(MENU_BAR op) {

		switch (op) {
			case SAVE:
				saveMenu.fire();
				break;

			case SAVE_AS:
				saveAsMenu.fire();
				break;

			default:
				throw new AssertionError("invalid menu bar operation " + op);
		}
	}
	public enum MENU_BAR {
		SAVE,
		SAVE_AS
	}
}
