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
 * BhNode の各ノードに対して何かしらの処理を施すクラスのインタフェース
 * @author K.Koike
 * */
public interface BhModelProcessor {

	/**
	 *  ConnectiveNode が持つ onnerSection, outerSection に自オブジェクトを渡す
	 * @param node 自オブジェクトを渡してもらう ConnectiveNode オブジェクト
	 * */
	default public void visit(ConnectiveNode node) {
		node.introduceSectionsTo(this);
	}

	/**
	 * VoidNode を訪れた時の処理
	 * @param node BhModelProcessor が訪れる VoidNode
	 * */
	default public void visit(VoidNode node) {}

	/**
	 * TextNode を訪れた時の処理
	 * @param node BhModelProcessor が訪れる VoidNode
	 * */
	default public void visit(TextNode node) {}

	/**
	 * Section の下位Sectionに 自オブジェクトを渡す
	 * @param section 自オブジェクトを渡してもらう section オブジェクト
	 * */
	default public void visit(Subsection section) {
		section.introduceSubsectionTo(this);
	}

	/**
	 * ConnectorGroup が持つConnector に自オブジェクトを渡す
	 * @param connectorGroup 自オブジェクトを渡してもらう ConnectorGroup オブジェクト
	 * */
	default public void visit(ConnectorSection connectorGroup) {
		connectorGroup.introduceConnectorsTo(this);
	}

	/**
	 * Connector に接続された ノード に自オブジェクトを渡す
	 * @param connector 自オブジェクトを渡してもらう FixedConnector オブジェクト
	 * */
	default public void visit(Connector connector) {
		connector.introduceConnectedNodeTo(this);
	}
}
