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
 * 五角形コネクタクラス
 * @author K.Koike
 * */
public class ConnectorPentagon extends ConnectorShape {


	/** コネクタの頂点を算出する
	 * @param offsetX 頂点に加算するオフセットX
	 * @param offsetY 頂点に加算するオフセットY
	 * @param width   コネクタの幅
	 * @param height  コネクタの高さ
	 * */
	@Override
	public Collection<Double> createVertices(double offsetX, double offsetY, double width, double height, CNCTR_POS pos) {

		ArrayList<Double> vertices = null;
		final double p = 4.0;
		final double q = 1.1;
		final double r = 4.0;
		final double s = 2.0;

		if (pos == CNCTR_POS.LEFT) {
			vertices = new ArrayList<>(Arrays.asList(
				offsetX + width,                  offsetY + height * (1.0 - q / p),
				offsetX + width * (s / r),        offsetY + height,
				offsetX + 0.0,                    offsetY + height * 0.5,
				offsetX + width * (s / r),        offsetY + 0.0,
				offsetX + width,                  offsetY + height * (q / p)));
		}
		else if (pos == CNCTR_POS.TOP) {
			vertices = new ArrayList<>(Arrays.asList(
				offsetX + width * (q / p),        offsetY + height,
				offsetX + 0.0,                    offsetY + height * (s / r),
				offsetX + width * 0.5,            offsetY + 0.0,
				offsetX + width,                  offsetY + height * (s / r),
				offsetX + width * (1.0 - q / p),  offsetY + height));
		}
		return vertices;
	}
}

