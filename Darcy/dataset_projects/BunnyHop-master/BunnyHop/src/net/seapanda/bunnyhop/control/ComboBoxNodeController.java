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

import java.util.Objects;
import java.util.Optional;

import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.model.TextNode;
import net.seapanda.bunnyhop.view.ComboBoxNodeView;
import net.seapanda.bunnyhop.view.SelectableItem;

/**
 * TextNode と ComboBoxNodeView のコントローラ
 * @author K.Koike
 */
public class ComboBoxNodeController extends BhNodeController {

	private final TextNode model;	//!< 管理するモデル
	private final ComboBoxNodeView view;	//!< 管理するビュー

	public ComboBoxNodeController(TextNode model, ComboBoxNodeView view) {
		super(model, view);
		this.model = model;
		this.view = view;
		view.setCreateImitHandler(model);
		setItemChangeHandler(model, view);
	}

	/**
	 * ComboBoxView のアイテム変更時のイベントハンドラを登録する
	 *@param model ComboBoxView に対応する model
	 * @param view イベントハンドラを登録するview
	 */
	public static void setItemChangeHandler(TextNode model, ComboBoxNodeView view) {

		view.setTextChangeListener((observable, oldVal, newVal) -> {
			if (Objects.equals(newVal.getModelText(), model.getText())) {
				return;
			}

			if (model.isTextAcceptable(newVal.getModelText())) {
				model.setText(newVal.getModelText());	//model の文字列をComboBox の選択アイテムに対応したものにする
				model.getImitNodesToImitateContents();	//イミテーションのテキストを変える (イミテーションの View がtextFieldの場合のみ有効)
			}
			else {
				view.setItem(oldVal);
			}
		});

		Optional<SelectableItem> optItem = view.getItemByModelText(model.getText());

		optItem.ifPresent(item -> view.setItem(item));
		if (!optItem.isPresent())
			model.setText(view.getItem().getModelText());
	}

	/**
	 * 受信したメッセージを処理する
	 * @param msg メッセージの種類
	 * @param data メッセージの種類に応じて処理するデータ
	 * @return メッセージを処理した結果返すデータ
	 * */
	@Override
	public MsgData processMsg(BhMsg msg, MsgData data) {

		switch (msg) {
			case GET_MODEL_AND_VIEW_TEXT:
				return new MsgData(model.getText(), view.getItem().getViewText());

			default:
				return super.processMsg(msg, data);
		}
	}
}
