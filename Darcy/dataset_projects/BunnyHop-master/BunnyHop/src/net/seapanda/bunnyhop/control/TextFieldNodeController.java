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

import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.model.TextNode;
import net.seapanda.bunnyhop.view.TextFieldNodeView;

/**
 * TextNodeとTextFieldNodeViewのコントローラ
 * @author K.Koike
 */
public class TextFieldNodeController extends BhNodeController {

	private final TextNode model;	//!< 管理するモデル
	private final TextFieldNodeView view;	//!< 管理するビュー

	public TextFieldNodeController(TextNode model, TextFieldNodeView view) {
		super(model, view);
		this.model = model;
		this.view = view;
		if (model.isImitationNode())
			view.setEditable(false);
		setTextChangeHandler(model, view);
		view.setCreateImitHandler(model);
	}

	/**
	 * TextNodeView に対して文字列変更時のハンドラを登録する
	 * @param model TextNodeView に対応する model
	 * @param view イベントハンドラを登録するview
	 * */
	static public void setTextChangeHandler(TextNode model, TextFieldNodeView view) {

		view.setTextChangeListener(model::isTextAcceptable);

		view.setObservableListener((observable, oldValue, newValue) -> {
			if (!newValue) {	//テキストフィールドからカーソルが外れたとき
				String currentGUIText = view.getText();
				boolean isValidFormat = model.isTextAcceptable(view.getText());
				if (isValidFormat) {	//正しいフォーマットの文字列が入力されていた場合
					model.setText(currentGUIText);	//model の文字列をTextField のものに変更する
					model.getImitNodesToImitateContents();
				}
				else {
					view.setText(model.getText());	//view の文字列を変更前の文字列に戻す
				}
			}
		});
		
		String initText = model.getText();
		view.setText(initText + " ");	//初期文字列が空文字だったときのため
		view.setText(initText);			
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
			case IMITATE_TEXT:
				model.setText(data.strPair._1);
				boolean editable = view.getEditable();
				view.setEditable(true);
				view.setText(data.strPair._2);
				view.setEditable(editable);
				break;
			
			case GET_MODEL_AND_VIEW_TEXT:
				return new MsgData(model.getText(), view.getText());
				
			default:
				return super.processMsg(msg, data);
		}
		return null;
	}
}
