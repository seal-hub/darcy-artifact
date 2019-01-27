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
package net.seapanda.bunnyhop.modelprocessor;

import java.util.Deque;
import java.util.LinkedList;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.TextNode;
import net.seapanda.bunnyhop.model.connective.ConnectiveNode;
import net.seapanda.bunnyhop.model.imitation.Imitatable;
import net.seapanda.bunnyhop.model.imitation.ImitationID;
import net.seapanda.bunnyhop.undo.UserOperationCommand;
	
/**
 * イミテーションノードツリーを作成する
 * @author K.Koike
 */
public class ImitationBuilder implements BhModelProcessor {
	
	final private Deque<Imitatable> parentImitStack = new LinkedList<>();	//!< 現在処理中のBhNode の親がトップにくるスタック
	UserOperationCommand userOpeCmd;	//!< undo用コマンドオブジェクト
	boolean isManualCreation = false;	//!< トップノードのイミテーションを手動作成する場合true
		
	public ImitationBuilder(UserOperationCommand userOpeCmd) {
		this.userOpeCmd = userOpeCmd;
	}
	
	public ImitationBuilder(UserOperationCommand userOpeCmd, boolean isManualCreation) {
		this.userOpeCmd = userOpeCmd;
		this.isManualCreation = isManualCreation;
	}	
	
	/**
	 * 作成したイミテーションノードツリーのトップノードを取得する
	 * @return 作成したイミテーションノードツリーのトップノード
	 */
	public Imitatable getTopImitation() {
		return parentImitStack.peekLast();
	}

	/**
	 * @param node イミテーションを作成して、入れ替えを行いたいオリジナルノード
	 */
	@Override
	public void visit(ConnectiveNode node) {
		
		ImitationID imitID = null;
		if (isManualCreation) {
			imitID = ImitationID.MANUAL;
			isManualCreation = false;
		}
		else if (node.getParentConnector() != null) {
			imitID = node.getParentConnector().findImitationID();
		}
		
		if (!node.getImitationInfo().imitationNodeExists(imitID))
			return;
		
		if (parentImitStack.isEmpty()) {
			ConnectiveNode newImit = node.createImitNode(userOpeCmd, imitID);
			parentImitStack.addLast(newImit);
			node.introduceSectionsTo(this);
			newImit.accept(new NodeMVCBuilder(NodeMVCBuilder.ControllerType.Default));
			newImit.accept(new TextImitationPrompter());
		}
		else {
			Imitatable parentImit = parentImitStack.peekLast();
			//接続先を探す
			ConnectiveChildFinder finder = new ConnectiveChildFinder(node.getParentConnector().getImitCnctPoint());
			parentImit.accept(finder);
			BhNode oldImit = finder.getFoundNode();
			if (oldImit != null) {
				ConnectiveNode newImit = node.createImitNode(userOpeCmd, imitID);
				oldImit.replacedWith(newImit, userOpeCmd);
				parentImitStack.addLast(newImit);
				node.introduceSectionsTo(this);
				parentImitStack.removeLast();
			}
		}
	}
	
	@Override
	public void visit(TextNode node) {
		
		ImitationID imitID = null;
		if (isManualCreation) {
			imitID = ImitationID.MANUAL;
			isManualCreation = false;
		}
		else if (node.getParentConnector() != null) {
			imitID = node.getParentConnector().findImitationID();
		}
		
		if (!node.getImitationInfo().imitationNodeExists(imitID))
			return;
		
		if (parentImitStack.isEmpty()) {
			TextNode newImit = node.createImitNode(userOpeCmd, imitID);
			parentImitStack.addLast(newImit);
			newImit.accept(new NodeMVCBuilder(NodeMVCBuilder.ControllerType.Default));
			newImit.accept(new TextImitationPrompter());
		}
		else {
			Imitatable parentImit = parentImitStack.peekLast();
			//接続先を探す
			ConnectiveChildFinder finder = new ConnectiveChildFinder(node.getParentConnector().getImitCnctPoint());
			parentImit.accept(finder);
			BhNode oldImit = finder.getFoundNode();
			if (oldImit != null) {
				TextNode newImit = node.createImitNode(userOpeCmd, imitID);
				oldImit.replacedWith(newImit, userOpeCmd);
			}			
		}
	}
}
