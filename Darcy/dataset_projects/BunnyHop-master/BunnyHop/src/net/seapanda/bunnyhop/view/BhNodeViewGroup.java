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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javafx.scene.Group;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.Showable;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.model.connective.Connector;

/**
 * BhNodeView の集合を持つクラス
 * @author K.Koike
 * */
public class BhNodeViewGroup extends Group implements Showable {

	private final List<BhNodeViewGroup> subGroupList = new ArrayList<>();	//!< group が子に持つ BhNodeView のリスト
	private ConnectiveNodeView parentView;	//!<このグループを持つConnectiveNode
	private BhNodeViewGroup parentGroup;	//!< このグループを持つBhNodeViewGroup
	public final boolean inner;	//!< このグループが内部描画ノードを持つグループの場合true
	private BhNodeViewStyle.Arrangement arrangeParams; //!< ノード配置パラメータ
	private final Map<String, BhNodeView> cnctrName_NodeView = new HashMap<>();	//!< コネクタ名とそのコネクタにつながるBhNodeView
	private final Point2D size = new Point2D(0.0, 0.0);

	/**
	 * コンストラクタ
	 * @param parentView このグループを持つConnectiveNode
	 * @param inner このグループが外部描画ノードを持つグループの場合true
	 * */
	public BhNodeViewGroup(ConnectiveNodeView parentView, boolean inner) {
		this.parentView = parentView;
		this.inner = inner;
	}

	/**
	 * コンストラクタ
	 * @param parentGroup このグループを持つBhNodeViewGroup
	 * @param inner このグループが内部描画ノードを持つグループの場合true
	 */
	public BhNodeViewGroup(BhNodeViewGroup parentGroup, boolean inner) {
		this.parentGroup = parentGroup;
		this.inner = inner;
	}

	/**
	 * このノード以下のサブグループを作成する
	 * @param arrangeParams ノード配置パラメータ
	 */
	public void buildSubGroup(BhNodeViewStyle.Arrangement arrangeParams) {

		this.arrangeParams = arrangeParams;
		arrangeParams.cnctrNameList.forEach(cnctrName -> cnctrName_NodeView.put(cnctrName, null));
		for (String cnctrName : arrangeParams.cnctrNameList) {
			cnctrName_NodeView.put(cnctrName, null);
			if (!inner)	//外部ノードをつなぐコネクタは1つだけとする
				return;
		}

		arrangeParams.subGroup.forEach(subGroupParams -> {
			BhNodeViewGroup subGroup = new BhNodeViewGroup(this, inner);
			subGroup.buildSubGroup(subGroupParams);
			subGroupList.add(subGroup);
			getChildren().add(subGroup);
		});
	}

	/**
	 * BhNodeView を追加する
	 * @param view 追加するノードビュー
	 * @return
	 * */
	public boolean addNodeView(BhNodeView view) {

		Connector cnctr = view.getModel().getParentConnector();
		if (cnctr != null) {
			String cnctrName = cnctr.getSymbolName();
			if (cnctrName_NodeView.containsKey(cnctrName)) {	// このグループ内に追加すべき場所が見つかった
				cnctrName_NodeView.put(cnctrName, view);
				getChildren().add(view);
				view.getTreeManager().setParentGroup(this);
				cnctr.setOuterFlag(!inner);
				return true;
			}
			else {	//サブグループに追加する
				for (BhNodeViewGroup subGroup : subGroupList) {
					if (subGroup.addNodeView(view))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * このグループが持つoldNodeView をnewNodeViewと入れ替える. <br>
	 * ただし, 古いノードのGUIツリーからの削除は行わない
	 * @param oldNodeView 入れ替えられる古いノード
	 * @param newNodeView 入れ替える新しいノード
	 */
	public void replace(BhNodeView oldNodeView, BhNodeView newNodeView) {

		for (Entry<String, BhNodeView> entrySet : cnctrName_NodeView.entrySet()) {
			if (entrySet.getValue().equals(oldNodeView)) {
				entrySet.setValue(newNodeView);
				getChildren().remove(newNodeView);
				getChildren().add(0, newNodeView);
				newNodeView.getTreeManager().setParentGroup(this);	//親をセット
				oldNodeView.getTreeManager().setParentGroup(null);	//親を削除
				return;
			}
		}
	}

	/**
	 * 引数で指定したBhNodeView を GUIツリーから取り除く<Br>
	 * BhNodeView の木構造からは取り除かない
	 * @param removed BhNodeView の木構造から取り除かれるBhNodeView
	 * */
	public void removeFromGUITree(BhNodeView removed) {
		getChildren().remove(removed);
	}

	/**
	 * このグループの親ノードを返す
	 * @return このグループの親ノード
	 * */
	public ConnectiveNodeView getParentView() {

		if (parentView != null)
			return parentView;

		return parentGroup.getParentView();
	}

	/**
	 * 親ノードまたはグループからの相対位置を取得する
	 * @return 親ノードまたは親グループからの相対位置
	 */
	public Point2D getRelativePosFromParent() {
		return new Point2D(getTranslateX(), getTranslateY());
	}

	/**
	 * 親ノードまたはグループからの相対位置をセットする
	 * @param posX 親ノードまたは親グループからのX相対位置
	 * @param posY 親ノードまたは親グループからのY相対位置
	 */
	public void setRelativePosFromParent(double posX, double posY) {
		setTranslateX(posX);
		setTranslateY(posY);
	}

	public Point2D getSize() {
		return size;
	}

	/**
	 * このグループの絶対位置を更新する関数
	 * @param posX グループ左上のX絶対位置
	 * @param posY グループ左上のY絶対位置
	 * */
	public void updateAbsPos(double posX, double posY) {

		arrangeParams.cnctrNameList.forEach(cnctrName -> {
			BhNodeView childNodeView = cnctrName_NodeView.get(cnctrName);
			if (childNodeView != null) {
				Point2D relativePos = childNodeView.getPositionManager().getRelativePosFromParent();
				childNodeView.getPositionManager().updateAbsPos(posX + relativePos.x, posY + relativePos.y);
			}
		});

		subGroupList.forEach(subGroup -> {
			Point2D subGroupRelPos = subGroup.getRelativePosFromParent();
			subGroup.updateAbsPos(posX + subGroupRelPos.x, posY + subGroupRelPos.y);});
	}

	/**
	 * 子ノードの形状が変わったことを親ConnectiveNodeViewに伝える
	 * */
	public void updateStyle() {

		Point2D offset = calcOffset();
		Point2D relPos = new Point2D(offset.x, offset.y);
		Point2D sizeBefor = new Point2D(size.x, size.y);
		size.x = 0.0;
		size.y = 0.0;

		arrangeParams.cnctrNameList.forEach(cnctrName -> {
			BhNodeView childNodeView = cnctrName_NodeView.get(cnctrName);
			if (childNodeView != null) {
				Point2D cnctrSize = childNodeView.getConnectorManager().getConnectorSize();
				if (arrangeParams.arrangement == BhNodeViewStyle.CHILD_ARRANGEMENT.COLUMN &&
					childNodeView.getConnectorManager().getConnectorPos() == BhNodeViewStyle.CNCTR_POS.TOP) {	//グループの中が縦並びでかつコネクタが上に付く
					relPos.y += cnctrSize.y;
				}
				else if (arrangeParams.arrangement == BhNodeViewStyle.CHILD_ARRANGEMENT.ROW &&
						 childNodeView.getConnectorManager().getConnectorPos() == BhNodeViewStyle.CNCTR_POS.LEFT){	//グループの中が横並びでかつコネクタが左に付く
					relPos.x += cnctrSize.x;
				}
				childNodeView.getPositionManager().setRelativePosFromParent(relPos.x, relPos.y);
				Point2D childNodeSize = childNodeView.getRegionManager().getBodyAndOuterSize(false);
				updateChildRelativePos(relPos, childNodeSize, offset);
			}
		});

		subGroupList.forEach(subGroup -> {
			subGroup.setRelativePosFromParent(relPos.x, relPos.y);
			Point2D subGroupSize = subGroup.getSize();
			updateChildRelativePos(relPos, subGroupSize, offset);
		});

		if (arrangeParams.arrangement == BhNodeViewStyle.CHILD_ARRANGEMENT.COLUMN) {	//グループの中が縦並び
			size.y = relPos.y - arrangeParams.interval;
		}
		else {	//グループの中が横並び
			size.x = relPos.x - arrangeParams.interval;
		}

		if (sizeBefor.equals(size)) {
			Point2D posOnWS = BhNodeView.getRelativePos(null, this);
			updateAbsPos(posOnWS.x, posOnWS.y);
			return;
		}

		if (parentView != null)
			parentView.getAppearanceManager().updateStyle(this);
		else
			parentGroup.updateStyle();
	}

	/**
	 * 子ノードとサブグループのこのノードからの相対位置を更新する
	 * @param relPos 相対位置
	 * @param childSize 子ノードまたはサブグループの大きさ
	 * @param offset 子ノードまたはサブグループの位置のオフセット
	 */
	private void updateChildRelativePos(Point2D relPos, Point2D childSize, Point2D offset) {

		if (arrangeParams.arrangement == BhNodeViewStyle.CHILD_ARRANGEMENT.COLUMN) {	//グループの中が縦並び
			relPos.y += childSize.y + arrangeParams.interval;
			size.x = Math.max(size.x, childSize.x + offset.x);
		}
		else {	//グループの中が横並び
			relPos.x += childSize.x + arrangeParams.interval;
			size.y = Math.max(size.y, childSize.y + offset.y);
		}
	}

	/**
	 * 子ノードとサブグループを並べる際のオフセットを計算する
	 */
	private Point2D calcOffset() {

		double offsetX = 0.0;
		double offsetY = 0.0;

		//外部ノードは, 先頭のコネクタの長さ分オフセットをマイナスしておく
		if (!inner) {
			for (String cnctrName : arrangeParams.cnctrNameList) {
				BhNodeView childNodeView = cnctrName_NodeView.get(cnctrName);
				if (childNodeView == null)
					continue;

				Point2D cnctrSize = childNodeView.getConnectorManager().getConnectorSize();
				if (arrangeParams.arrangement == BhNodeViewStyle.CHILD_ARRANGEMENT.COLUMN &&
					childNodeView.getConnectorManager().getConnectorPos() == BhNodeViewStyle.CNCTR_POS.TOP) {	//グループの中が縦並びでかつコネクタが上に付く
					offsetY -= cnctrSize.y;
				}
				else if (arrangeParams.arrangement == BhNodeViewStyle.CHILD_ARRANGEMENT.ROW &&
					childNodeView.getConnectorManager().getConnectorPos() == BhNodeViewStyle.CNCTR_POS.LEFT){	//グループの中が横並びでかつコネクタが左に付く
					offsetX -= cnctrSize.x;
				}
				return new Point2D(offsetX, offsetY);
			}
		}

		for (String cnctrName : arrangeParams.cnctrNameList) {
			BhNodeView childNodeView = cnctrName_NodeView.get(cnctrName);
			if (childNodeView == null)
				continue;

			Point2D cnctrSize = childNodeView.getConnectorManager().getConnectorSize();
			if (arrangeParams.arrangement == BhNodeViewStyle.CHILD_ARRANGEMENT.COLUMN &&
				childNodeView.getConnectorManager().getConnectorPos() == BhNodeViewStyle.CNCTR_POS.LEFT) {	//グループの中が縦並びでかつコネクタが左に付く
				offsetX = Math.max(offsetX, cnctrSize.x);
			}
			else if (arrangeParams.arrangement == BhNodeViewStyle.CHILD_ARRANGEMENT.ROW &&
				 childNodeView.getConnectorManager().getConnectorPos() == BhNodeViewStyle.CNCTR_POS.TOP){	//グループの中が横並びでかつコネクタが上に付く
				offsetY = Math.max(offsetY, cnctrSize.y);
			}
		}
		return new Point2D(offsetX, offsetY);
	}

	/**
	 * BhNodeView を引数にとる関数オブジェクトを子ノードに渡す<br>
	 * @param visitorFunc BhNodeView を引数にとり処理するオブジェクト
	 * */
	public void accept(Consumer<BhNodeView> visitorFunc) {

		arrangeParams.cnctrNameList.forEach(cnctrName -> {
			BhNodeView childNodeView =  cnctrName_NodeView.get(cnctrName);
			if (childNodeView != null)
				childNodeView.accept(visitorFunc);
		});

		subGroupList.forEach(subGroup -> subGroup.accept(visitorFunc));
	}

	public void toForeGround() {

		toFront();
		if (parentGroup != null)
			parentGroup.toForeGround();

		if (parentView != null)
			parentView.getAppearanceManager().toForeGround();
	}

	@Override
	public void show(int depth) {

		try {
			MsgPrinter.INSTANCE.msgForDebug(indent(depth) + "<BhNodeViewGroup>  "  + this.hashCode());
			MsgPrinter.INSTANCE.msgForDebug(indent(depth + 1) + (inner ? "<inner>" : "<outer>"));
			arrangeParams.cnctrNameList.forEach(cnctrName -> {
				BhNodeView childNodeView =  cnctrName_NodeView.get(cnctrName);
				if (childNodeView != null)
					childNodeView.show(depth + 1);
			});
			subGroupList.forEach(subGroup -> subGroup.show(depth + 1));
		}
		catch (Exception e) {
			MsgPrinter.INSTANCE.msgForDebug("connectiveNodeView show exception " + e);
		}
	}
}






