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
import java.util.function.Function;
import net.seapanda.bunnyhop.common.Point2D;

/**
 * 4文木空間を持ちいた衝突を管理するクラス
 * @author K.Koike
 * */
public class QuadTreeManager {

	private int numRecursive;	//!< 再帰的に分割する回数
	private int numPartitions;	//!< 縦と横の分割数.
	private double width;		//!< 分割する空間横幅
	private double height;		//!< 分割する空間の縦幅
	private double cellWidth;	//!< 分割された空間の横幅
	private double cellHeight;	//!< 分割された空間の縦幅
	private ArrayList<QuadTreeRectangle> quadTree;	//!< 4分木
	private QuadTreeRectangle unknownSpaceListHead = new QuadTreeRectangle();	//!< 4分木空間上での位置が決まっていない 4分木オブジェクトのリストの先頭

	/**
	 * コンストラクタ
	 * @param numRecursive 再帰的に分割する回数 (3 の場合縦横が 2**3 = 8 に分割され, 64個の小空間に分割される)
	 * @param width 分割される空間の横幅
	 * @param height 分割される空間の縦幅
	 * */
	public QuadTreeManager(int numRecursive, double width, double height) {

		this.numRecursive = numRecursive;
		this.width = width;
		this.height = height;
		numPartitions = 1;
		for (int i = 0; i < numRecursive; ++i)
			numPartitions *= 2;

		cellWidth = width / numPartitions;
		cellHeight = height / numPartitions;

		int tmp = 1;
		for (int i = 0; i < numRecursive + 1; ++i)
			tmp *= 4;
		int numQuadTreeNode = (tmp - 1) / 3;
		quadTree = new ArrayList<>(numQuadTreeNode);
		for (int i = 0; i < numQuadTreeNode; ++i)
			quadTree.add(new QuadTreeRectangle());
	}

	/**
	 * コンストラクタ
	 * @param org コピー元オブジェクト
	 * @param numRecursive 再帰的に分割する回数 (3 の場合縦横が 2**3 = 8 に分割され, 64個の小空間に分割される)
	 * @param width 分割される空間の横幅
	 * @param height 分割される空間の縦幅
	 * */
	public QuadTreeManager(QuadTreeManager org, int numRecursive, double width, double height) {

		this(numRecursive, width, height);
		moveQuadTreeObj(org);
	}

	/**
	 * 矩形オブジェクトを登録する
	 * @param quadTreeObj 登録する矩形オブジェクト
	 * */
	public void addQuadTreeObj(QuadTreeRectangle quadTreeObj) {

		quadTreeObj.remove();
		quadTreeObj.setIdxInQuadTree(-1);	//無効な4分木ノードインデックスを登録しておく
		unknownSpaceListHead.connectToNext(quadTreeObj);
		quadTreeObj.setCallBackFuncs(this::registerWithQuadTree, this::searchOverlappedRects);
	}

	/**
	 * 矩形オブジェクトを削除する
	 * @param quadTreeObj 削除する矩形オブジェクト
	 * */
	static public void removeQuadTreeObj(QuadTreeRectangle quadTreeObj) {
		quadTreeObj.remove();
		quadTreeObj.setIdxInQuadTree(-1);
		quadTreeObj.setCallBackFuncs(null, null);
	}

	/**
	 * 古い QuadTreeManager から この QuadTreeManager に 4分木登録オブジェクトを移し替える
	 * @param old 古い QuadTreeManager
	 * */
	private void moveQuadTreeObj(QuadTreeManager old) {

		//4分木からの移動
		old.quadTree.forEach(
			headQuadTreeObj -> {
				QuadTreeRectangle movedQuadTreeObj;
				while ((movedQuadTreeObj = headQuadTreeObj.getNext()) != null)
					addQuadTreeObj(movedQuadTreeObj);
			});

		//位置不明リストからの移動
		QuadTreeRectangle movedQuadTreeObj;
		while ((movedQuadTreeObj = old.unknownSpaceListHead.getNext()) != null)
			addQuadTreeObj(movedQuadTreeObj);
	}

	/**
	 * 4分木空間の大きさを取得する
	 * @return 4分木空間の大きさ
	 * */
	public Point2D getQTSpaceSize() {
		return new Point2D(width, height);
	}

	/**
	 * 4分木に4分木オブジェクトを登録する
	 * @param quadTreeObj 4分木に登録されるオブジェクト
	 * */
	private void registerWithQuadTree(QuadTreeRectangle quadTreeObj) {

		Point2D upperLeftPos = quadTreeObj.getUpperLeftPos();
		Point2D lowerRightPos = quadTreeObj.getLowerRightPos();

		int upperLeftMortonNum = getMortonNumber(upperLeftPos);
		int lowerRightMortonNum = getMortonNumber(lowerRightPos);
		int xorMorton = (upperLeftMortonNum ^ lowerRightMortonNum) << 2 | 0x00000003;
		int spaceLevel = 0; // 分割空間レベル (0:ルート, 1:親, 2:子, 3:孫, ...)
		int levelDecision = 0x3 << numRecursive * 2;
		while ((xorMorton & levelDecision) == 0) {
			//System.out.println(Integer.toHexString(xorMorton) + "  morton  " + "levelDecision " + Integer.toHexString(levelDecision));
			xorMorton <<= 2;
			++spaceLevel;
		}
		int spaceMortonNum = lowerRightMortonNum >>> ((numRecursive - spaceLevel) * 2);
		int tmp = 1;
		for (int i = 0; i < spaceLevel; ++i)
			tmp *= 4;
		int quadTreeIndex = (tmp - 1) / 3 + spaceMortonNum;

//		System.out.println("size " +quadTree.size());
//		System.out.println("upperLeftPos" + upperLeftPos);
//		System.out.println("lowerRightPos " + lowerRightPos);
//		System.out.println("upLeftMorton " + upperLeftMortonNum);
//		System.out.println("lowRightMorton " + lowerRightMortonNum);
//		System.out.println("spaceLevel " + spaceLevel);
//		System.out.println("spaceMortonNum " + spaceMortonNum);
//		System.out.println("quadTreeIndex " + quadTreeIndex + "\n");

		quadTreeObj.remove();
		quadTreeObj.setIdxInQuadTree(quadTreeIndex);
		quadTree.get(quadTreeIndex).connectToNext(quadTreeObj);	//所属空間変更
	}

	/**
	 * 点の位置からモートン番号を求める
	 * @param pos モートン番号を求める位置
	 * @rteurn モートン番号
	 * */
	private int getMortonNumber(Point2D pos) {

		int adjustedX = (int)Math.min(width - 1,  Math.max(0, pos.x));
		int adjustedY = (int)Math.min(height - 1, Math.max(0, pos.y));

		int addressX = (int)(adjustedX / cellWidth);
		int addressY = (int)(adjustedY / cellHeight);

//		System.out.println("spaceWidth " + width);
//		System.out.println("spaceHeight " + height);
//		System.out.println("cellWidth " + cellWidth);
//		System.out.println("cellHeight " + cellHeight);
//		System.out.println("partition " + numPartitions);
//		System.out.println("addrX " + addressX);
//		System.out.println("addrY " + addressY);
//		System.out.println();

		Function<Integer, Integer> bitSeparate =
			(address) -> {
				address = (address | (address << 8)) & 0x00ff00ff;
				address = (address | (address << 4)) & 0x0f0f0f0f;
				address = (address | (address << 2)) & 0x33333333;
				return (address | (address << 1)) & 0x55555555;
			};

		return bitSeparate.apply(addressX) | (bitSeparate.apply(addressY) << 1);
	}

	/**
	 * 引数で指定したQuadTreeRectangleオブジェクトに重なるQuadTreeRectangleを4分木空間の中から見つける
	 * @param rectangle このオブジェクトに重なっているQuadTreeRectangleオブジェクトを見つける
	 * @return 引数で指定したQuadTreeRectangleオブジェクトに重なるQuadTreeRectangleオブジェクトのリスト
	 * */
	public ArrayList<QuadTreeRectangle> searchOverlappedRects(QuadTreeRectangle rectangle) {

		int idxInQuadTree = rectangle.getIdxInQuadTree();
		ArrayList<QuadTreeRectangle> overlappedList = new ArrayList<>();
		searchOverlappedRects(idxInQuadTree, rectangle, overlappedList);	//子空間から探す
		
		//親空間から探す
		int nextSearchIdx = idxInQuadTree - 1;
		while (nextSearchIdx >= 0) {
			nextSearchIdx /= 4;
			searchOverlappedRectsForOneSpace(nextSearchIdx, rectangle, overlappedList);
			nextSearchIdx -= 1;
		}
		overlappedList.remove(rectangle);
		
		//引き数の rectangle に対して近い順にソートする
		double centerX = (rectangle.getLowerRightPos().x - rectangle.getUpperLeftPos().x) / 2 + rectangle.getUpperLeftPos().x;
		double centerY = (rectangle.getLowerRightPos().y - rectangle.getUpperLeftPos().y) / 2 + rectangle.getUpperLeftPos().y;
		overlappedList.sort((rectA, rectB) -> {
			double posAX = (rectA.getLowerRightPos().x - rectA.getUpperLeftPos().x) / 2 + rectA.getUpperLeftPos().x;
			double posAY = (rectA.getLowerRightPos().y - rectA.getUpperLeftPos().y) / 2 + rectA.getUpperLeftPos().y;
			double distanceAX = centerX - posAX;
			double distanceAY = centerY - posAY;
			double distanceA = distanceAX * distanceAX + distanceAY * distanceAY;
			double posBX = (rectB.getLowerRightPos().x - rectB.getUpperLeftPos().x) / 2 + rectB.getUpperLeftPos().x;
			double posBY = (rectB.getLowerRightPos().y - rectB.getUpperLeftPos().y) / 2 + rectB.getUpperLeftPos().y;
			double distanceBX = centerX - posBX;
			double distanceBY = centerY - posBY;
			double distanceB = distanceBX * distanceBX + distanceBY * distanceBY;

			if (distanceA < distanceB)
				return -1;
			else if (distanceA > distanceB)
				return 1;
			return 0;
		});
		return overlappedList;
	}

	/**
	 * 引数で指定したインデックスの4分木空間とその下位空間からrectangle に重なるQuadTreeRectangleオブジェクトを見つける
	 * @param idx この4分木空間以下の空間から rectangleに重なっているQuadTreeRectangleオブジェクトを見つける
	 * @param rectangle このオブジェクトに重なっているQuadTreeRectangleオブジェクトを見つける
	 * @param overlappedList 重なっているQuadTreeRectangleオブジェクトを格納するリスト
	 * */
	private void searchOverlappedRects(int idx, QuadTreeRectangle rectangle, ArrayList<QuadTreeRectangle> overlappedList) {

		searchOverlappedRectsForOneSpace(idx, rectangle, overlappedList);
		int childIdx = idx * 4 + 1;
		if (childIdx <= quadTree.size() - 1) {
			searchOverlappedRects(childIdx, rectangle, overlappedList);
			searchOverlappedRects(childIdx + 1, rectangle, overlappedList);
			searchOverlappedRects(childIdx + 2, rectangle, overlappedList);
			searchOverlappedRects(childIdx + 3, rectangle, overlappedList);
		}
	}

	/**
	 * 引数で指定したインデックスの4分木空間からrectangle に重なるQuadTreeRectangleオブジェクトを見つける
	 * @param idx この4分木空間から rectangleに重なっているQuadTreeRectangleオブジェクトを見つける
	 * @param rectangle このオブジェクトに重なっているQuadTreeRectangleオブジェクトを見つける
	 * @param overlappedList 重なっているQuadTreeRectangleオブジェクトを格納するリスト
	 * */
	private void searchOverlappedRectsForOneSpace(int idx, QuadTreeRectangle rectangle, ArrayList<QuadTreeRectangle> overlappedList) {

		QuadTreeRectangle head = quadTree.get(idx);
		QuadTreeRectangle next = head.getNext();
		while (next != null) {
			if(next.overlapsWith(rectangle))
				overlappedList.add(next);
			next = next.getNext();
		}
	}

	/**
	 * 登録されているBhNodeの数を計算する (デバッグ用)
	 * @return 登録されているBhNodeの数
	 */
	public int calcRegisteredNodeNum() {
	
		int numOfNode = 0;
		for (QuadTreeRectangle head : quadTree) {
			QuadTreeRectangle rect = head;
			while ((rect = rect.getNext()) != null) {
				++numOfNode;
			}
		}
		
		QuadTreeRectangle rect = unknownSpaceListHead;
		while ((rect = rect.getNext()) != null) {
			++numOfNode;
		}
		return numOfNode;
	}
	
	/**
	 * 4分木空間の縦と横の分割数を返す
	 * @return 4分木空間の縦と横の分割数
	 */
	public int getNumPartitions() {
		return numPartitions;
	}
}
















