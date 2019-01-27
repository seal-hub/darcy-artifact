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

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import net.seapanda.bunnyhop.model.BhNodeCategoryList;
import net.seapanda.bunnyhop.model.WorkspaceSet;

/**
 * GUIの基底部分のコントローラ
 * @author K.Koike
 */
public class FoundationController {

	//View
	@FXML VBox foundationVBox;
	@FXML SplitPane horizontalSplitter;

	//Controller
	@FXML private MenuOperationController menuOperationController;
	@FXML private WorkspaceSetController workspaceSetController;
	@FXML private BhNodeCategoryListController nodeCategoryListController;
	@FXML private MenuBarController menuBarController;

	/**
	 * 初期化する
	 * @param wss ワークスペースセットのモデル
	 * @param nodeCategoryList ノードカテゴリリストのモデル
	 */
	public void init(WorkspaceSet wss, BhNodeCategoryList nodeCategoryList) {

		workspaceSetController.init(wss);
		nodeCategoryListController.init(nodeCategoryList);
		menuOperationController.init(
			wss,
			workspaceSetController.getTabPane(),
			nodeCategoryListController.getView());
		menuBarController.init(wss);

		wss.setMsgProcessor(workspaceSetController);
		nodeCategoryList.setMsgProcessor(nodeCategoryListController);
		setKeyEvents();
	}

	public MenuBarController getMenuBarController() {
		return menuBarController;
	}

	/**
	 * キーボード押下時のイベントを登録する
	 */
	private void setKeyEvents() {
		foundationVBox.setOnKeyPressed(event -> {
			switch (event.getCode()) {
				case C:
					if (event.isControlDown())
						menuOperationController.fireEvent(MenuOperationController.MENU_OPERATION.COPY);
					break;

				case X:
					if (event.isControlDown())
						menuOperationController.fireEvent(MenuOperationController.MENU_OPERATION.CUT);
					break;

				case V:
					if (event.isControlDown())
						menuOperationController.fireEvent(MenuOperationController.MENU_OPERATION.PASTE);
					break;

				case Z:
					if (event.isControlDown())
						menuOperationController.fireEvent(MenuOperationController.MENU_OPERATION.UNDO);
					break;

				case Y:
					if (event.isControlDown())
						menuOperationController.fireEvent(MenuOperationController.MENU_OPERATION.REDO);
					break;

				case S:
					if (event.isControlDown())
						menuBarController.fireEvent(MenuBarController.MENU_BAR.SAVE);
					break;

				case F12:
					menuBarController.fireEvent(MenuBarController.MENU_BAR.SAVE_AS);
					break;

				case DELETE:
					menuOperationController.fireEvent(MenuOperationController.MENU_OPERATION.DELETE);
					break;

				default:
			}
			event.consume();
		});
	}
}
