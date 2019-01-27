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
 * 八角形型コネクタクラス
 * @author K.Koike
 * */
public class ConnectorOctagon extends ConnectorShape {

	/** コネクタの頂点を算出する
	 * @param offsetX 頂点に加算するオフセットX
	 * @param offsetY 頂点に加算するオフセットY
	 * @param width   コネクタの幅
	 * @param height  コネクタの高さ
	 * */
	@Override
	public Collection<Double> createVertices(double offsetX, double offsetY, double width, double height, CNCTR_POS pos) {

		ArrayList<Double> vertices = null;
		final double ofs = 0.5;

		if (pos == CNCTR_POS.LEFT) {
			vertices = new ArrayList<>(Arrays.asList(
				offsetX + width * (ofs + 0.5),                       offsetY + height * (ofs + 0),
				offsetX + width * (ofs + 0.3535533905932737622004),  offsetY + height * (ofs + 0.3535533905932737622004),
				offsetX + width * (ofs + 0),                         offsetY + height * (ofs + 0.5),
				offsetX + width * (ofs - 0.3535533905932737622004),  offsetY + height * (ofs + 0.3535533905932737622004),
				offsetX + width * (ofs - 0.5),                       offsetY + height * (ofs + 0),
				offsetX + width * (ofs - 0.3535533905932737622004),  offsetY + height * (ofs - 0.3535533905932737622004),
				offsetX + width * (ofs + 0),                         offsetY + height * (ofs - 0.5),
				offsetX + width * (ofs + 0.3535533905932737622004),  offsetY + height * (ofs - 0.3535533905932737622004),
				offsetX + width * (ofs + 0.5),                       offsetY + height * (ofs + 0)));
		}
		else if (pos == CNCTR_POS.TOP) {
			vertices = new ArrayList<>(Arrays.asList(
				offsetX + width * (ofs + 0),                         offsetY + height * (ofs + 0.5),
				offsetX + width * (ofs - 0.3535533905932737622004),  offsetY + height * (ofs + 0.3535533905932737622004),
				offsetX + width * (ofs - 0.5),                       offsetY + height * (ofs + 0),
				offsetX + width * (ofs - 0.3535533905932737622004),  offsetY + height * (ofs - 0.3535533905932737622004),
				offsetX + width * (ofs + 0),                         offsetY + height * (ofs - 0.5),
				offsetX + width * (ofs + 0.3535533905932737622004),  offsetY + height * (ofs - 0.3535533905932737622004),
				offsetX + width * (ofs + 0.5),                       offsetY + height * (ofs - 0),
				offsetX + width * (ofs + 0.3535533905932737622004),  offsetY + height * (ofs + 0.3535533905932737622004),
				offsetX + width * (ofs + 0),                         offsetY + height * (ofs + 0.5)));
		}
		return vertices;
	}
}

