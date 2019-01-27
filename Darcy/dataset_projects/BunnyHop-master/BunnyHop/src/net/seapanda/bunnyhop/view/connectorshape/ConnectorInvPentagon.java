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
 * 逆五角形コネクタクラス
 * @author K.Koike
 * */
public class ConnectorInvPentagon extends ConnectorShape {


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
			final double ofsX = 0.475;
			final double ofsY = 0.5;
			vertices = new ArrayList<>(Arrays.asList(
				offsetX + width * (ofsX + 0.525),                            offsetY + height * (ofsY + 0),
				offsetX + width * (ofsX + 0.1622339220468473976537)       ,  offsetY + height * (ofsY + 0.4993046710549556253611),
				offsetX + width * (ofsX - 0.4247339220468473976537)       ,  offsetY + height * (ofsY + 0.3085872574535483928136),
				offsetX + width * (ofsX - 0.4247339220468473976537)       ,  offsetY + height * (ofsY - 0.3085872574535483928136),
				offsetX + width * (ofsX + 0.1622339220468473976537)       ,  offsetY + height * (ofsY - 0.4993046710549556253611),
				offsetX + width * (ofsX + 0.525)                          ,  offsetY + height * (ofsY + 0)));
		}
		else if (pos == CNCTR_POS.TOP) {
			final double ofsX = 0.5;
			final double ofsY = 0.475;
			vertices = new ArrayList<>(Arrays.asList(
				offsetX + width * (ofsX + 0)                              ,  offsetY + height * (ofsY + 0.525),
				offsetX + width * (ofsX - 0.4993046710549556253611)       ,  offsetY + height * (ofsY + 0.1622339220468473976537),
				offsetX + width * (ofsX - 0.3085872574535483928136)       ,  offsetY + height * (ofsY - 0.4247339220468473976537),
				offsetX + width * (ofsX + 0.3085872574535483928136)       ,  offsetY + height * (ofsY - 0.4247339220468473976537),
				offsetX + width * (ofsX + 0.4993046710549556253611)       ,  offsetY + height * (ofsY + 0.1622339220468473976537),
				offsetX + width * (ofsX + 0)                              ,  offsetY + height * (ofsY + 0.525)));
		}
		return vertices;
	}
}