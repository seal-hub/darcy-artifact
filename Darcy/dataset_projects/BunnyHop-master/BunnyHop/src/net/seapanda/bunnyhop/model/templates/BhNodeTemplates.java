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

import java.io.IOException;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Pair;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.connective.Connector;
import net.seapanda.bunnyhop.configfilereader.BhScriptManager;
import net.seapanda.bunnyhop.model.BhNodeID;
import net.seapanda.bunnyhop.model.connective.ConnectorID;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * ノード生成時のテンプレートを保持するクラス
 * @author K.Koike
 * */
public class BhNodeTemplates {

	public static final BhNodeTemplates INSTANCE = new BhNodeTemplates(); //!< シングルトンインスタンス

	private final HashMap<BhNodeID, BhNode> nodeID_nodeTemplate = new HashMap<>(); //!< ノードのテンプレートを格納するハッシュ. Nodeタグの bhID がキー
	private final HashMap<ConnectorID, Connector> cnctrID_cntrTemplate = new HashMap<>(); //!< コネクタのテンプレートを格納するハッシュ. Connectorタグの bhID がキー
	private final List<Pair<BhNodeID, BhNodeID>> orgNodeID_imitNodeID = new ArrayList<>();	// オリジナルノードと, そのイミテーションノードのIDを格納する.
		
	private BhNodeTemplates() {}
	
	/**
	 * ノードIDからBhNode のテンプレートを取得する
	 * @param id 取得したいノードのID
	 * @return id で指定したBhNodeのテンプレート.
	 * */
	private Optional<BhNode> getBhNodeTemplate(BhNodeID id) {
		return Optional.ofNullable(nodeID_nodeTemplate.get(id));
	}

	/**
	 * ノードIDからBhNode を新しく作る
	 * @param id 取得したいノードのID
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * @return id で指定したBhNodeのオブジェクト
	 * */
	public BhNode genBhNode(BhNodeID id, UserOperationCommand userOpeCmd) {

		BhNode newNode = nodeID_nodeTemplate.get(id);
		if (newNode == null) {
			MsgPrinter.INSTANCE.errMsgForDebug(BhNodeTemplates.class.getSimpleName() +  ".genBhNode template not found" + id);
		}
		else {
			newNode = newNode.copy(userOpeCmd);
		}
		return newNode;
	}

	/**
	 * 引数で指定したIDを持つBhNode が存在するかどうかを返す
	 * @param id 存在を確認するBhNode のID
	 * @return BhNodeが存在していた場合true
	 * */
	public boolean bhNodeExists(BhNodeID id) {
		return nodeID_nodeTemplate.get(id) != null;
	}

	/**
	 * テンプレートを作成する
	 * @return テンプレートノードの作成に成功した場合true
	 * */
	public boolean genTemplate() {

		boolean success = true;	//全てのファイルから正しくテンプレートを作成できた場合 true
		success &= genConnectorTemplate();
		success &= genNodeTemplate();
		if (!success)
			return false;

		success &= registerDefaultNodeWithConnector();
		success &= genCompoundNodes();
		return success;
	}

	/**
	 * コネクタのテンプレートを作成し, ハッシュに格納する
	 * @return 成功した場合 true
	 * */
	private boolean genConnectorTemplate() {

		//コネクタファイルパスリスト取得
		Path dirPath = Paths.get(Util.EXEC_PATH, BhParams.Path.BH_DEF_DIR, BhParams.Path.CONNECTOR_DEF_DIR);
		Stream<Path> files;	//読み込むファイルパスリスト
		try {
			files = Files.walk(dirPath, FOLLOW_LINKS).filter(path -> path.toString().endsWith(".xml"));
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("connector directory not found " + dirPath);
			return false;
		}

		//コネクタ設定ファイル読み込み
		boolean success = files.map(file -> {
				try {
					DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = dbfactory.newDocumentBuilder();
					Document doc = builder.parse(file.toFile());
					Optional<? extends Connector> templateConnector = new ConnectorConstructor().genTemplate(doc);
					templateConnector.ifPresent(connector -> registerCnctrTemplate(connector.getID(), connector));	//テンプレート格納ハッシュに格納
					return templateConnector.isPresent();
				}
				catch (IOException | ParserConfigurationException | SAXException e) {
					MsgPrinter.INSTANCE.errMsgForDebug("ConnectorTemplates genTemplate \n" + e.toString() + "\n" +  file);
					return false;
				}
			}).allMatch(successful -> successful) ;

		files.close();
		return success;
	}

	/**
	 * ノードのテンプレートを作成し, ハッシュに格納する
	 * @return 成功した場合 true
	 * */
	private boolean genNodeTemplate() {

		//ノードファイルパスリスト取得
		Path dirPath = Paths.get(Util.EXEC_PATH, BhParams.Path.BH_DEF_DIR, BhParams.Path.NODE_DEF_DIR);
		Stream<Path> files;	//読み込むファイルパスリスト
		try {
			files = Files.walk(dirPath, FOLLOW_LINKS).filter(path -> path.toString().endsWith(".xml"));
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("node directory not found " + dirPath);
			return false;
		}
		
		//ノード設定ファイル読み込み
		boolean success = !files.map(file -> {
				try {
					DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = dbfactory.newDocumentBuilder();
					Document doc = builder.parse(file.toFile());
					Optional<? extends BhNode> templateNode = 
						new NodeConstructor(
							this::registerNodeTemplate, 
							this::registerCnctrTemplate,
							this::registerOrgNodeIdAndImitNodeID,
							this::getCnctrTemplate)
							.genTemplate(doc);
					templateNode.ifPresent(node -> nodeID_nodeTemplate.put(node.getID(), node));
					return templateNode.isPresent();
				}
				catch (IOException | ParserConfigurationException | SAXException e) {
					MsgPrinter.INSTANCE.errMsgForDebug("NodeTemplates genTemplate \n" + e.toString() + "\n" +  file);
					return false;
				}
			}).anyMatch(isSuccessful -> !isSuccessful);
		files.close();
		success &= checkImitationConsistency(orgNodeID_imitNodeID);
		return success;
	}

	/**
	 * コネクタに最初につながっているノードをテンプレートコネクタに登録する
	 * @return 全てのコネクタに対し、ノードの登録が成功した場合 true を返す
	 * */
	private boolean registerDefaultNodeWithConnector() {

		//コネクタに最初につながっているノード(defaultNode) をコネクタに登録する
		boolean success = cnctrID_cntrTemplate.values().stream().map(connector -> {
				BhNodeID defNodeID = connector.defaultNodeID;
				Optional<BhNode> defNode = getBhNodeTemplate(defNodeID);
				BhNodeID initNodeID = connector.initNodeID;
				Optional<BhNode> initNode = getBhNodeTemplate(initNodeID);		
				
				//ノードテンプレートが見つからない
				if (!defNode.isPresent()) {
					MsgPrinter.INSTANCE.errMsgForDebug(
						"<" + BhParams.BhModelDef.ELEM_NAME_CONNECTOR + ">" + " タグの "
							+ BhParams.BhModelDef.ATTR_NAME_DEFAULT_BHNODE_ID + " (" + defNodeID +") " + "と一致する " 
							+ BhParams.BhModelDef.ATTR_NAME_BHNODE_ID + " を持つ"
							+ BhParams.BhModelDef.ELEM_NAME_NODE + " の定義が見つかりません.");
					return false;
				}
				else if (!initNode.isPresent()) {
					MsgPrinter.INSTANCE.errMsgForDebug(
						"<" + BhParams.BhModelDef.ELEM_NAME_CONNECTOR + ">" + " タグの "
							+ BhParams.BhModelDef.ATTR_NAME_INITIAL_BHNODE_ID + " (" + initNodeID +") " + "と一致する " 
							+ BhParams.BhModelDef.ATTR_NAME_BHNODE_ID + " を持つ "
							+ BhParams.BhModelDef.ELEM_NAME_NODE + " の定義が見つかりません.");
					return false;				
				}
				else {
					connector.connectNode(initNode.get(), null);
					return true;
				}
			}).allMatch(bool -> bool);

		return success;
	}

	/**
	 * イミテーションノードの整合性を検査する
	 * @param orgNodeID_imitNodeID オリジナルノードとイミテーションノードのIDのリスト
	 * @return オリジナルノードとイミテーションノードの整合性が取れていた場合 true
	 */
	private boolean checkImitationConsistency(List<Pair<BhNodeID, BhNodeID>> orgNodeID_imitNodeID) {

		boolean hasConsistency = orgNodeID_imitNodeID.stream().allMatch(orgID_imitID -> {
			
			//イミテーションノードの存在チェック
			if (!bhNodeExists(orgID_imitID._2)) {
				MsgPrinter.INSTANCE.errMsgForDebug(
					"\"" + orgID_imitID._2 + "\"" + " を " 
					+ BhParams.BhModelDef.ATTR_NAME_BHNODE_ID + " に持つ " 
					+ BhParams.BhModelDef.ELEM_NAME_NODE + " が見つかりません. " + "(" + orgID_imitID._1 + ")");
				return false;
			}
			
			//オリジナルノードとイミテーションノードの型一致チェック
			Optional<BhNode> orgNodeOpt = getBhNodeTemplate(orgID_imitID._1);
			Optional<BhNode> imitNodeOpt = getBhNodeTemplate(orgID_imitID._2);
			boolean isSameType = orgNodeOpt.get().getClass() == imitNodeOpt.get().getClass();
			if (!isSameType) {
				MsgPrinter.INSTANCE.errMsgForDebug(
					BhParams.BhModelDef.ATTR_NAME_TYPE + " が " + orgNodeOpt.get().type + " の " + BhParams.BhModelDef.ELEM_NAME_NODE + " は " 
				  + BhParams.BhModelDef.ATTR_NAME_TYPE + " が " + imitNodeOpt.get().type + " の " + BhParams.BhModelDef.ELEM_NAME_NODE + " を "
				  + BhParams.BhModelDef.ATTR_NAME_IMITATION_NODE_ID + " に指定できません. \n"
				  + "org: " + orgID_imitID._1 + "    imit: " + orgID_imitID._2);
			}
			return isSameType;
		});		
		return hasConsistency;
	}
	
	/**
	 * ノードIDとノードテンプレートを登録する
	 * @param nodeID bhNodeのID
	 * @param nodeTemplate bhNodeテンプレート
	 */
	public void registerNodeTemplate(BhNodeID nodeID, BhNode nodeTemplate) {
		nodeID_nodeTemplate.put(nodeID, nodeTemplate);
	}
	
	/**
	 * コネクタIDとコネクタテンプレートを登録する
 	 * @param cnctrID コネクタID
	 * @param cnctrTemplate コネクタテンプレート

	 */
	private void registerCnctrTemplate(ConnectorID cnctrID, Connector cnctrTemplate) {
		cnctrID_cntrTemplate.put(cnctrID, cnctrTemplate);
	}
	
	/**
	 * オリジナルノードのIDと, そのイミテーションノードのIDを登録する
	 * @param orgNodID オリジナルノードのID
	 * @param imitNodeID イミテーションノードのID
	 */
	private void registerOrgNodeIdAndImitNodeID(BhNodeID orgNodeID, BhNodeID imitNodeID) {
		orgNodeID_imitNodeID.add(new Pair<>(orgNodeID, imitNodeID));
	}
	
	/**
	 * コネクタテンプレートを取得する
	 * @param cnctrID このIDを持つコネクタのテンプレートを取得する
	 * @return コネクタテンプレート
	 */
	private Optional<Connector> getCnctrTemplate(ConnectorID cnctrID) {
		return Optional.ofNullable(cnctrID_cntrTemplate.get(cnctrID));
	}
	
	
	/**
	 * 複合ノードを作成する
	 */
	private boolean genCompoundNodes() {
		
		CompiledScript cs = BhScriptManager.INSTANCE.getCompiledScript(BhParams.Path.GEN_COMPOUND_NODES_JS);
		Bindings scriptScope = BhScriptManager.INSTANCE.createScriptScope();
		scriptScope.put(BhParams.JsKeyword.KEY_BH_USER_OPE_CMD, new UserOperationCommand());
		scriptScope.put(BhParams.JsKeyword.KEY_BH_NODE_TEMPLATES, INSTANCE);
		try {
			cs.eval(scriptScope);
		}
		catch (ScriptException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("eval " + BhParams.Path.GEN_COMPOUND_NODES_JS + "\n" + e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * BhNode の処理時に呼ばれるスクリプトが存在するかどうか調べる <br>
	 * ただし, スクリプト名が nullか空文字だった場合, そのスクリプトの存在は調べない
	 * @param fileName scriptNames が書いてあるファイル名
	 * @param scriptNames 実行されるスクリプト名
	 * @return nullか空文字以外のスクリプト名に対応するスクリプトが全て見つかった場合 trueを返す
	 * */
	public static boolean allScriptsExist(String fileName, String... scriptNames) {
			
		String[] scriptNamesFiltered = Stream.of(scriptNames)
			.filter(scriptName -> {
				if (scriptName != null)
					if (!scriptName.isEmpty())
						return true;
				return false;})
			.toArray(String[]::new);
		
		return BhScriptManager.INSTANCE.scriptsExist(fileName, scriptNamesFiltered);
	}
		
	/**
	 * 指定した名前を持つ子タグを探す
	 * @param elem         子タグを探すタグ
	 * @param childTagName 探したいタグ名
	 * @return childTagName で指定した名前を持つタグリスト
	 * */
	public static List<Element> getElementsByTagNameFromChild(Element elem, String childTagName) {

		ArrayList<Element> selectedElementList = new ArrayList<>();
		for (int i = 0; i < elem.getChildNodes().getLength(); ++i) {
			Node node = elem.getChildNodes().item(i);
			if (node instanceof Element) {
				Element tag = (Element) node;
				if (tag.getTagName().equals(childTagName))
					selectedElementList.add(tag);
			}
		}
		return selectedElementList;
	}
}
