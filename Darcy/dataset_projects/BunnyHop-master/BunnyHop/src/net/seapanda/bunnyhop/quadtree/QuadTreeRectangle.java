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
package net.seapanda.bunnyhop.quadtree;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import net.seapanda.bunnyhop.common.Linkable;
import net.seapanda.bunnyhop.common.Point2D;

/**
 * 4分木空間に登録される矩形オブジェクト<br>
 * @author K.Koike
 * */
public class QuadTreeRectangle extends Linkable<QuadTreeRectangle> {

	private int currentIdxInQuadTree = -1;  //!< 現在属している4分木ノードのインデックス


	private Point2D upperLeftPos;	//!< 矩形の左上座標
	private Point2D lowerRightPos;	//!< 矩形の右下座標
	Consumer<QuadTreeRectangle> posUpdateHandler;		//!< 位置が更新されたときに呼び出すメソッド
	Function<QuadTreeRectangle, ArrayList<QuadTreeRectangle>> searchOverlappedHandler;	//!< このオブジェクトの矩形領域に重なっているQuadTreeRectangleを探すときに呼び出すメソッド
	private final Object drawedObj;	//!< この矩形に対応する描画対象のオブジェクト

	/**
	 * コンストラクタ
	 * @param upperLeftX 左上X座標
	 * @param upperLeftY 左上Y座標
	 * @param lowerRightX 右下X座標
	 * @param lowerRightY 右下Y座標
	 * @param drawedObj この矩形に対応する描画対象のオブジェクト
	 * */
	public QuadTreeRectangle(double upperLeftX, double upperLeftY, double lowerRightX, double lowerRightY, Object drawedObj){
		upperLeftPos = new Point2D(upperLeftX, upperLeftY);
		lowerRightPos = new Point2D(lowerRightX, lowerRightY);
		container = this;
		this.drawedObj = drawedObj;
	};

	public QuadTreeRectangle() {
		container = this;
		drawedObj = null;
	};

	/**
	 * 位置更新
	 * @param upperLeftX 左上X座標
	 * @param upperLeftY 左上Y座標
	 * @param lowerRightX 右下X座標
	 * @param lowerRightY 右下Y座標
	 * */
	public void updatePos(double upperLeftX, double upperLeftY, double lowerRightX, double lowerRightY) {
		upperLeftPos.x = upperLeftX;
		upperLeftPos.y = upperLeftY;
		lowerRightPos.x = lowerRightX;
		lowerRightPos.y = lowerRightY;
		if (posUpdateHandler != null)
			posUpdateHandler.accept(this);
	}

	/**
	 * 矩形の左上座標を返す
	 * @return 矩形の左上座標
	 * */
	public Point2D getUpperLeftPos() {
		return upperLeftPos;
	}

	/**
	 * 矩形の右下座標を返す
	 * @return 矩形の右下座標
	 * */
	public Point2D getLowerRightPos() {
		return lowerRightPos;
	}

	/**
	 * コールバック関数を登録する
	 * @param posUpdate 位置更新時に呼び出すメソッド
	 * @param searchOverlapped このオブジェクトの矩形領域に重なっているQuadTreeRectangleを探すときに呼び出すメソッド
	 * */
	public void setCallBackFuncs(Consumer<QuadTreeRectangle> posUpdate, Function<QuadTreeRectangle, ArrayList<QuadTreeRectangle>> searchOverlapped) {
		posUpdateHandler = posUpdate;
		searchOverlappedHandler = searchOverlapped;
	}

	/**
	 * 現在属している4分木ノードのインデックスを返す
	 * @return 現在属している4分木ノードのインデックス
	 * */
	public int getIdxInQuadTree() {
		return currentIdxInQuadTree;
	}

	/**
	 * 4分木ノードのインデックスをセットする
	 * @param idxInQuadTree 4分木ノードのインデックス
	 * */
	public void setIdxInQuadTree(int idxInQuadTree) {
		currentIdxInQuadTree = idxInQuadTree;
	}

	/**
	 * 引数のオブジェクトとこのオブジェクトが重なりを判定する
	 * @param rectangle このオブジェクトとの重なりを判定するオブジェクト
	 * @return 引数のオブジェクトとこのオブジェクトが重なっていた場合 true
	 * */
	public boolean overlapsWith(QuadTreeRectangle rectangle) {
		if (rectangle.lowerRightPos.x >= upperLeftPos.x &&
			rectangle.upperLeftPos.x <= lowerRightPos.x &&
			rectangle.lowerRightPos.y >= upperLeftPos.y &&
			rectangle.upperLeftPos.y <= lowerRightPos.y)
			return true;
		return false;
	}

	/**
	 * この矩形に重なっているQuadTreeRectangleオブジェクトを4分木空間から探す
	 * */
	public ArrayList<QuadTreeRectangle> searchOverlappedRects(){
		return searchOverlappedHandler.apply(this);
	}

	/**
	 * この矩形に対応する描画対象のオブジェクトを返す
	 * @return 描画対象のオブジェクト
	 * */
	public <T> T getDrawedObj() {
		return (T)drawedObj;
	}
}
















