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

import java.util.Objects;

/**
 * コンボボックスなどに登録する選択アイテム
 * @author K.Koike
 */
public class SelectableItem {
	
	private String viewText;	//!< ビューに表示されるテキスト
	private String modelText;	//!< モデルが保持するテキスト
	
	/**
	 * ビューに表示されるテキストをセットする
	 * @param text ビューに表示されるテキスト
	 */
	public void setViewText(String text) {
		viewText = text;
	}
	
	/**
	 * ビューに表示されるテキストを取得する
	 * @return ビューに表示されるテキスト
	 */
	public String getViewText() {
		return viewText;
	}
	
	/**
	 * モデルが保持するテキストをセットする
	 * @param text モデル側でのテキスト
	 */
	public void setModelText(String text) {
		modelText = text;
	}
	
	/**
	 * モデルが保持するテキストを取得する
	 * @return モデル側でのテキスト
	 */
	public String getModelText() {
		return modelText;
	}
	
	@Override
	public String toString() {
		return viewText;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		SelectableItem selectableItem = (SelectableItem)obj;
		if (selectableItem.getModelText() == null) {
			return modelText == null;
		}

		return Objects.equals(modelText, selectableItem.modelText) &&
			   Objects.equals(viewText, selectableItem.viewText);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 17 * hash + Objects.hashCode(this.viewText);
		hash = 17 * hash + Objects.hashCode(this.modelText);
		return hash;
	}
}
