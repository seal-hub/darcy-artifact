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
package net.seapanda.bunnyhop.model.templates;

import java.util.Optional;
import org.w3c.dom.Element;

import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.model.BhNodeID;
import net.seapanda.bunnyhop.view.BhNodeViewStyle;

/**
 * \<Node\> タグが持つ属性一覧
 * @author K.Koike
 */
public class BhNodeAttributes {
	
	public BhNodeID bhNodeID;
	public String name;
	public String nodeStyleID;
	public String onMovedFromChildToWS;
	public String onMovedToChild;
	public String onTextInput;
	public String imitScopeName;
	public String initString;
	public String nodeInputControlFileName;
	public boolean canCreateImitManually;
	
	private BhNodeAttributes(){}
	
	/**
	 * \<Node\> タグが持つ属性一覧を読み取る
	 * @param node \<Node\>タグを表すオブジェクト
	 */
	static Optional<BhNodeAttributes> readBhNodeAttriButes(Element node) {

		BhNodeAttributes nodeAttrs = new BhNodeAttributes();
		
		//bhNodeID
		nodeAttrs.bhNodeID = BhNodeID.createBhNodeID(node.getAttribute(BhParams.BhModelDef.ATTR_NAME_BHNODE_ID));
		if (nodeAttrs.bhNodeID.equals(BhNodeID.NONE)) {
			MsgPrinter.INSTANCE.errMsgForDebug(
				"<" + BhParams.BhModelDef.ELEM_NAME_NODE + ">" + " タグには " 
				+ BhParams.BhModelDef.ATTR_NAME_BHNODE_ID + " 属性を記述してください.  " + node.getBaseURI());
			return Optional.empty();
		}
		
		//name
		nodeAttrs.name = node.getAttribute(BhParams.BhModelDef.ATTR_NAME_NAME);
		
		// nodeStyleID
		String nodeStyleID = node.getAttribute(BhParams.NodeStyleDef.KEY_NODE_STYLE_ID);
		nodeAttrs.nodeStyleID = nodeStyleID.isEmpty() ? BhParams.BhModelDef.ATTR_VALUE_DEFAULT_NODE_STYLE_ID : nodeStyleID;
		BhNodeViewStyle.putNodeID_NodeStyleID(nodeAttrs.bhNodeID, nodeAttrs.nodeStyleID);
				
		//onMovedFromChildToWS
		nodeAttrs.onMovedFromChildToWS = node.getAttribute(BhParams.BhModelDef.ATTR_NAME_ON_MOVED_FROM_CHILD_TO_WS);
		
		//onMovedToChild
		nodeAttrs.onMovedToChild = node.getAttribute(BhParams.BhModelDef.ATTR_NAME_ON_MOVED_TO_CHILD);
		
		//onTextInput
		nodeAttrs.onTextInput = node.getAttribute(BhParams.BhModelDef.ATTR_NAME_ON_INPUT_TEXT);

		//imitScopeName
		nodeAttrs.imitScopeName = node.getAttribute(BhParams.BhModelDef.ATTR_NAME_IMIT_SCOPE_NAME);
		
		//initString
		nodeAttrs.initString = node.getAttribute(BhParams.BhModelDef.ATTR_NAME_INIT_STRING);
		
		//nodeInputControl
		nodeAttrs.nodeInputControlFileName = node.getAttribute(BhParams.BhModelDef.ATTR_NAME_NODE_INPUT_CONTROL);
		
		//canCreateImitManually
		String strCreateImit = node.getAttribute(BhParams.BhModelDef.ATTR_NAME_CAN_CREATE_IMIT_MANUALLY);
		if (strCreateImit.equals(BhParams.BhModelDef.ATTR_VALUE_TRUE)) {
			nodeAttrs.canCreateImitManually = true;
		}
		else if (strCreateImit.equals(BhParams.BhModelDef.ATTR_VALUE_FALSE) || strCreateImit.isEmpty()) {
			nodeAttrs.canCreateImitManually = false;
		}
		else {
			MsgPrinter.INSTANCE.errMsgForDebug(BhParams.BhModelDef.ATTR_NAME_CAN_CREATE_IMIT_MANUALLY + " 属性には "
				+ BhParams.BhModelDef.ATTR_VALUE_TRUE + " か "
				+ BhParams.BhModelDef.ATTR_VALUE_FALSE + " を指定してください. " + node.getBaseURI());
			return Optional.empty();
		}
		return Optional.of(nodeAttrs);
	}
	
}
