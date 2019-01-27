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

import net.seapanda.bunnyhop.model.TextNode;
import net.seapanda.bunnyhop.model.VoidNode;
import net.seapanda.bunnyhop.model.Workspace;
import net.seapanda.bunnyhop.model.connective.ConnectiveNode;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * ノードに対してワークスペースをセットする
 * @author K.Koike
 */
public class WorkspaceRegisterer implements BhModelProcessor{
	
	private final Workspace ws;	//!< 登録されるワークスペース
	private final UserOperationCommand userOpeCmd;
	
	/**
	 * @param ws 登録されるワークスペース<br> nullを指定するとノードと所属しているワークスペースの関連が無くなる
	 * @param userOpeCmd undo用コマンドオブジェクト
	 */
	public WorkspaceRegisterer(Workspace ws, UserOperationCommand userOpeCmd) {
		this.ws = ws;
		this.userOpeCmd = userOpeCmd;
	}
	
	@Override
	public void visit(ConnectiveNode node) {
		node.setWorkspace(ws, userOpeCmd);
		node.introduceSectionsTo(this);
	}
	
	@Override
	public void visit(VoidNode node) {
		node.setWorkspace(ws, userOpeCmd);
	}

	@Override
	public void visit(TextNode node) {
		node.setWorkspace(ws, userOpeCmd);
	}
}
