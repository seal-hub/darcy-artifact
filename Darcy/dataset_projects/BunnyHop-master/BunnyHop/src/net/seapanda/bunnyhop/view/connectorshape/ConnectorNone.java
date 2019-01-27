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
package net.seapanda.bunnyhop.view.connectorshape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.seapanda.bunnyhop.view.BhNodeViewStyle.CNCTR_POS;

/**
 * 非表示コネクタクラス
 * @author K.Koike
 * */
public class ConnectorNone extends ConnectorShape {


	/** コネクタの頂点を算出する
	 * @param offsetX 頂点に加算するオフセットX
	 * @param offsetY 頂点に加算するオフセットY
	 * @param width   コネクタの幅
	 * @param height  コネクタの高さ
	 * */
	@Override
	public Collection<Double> createVertices(double offsetX, double offsetY, double width, double height, CNCTR_POS pos) {

		ArrayList<Double> vertices = null;

		if (pos == CNCTR_POS.LEFT) {
			vertices = new ArrayList<>(Arrays.asList(
				offsetX + width,             offsetY + height,
				offsetX + width,             offsetY + 0.0));
		}
		else if (pos == CNCTR_POS.TOP) {
			vertices = new ArrayList<>(Arrays.asList(
				offsetX + 0.0,               offsetY + height,
				offsetX + width,             offsetY + height));
		}
		return vertices;
	}
}

