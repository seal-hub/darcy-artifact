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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;
import net.seapanda.bunnyhop.common.Rem;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.message.MsgProcessor;
import net.seapanda.bunnyhop.model.BhNodeCategoryList;
import net.seapanda.bunnyhop.view.BhNodeCategoryListView;

/**
 * BhNode のカテゴリ選択画面のController
 * @author K.Koike
 * */
public class BhNodeCategoryListController implements MsgProcessor {

	@FXML private ScrollPane nodeCategoryListViewBase;
	@FXML private TreeView<BhNodeCategoryListView.BhNodeCategory> categoryTree;
	private BhNodeCategoryList model;
	private BhNodeCategoryListView view;

	/**
	 * モデルとイベントハンドラの登録を行う
	 * @param categoryList ノードカテゴリリストのモデル
	 */
	public void init(BhNodeCategoryList categoryList) {
		model = categoryList;
		view = new BhNodeCategoryListView(categoryTree);
		nodeCategoryListViewBase.setMinWidth(Region.USE_PREF_SIZE);
		nodeCategoryListViewBase.widthProperty().addListener((obs, oldVal, newVal) -> {nodeCategoryListViewBase.setMinWidth(Rem.VAL * 3);});
	}

	/**
	 * カテゴリリスト部分の基底GUI部品を返す
	 * @return カテゴリリスト部分の基底GUI部品
	 */
	public ScrollPane getCategoryListViewBase() {
		return nodeCategoryListViewBase;
	}

	/**
	 * カテゴリリストのビューを返す
	 * @return カテゴリリストのビュー
	 */
	public BhNodeCategoryListView getView() {
		return view;
	}

	@Override
	public MsgData processMsg(BhMsg msg, MsgData data) {
		switch (msg) {
		case BUILD_NODE_CATEGORY_LIST_VIEW:
			view.buildCategoryList(model.getRootNode());
			break;

		case ADD_NODE_SELECTION_PANELS:
			return new MsgData(view.getSelectionViewList());

		case HIDE_NODE_SELECTION_PANEL:
			view.hideAll();
			break;

		case ZOOM:
			view.zoomAll(data.bool);
			break;

		default :
			throw new AssertionError("receive an unknown msg " + msg);
		}
		return null;
	}
}
