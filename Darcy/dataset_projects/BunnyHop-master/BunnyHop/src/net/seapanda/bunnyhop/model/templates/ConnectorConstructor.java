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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.model.BhNodeID;
import net.seapanda.bunnyhop.model.connective.Connector;
import net.seapanda.bunnyhop.model.connective.ConnectorID;

/**
 * \<Conncetor\> タグからコネクタを作成するクラス
 * @author K.Koike
 */
public class ConnectorConstructor {

	public ConnectorConstructor(){}
	
	/**
	 * コネクタテンプレートを作成する
	 * @param doc テンプレートを作成するxml の Document オブジェクト
	 * @return 作成したコネクタオブジェクト
	 */
	public Optional<Connector> genTemplate(Document doc) {

		//ルートタグチェック
		Element root = doc.getDocumentElement();
		if (!root.getNodeName().equals(BhParams.BhModelDef.ELEM_NAME_CONNECTOR)) {
			MsgPrinter.INSTANCE.errMsgForDebug("コネクタ定義のルート要素は " + BhParams.BhModelDef.ELEM_NAME_CONNECTOR + " で始めてください.  " + doc.getBaseURI());
			return Optional.empty();
		}
		return genTemplate(root);
	}
	
	/**
	 * コネクタテンプレートを作成する
	 * @param cnctrRoot \<Connector\> タグの要素
	 * @return 作成したコネクタオブジェクト
	 */
	public Optional<Connector> genTemplate(Element cnctrRoot) {
		
		//コネクタID
		ConnectorID cnctrID = ConnectorID.createCnctrID(cnctrRoot.getAttribute(BhParams.BhModelDef.ATTR_NAME_BHCONNECTOR_ID));
		if (cnctrID.equals(ConnectorID.NONE)) {
			MsgPrinter.INSTANCE.errMsgForDebug("<" + BhParams.BhModelDef.ELEM_NAME_CONNECTOR + ">" + " タグには "
				+ BhParams.BhModelDef.ATTR_NAME_BHCONNECTOR_ID + " 属性を付加してください.  " + cnctrRoot.getBaseURI());
			return Optional.empty();
		}

		//Fixed
		String fixedStr = cnctrRoot.getAttribute(BhParams.BhModelDef.ATTR_NAME_FIXED);
		if (!fixedStr.isEmpty() && !fixedStr.equals(BhParams.BhModelDef.ATTR_VALUE_TRUE) && !fixedStr.equals(BhParams.BhModelDef.ATTR_VALUE_FALSE)) {
			MsgPrinter.INSTANCE.errMsgForDebug("<" + BhParams.BhModelDef.ELEM_NAME_CONNECTOR + ">" + " タグの "
				+ BhParams.BhModelDef.ATTR_NAME_FIXED + " 属性は, " + cnctrRoot.getBaseURI()
				+ BhParams.BhModelDef.ATTR_VALUE_TRUE + "か" + BhParams.BhModelDef.ATTR_VALUE_FALSE + "で無ければなりません.  " + cnctrRoot.getBaseURI());
			return Optional.empty();
		}
		boolean fixed = fixedStr.equals(BhParams.BhModelDef.ATTR_VALUE_TRUE);

		//初期接続ノードID
		BhNodeID initNodeID = BhNodeID.createBhNodeID(cnctrRoot.getAttribute(BhParams.BhModelDef.ATTR_NAME_INITIAL_BHNODE_ID));
		
		//デフォルトノードID
		BhNodeID defNodeID;
		boolean hasFixedInitNode = fixed && !initNodeID.equals(BhNodeID.NONE);
		if (hasFixedInitNode) {	//初期ノードが固定ノードである => 初期ノードがデフォルトノードとなる
			defNodeID = initNodeID;
		}
		else{	
			defNodeID = BhNodeID.createBhNodeID(cnctrRoot.getAttribute(BhParams.BhModelDef.ATTR_NAME_DEFAULT_BHNODE_ID));
			if (defNodeID.equals(BhNodeID.REF_INIT))	{	//デフォルトノードに初期ノードを指定してある
				if (initNodeID.equals(BhNodeID.NONE)) {	//初期ノードが未指定
					MsgPrinter.INSTANCE.errMsgForDebug(
						BhParams.BhModelDef.ATTR_NAME_DEFAULT_BHNODE_ID + "が未指定の場合, "
						+ BhParams.BhModelDef.ATTR_VALUE_INITIAL_BHNODE_ID + " 属性を指定してください.");
					return Optional.empty();
				}
				else {	//デフォルトノード = 初期ノード
					defNodeID = initNodeID;
				}
			}
			
			if (defNodeID.equals(BhNodeID.REF_INIT)) {	//初期ノードが固定ノードではないのに, デフォルトノード
				MsgPrinter.INSTANCE.errMsgForDebug(
					"固定初期ノードを持たない "
					+ "<" + BhParams.BhModelDef.ELEM_NAME_CONNECTOR + "> および "
					+ "<" + BhParams.BhModelDef.ELEM_NAME_PRIVATE_CONNECTOR + "> タグは"
					+ BhParams.BhModelDef.ATTR_NAME_DEFAULT_BHNODE_ID + " 属性を持たなければなりません.  " + cnctrRoot.getBaseURI());
				return Optional.empty();
			}	
		}		
		
		//ノード入れ替え時の実行スクリプト
		String scriptNameOnReplaceabilityChecked = cnctrRoot.getAttribute(BhParams.BhModelDef.ATTR_NAME_ON_REPLACEABILITY_CHECKED);
		if (!BhNodeTemplates.allScriptsExist(cnctrRoot.getBaseURI(), scriptNameOnReplaceabilityChecked)) {
			return Optional.empty();
		}
		
		return Optional.of(new Connector(cnctrID, defNodeID, initNodeID, fixed, scriptNameOnReplaceabilityChecked));
	}
}
