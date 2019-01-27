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
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.connective.ConnectiveNode;
import net.seapanda.bunnyhop.model.connective.Connector;
import net.seapanda.bunnyhop.model.connective.ConnectorSection;
import net.seapanda.bunnyhop.model.connective.Subsection;
import net.seapanda.bunnyhop.model.imitation.ImitationConnectionPos;

/**
 * イミテーションタグを指定し, 接続されているBhNode を見つける
 * @author K.Koike
 */
public class ConnectiveChildFinder implements BhModelProcessor {
	
	BhNode foundNode;	//!< 見つかったノード
	ImitationConnectionPos imitCnctPos;	//!< 接続されている探したい接続先のコネクタ名
	private boolean found = false;
	
	/**
	 * コンストラクタ
	 * @param imitCnctPos このイミテーションタグを持つコネクタにつながったBhNodeを見つける
	 */
	public ConnectiveChildFinder(ImitationConnectionPos imitCnctPos){
		this.imitCnctPos = imitCnctPos;
	}
	
	@Override
	public void visit(ConnectiveNode node) {
		node.introduceSectionsTo(this);
	}

	@Override
	public void visit(Subsection section) {
		if (found)
			return;
		section.introduceSubsectionTo(this);
	}
	
	@Override
	public void visit(ConnectorSection connectorGroup) {
		if (found)
			return;
		connectorGroup.introduceConnectorsTo(this);
	}

	@Override
	public void visit(Connector connector) {
		if (found)
			return;
		
		if (connector.getImitCnctPoint().equals(imitCnctPos)) {
			foundNode = connector.getConnectedNode();
			found = true;
		}
	}
	
	/**
	 * コネクティブノードを捜査した結果見つかったBhNode を返す. <br>
	 * 何も見つからなかった場合は null を返す
	 * @return コネクティブノードを捜査した結果見つかったBhNode
	 */
	public BhNode getFoundNode() {
		return foundNode;
	}
}
