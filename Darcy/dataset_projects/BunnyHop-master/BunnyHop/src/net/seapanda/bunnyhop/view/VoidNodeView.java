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

import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.model.VoidNode;

/**
 * VoidNode に対応するビュー
 * @author K.Koike
 */
public class VoidNodeView extends BhNodeView {

	private final VoidNode model;	//!< このビューに対応するモデル
	
	/**
	 * コンストラクタ
	 * @param model ビューに対応するモデル
	 * @param viewStyle ビューのスタイル
	 * */
	public VoidNodeView(VoidNode model, BhNodeViewStyle viewStyle) {

		super(viewStyle, model);
		this.model = model;
	}

	/**
	 * 初期化する
	 */
	public void init() {
		initialize();
		getAppearanceManager().addCssClass(BhParams.CSS.CLASS_VOID_NODE);
		setFuncs(this::updateStyleFunc, null);
		setMouseTransparent(true);
	}
	
	/**
	 * このビューのモデルであるBhNodeを取得する
	 * @return このビューのモデルであるBhNode
	 */
	@Override
	public VoidNode getModel() {
		return model;
	}
	
	/**
	 * ノードの大きさや見た目を変える関数
	 * */
	private void updateStyleFunc(BhNodeViewGroup child) {
		
		boolean inner = (parent == null) ? true : parent.inner;
		//ボディサイズ決定
		if (!inner) {
			viewStyle.topMargin = 0.0;
			viewStyle.height = 0.0;
			viewStyle.bottomMargin = 0.0;
			viewStyle.leftMargin = 0.0;
			viewStyle.width = 0.0;
			viewStyle.rightMargin = 0.0;
		}
		
		boolean drawBody = inner && viewStyle.drawBody;
		getAppearanceManager().updatePolygonShape(drawBody);
		if (parent != null)
			parent.updateStyle();
	}

	/**
	 * モデルの構造を表示する
	 * @param depth 表示インデント数
	 * */
	@Override
	public void show(int depth) {
		MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<VoidNodeView" + ">   " + this.hashCode());
	}
}


