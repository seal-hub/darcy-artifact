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
import net.seapanda.bunnyhop.model.connective.ConnectiveNode;
import net.seapanda.bunnyhop.model.connective.Connector;
import net.seapanda.bunnyhop.model.connective.ConnectorSection;
import net.seapanda.bunnyhop.model.connective.Subsection;

/**
 * SymtaxSymbolオブジェクトにユニークなIDを付与する
 * @author K.Koike
 */
public class SyntaxSymbolIDCreator implements BhModelProcessor {

	private long id = 1;
	
	public SyntaxSymbolIDCreator() {}
	
	@Override
	public void visit(ConnectiveNode node) {
		node.setSymbolID(Long.toHexString(id++));
		node.introduceSectionsTo(this);
	}

	@Override
	public void visit(VoidNode node) {
		node.setSymbolID(Long.toHexString(id++));
	}

	@Override
	public void visit(TextNode node) {
		node.setSymbolID(Long.toHexString(id++));
	}

	@Override
	public void visit(Subsection section) {
		section.setSymbolID(Long.toHexString(id++));
		section.introduceSubsectionTo(this);
	}

	@Override
	public void visit(ConnectorSection connectorGroup) {
		connectorGroup.setSymbolID(Long.toHexString(id++));
		connectorGroup.introduceConnectorsTo(this);
	}

	@Override
	public void visit(Connector connector) {
		connector.setSymbolID(Long.toHexString(id++));
		connector.introduceConnectedNodeTo(this);
	}
}
