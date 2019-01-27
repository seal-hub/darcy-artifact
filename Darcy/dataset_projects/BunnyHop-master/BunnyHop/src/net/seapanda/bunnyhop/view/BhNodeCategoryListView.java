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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.css.PseudoClass;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.seapanda.bunnyhop.modelprocessor.NodeMVCBuilder;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.TreeNode;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.BhNodeID;
import net.seapanda.bunnyhop.model.templates.BhNodeTemplates;
import net.seapanda.bunnyhop.modelprocessor.TextImitationPrompter;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * BhNode のカテゴリ選択画面のView
 * @author K.Koike
 * */
public class BhNodeCategoryListView {

	private final TreeView<BhNodeCategory> categoryTree;
	private final Map<BhNodeCategory, BhNodeSelectionView> category_selectionView = new HashMap<>();	//!< BhNode選択カテゴリ名とBhNode選択ビューのマップ
	private final List<BhNodeSelectionView> selectionViewList = new ArrayList<>();	//!< BhNode選択ビューのリスト
	private final List<BhNodeCategory> categoryList = new ArrayList<>();
	
	
	public BhNodeCategoryListView(TreeView<BhNodeCategory> categoryTree) {
		this.categoryTree = categoryTree;
	}

	/**
	 * BhNode のカテゴリリスト画面を作る
	 * @param root 選択リストのノード
	 * */
	public void buildCategoryList(TreeNode<String> root) {

		TreeItem<BhNodeCategory> rootItem = new TreeItem<>(new BhNodeCategory(root.content));
		rootItem.setExpanded(true);
		addChildren(root, rootItem);
		categoryTree.setRoot(rootItem);
		categoryTree.setShowRoot(false);
		categoryTree.setCellFactory(templates -> {
			return new BhNodeCategoryView();
		});
	}

	/**
	 * ノード選択ビューのリストを取得する
	 * @return ノード選択ビューのリスト
	 */
	public List<BhNodeSelectionView> getSelectionViewList() {
		return selectionViewList;
	}
	
	/**
	 * テンプレートツリーに子ノードを追加する.<br>
	 * @param parent 追加する子ノード情報を持ったノード
	 * @param parentItem 子ノードを追加したいノード
	 * */
	private void addChildren(TreeNode<String> parent, TreeItem<BhNodeCategory> parentItem) {

		parent.children.forEach(child -> {
			
			switch (child.content) {
				case BhParams.NodeTemplateList.KEY_CSS_CLASS:
					String cssClass = child.children.get(0).content;
					parentItem.getValue().setCssClass(cssClass);
					break;
					
				case BhParams.NodeTemplateList.KEY_CONTENTS:
					child.children.forEach(bhNodeID -> {
						addBhNodeToSelectionView(parentItem.getValue(), BhNodeID.createBhNodeID(bhNodeID.content));
					});	break;
					
				default:
					BhNodeCategory category = new BhNodeCategory(child.content);
					categoryList.add(category);
					TreeItem<BhNodeCategory> childItem = new TreeItem<>(category);
					parentItem.getChildren().add(childItem);
					childItem.setExpanded(true);
					addChildren(child, childItem);
					break;
			}
		});
	}

	/**
	 * ノード選択ビューにBhNodeを追加する
	 * @param category ノード選択ビューが属するカテゴリ
	 * @param bhNodeID 追加するBhNodeのID
	 */
	private void addBhNodeToSelectionView(BhNodeCategory category, BhNodeID bhNodeID) {
		
		if (!category_selectionView.containsKey(category)) {
			BhNodeSelectionView selectionView = new BhNodeSelectionView();
			selectionView.init(category.categoryName, category.cssClass, this);
			category.setFuncOnSelectionViewShowed(show -> {
				if (show)
					selectionView.arrange();
				selectionView.setVisible(show);
			});
			category_selectionView.put(category, selectionView);
			selectionViewList.add(selectionView);
			selectionView.setVisible(false);
		}
		UserOperationCommand userOpeCmd = new UserOperationCommand();
		BhNode node = BhNodeTemplates.INSTANCE.genBhNode(bhNodeID, userOpeCmd);
		NodeMVCBuilder builder = new NodeMVCBuilder(NodeMVCBuilder.ControllerType.Template);
		node.accept(builder);	//MVC構築
		node.accept(new TextImitationPrompter());
		category_selectionView.get(category).addBhNodeView(builder.getTopNodeView());	//BhNode テンプレートリストパネルにBhNodeテンプレートを追加	
	}
		
	/**
	 * 全てのBhNode選択パネルを隠す
	 * */
	public void hideAll() {
		categoryList.forEach(category -> category.hide());
	}
	
	/**
	 * BhNode選択パネルのうち表示されているものがあるかどうか調べる
	 * @return BhNode選択パネルのうち一つでも表示されている場合true
	 */
	public boolean isAnyShowed() {
		return selectionViewList.stream().anyMatch(view -> view.visibleProperty().get());
	}
	
	/**
	 * BhNode選択パネルを全て拡大もしくは縮小する
	 * @param zoomIn 拡大する場合true
	 */
	public void zoomAll(boolean zoomIn) {
		selectionViewList.forEach(view -> view.zoom(zoomIn));
	}

	/**
	 * カテゴリ名格納クラス
	 * */
	public class BhNodeCategory {

		public final String categoryName;
		private boolean displayed = false;
		private String cssClass = "";
		Consumer<Boolean> showCellView;
		Consumer<Boolean> showTemplatePanel;

		public BhNodeCategory(String category) {
			this.categoryName = category;
		}

		@Override
		public String toString() {
			return categoryName==null ? "" : categoryName;
		}

		public void show() {

			if (category_selectionView.containsKey(this)) {	//表示するテンプレートパネルがある場合
				BhNodeCategoryListView.this.hideAll();	//前に選択されていたものを非選択にする
				displayed = true;
				showTemplatePanel.accept(true);
			}
			showCellView.accept(true);
		}

		public void hide() {
			displayed = false;
			showCellView.accept(false);
			if (showTemplatePanel != null)	//葉ノード以外のカテゴリ名はパネルを持っていないことがある
				showTemplatePanel.accept(false);
		}

		public boolean isDisplayed() {
			return displayed;
		}

		/**
		 * このカテゴリが表示/非表示されたときの TreeCell の表示/非表示用関数を登録する
		 * */
		public void setFuncOnCellViewShowed(Consumer<Boolean> func) {
			showCellView = func;
		}

		/**
		 * このカテゴリが表示/非表示されたときのテンプレートパネル表示/非表示用関数を登録する
		 * */
		public void setFuncOnSelectionViewShowed(Consumer<Boolean> func) {
			showTemplatePanel = func;
		}
		
		public void setCssClass(String cssClass) {
			this.cssClass = cssClass;
		}
		
		public String getCssClass() {
			return cssClass;
		}
	}

	/**
	 * BhNode カテゴリのView.  BhNodeCategoryとの結びつきは動的に変わる
	 * */
	public class BhNodeCategoryView extends TreeCell<BhNodeCategory> {

		BhNodeCategory model;
		public BhNodeCategoryView() {

			// BhNode のカテゴリクリック時の処理
			setOnMousePressed(evenet -> {

				//カテゴリ名の無いTreeCell がクリックされたときはノード選択パネルを隠す
				if (isEmpty()) {
					BhNodeCategoryListView.this.hideAll();
					return;
				}

				if (model.isDisplayed()) {	//表示済みカテゴリを再度クリックした場合はそれを隠す
					BhNodeCategoryListView.this.hideAll();
				}
				else {
					model.show();
				}
			});
		}

		/**
		 * TreeItemの選択状態を解除する
		 * */
		public void select(boolean select) {
			pseudoClassStateChanged(PseudoClass.getPseudoClass(BhParams.CSS.PSEUDO_SELECTED), select);
		}

		@Override
		protected void updateItem(BhNodeCategory category, boolean empty) {
			
			super.updateItem(category, empty);
			model = category;
			if (!empty) {
				category.setFuncOnCellViewShowed(this::select);
				getStyleClass().add(model.getCssClass());				
				pseudoClassStateChanged(PseudoClass.getPseudoClass(BhParams.CSS.PSEUDO_EMPTY), false);
				setText(category.toString());
			}
			else {
				select(false);
				getStyleClass().clear();
				getStyleClass().add("tree-cell");
				pseudoClassStateChanged(PseudoClass.getPseudoClass(BhParams.CSS.PSEUDO_EMPTY), true);
				setText(null);
			}
		}
	}
}












