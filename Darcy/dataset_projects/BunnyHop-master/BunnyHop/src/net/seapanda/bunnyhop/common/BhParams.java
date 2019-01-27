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
package net.seapanda.bunnyhop.common;

/**
 * パラメータ一式をまとめたクラス
 * @author K.Koike
 */
public class BhParams {

	public static final String APPLICATION_NAME = "BunnyHop";
	public static final String APP_VERSION = "1.0.0.0";
	public static final double DEFAULT_APP_WIDTH_RATE = 0.7; // !< 起動時の画面幅のディスプレイに対する割合
	public static final double DEFAULT_APP_HEIGHT_RATE = 0.7; // !< 起動時の画面高さのディスプレイに対する割合
	public static final double DEFAULT_WORKSPACE_WIDTH = 200 * Rem.VAL;
	public static final double DEFAULT_WORKSPACE_HEIGHT = 200 * Rem.VAL;
	public static final double DEFAULT_VERTICAL_DIV_POS = 0.85;	//!< ワークスペースとメッセージエリアを分けるディバイダの初期位置
	public static final double NODE_SCALE = 0.5 * Rem.VAL;
	public static final double ZOOM_MAGNIFICATION = 1.2;		//!< ctrl + マウスホイールや拡大, 縮小ボタンを押したときの拡大縮小倍率
	public static final double MAX_ZOOM_LEVEL = 20;		//!< 最大拡大レベル
	public static final double MIN_ZOOM_LEVEL = -20;	//!< 最小拡大レベル
	public static final int INITIAL_ZOOM_LEVEL = -1;
	public static final double REPLACED_NODE_POS = 2.0 * BhParams.NODE_SCALE;	//!< 入れ替えられたノードがワークスペースに移ったときの元の位置に対する位置 (単位rem)
	public static final double BHNODE_SPACE_ON_SELECTION_PANEL = 2.0 * BhParams.NODE_SCALE;		//!< BhNode選択パネル上でのBhNode間のスペース
	public static final int NUM_TIMES_MAX_UNDO = 128;	//!< undo 可能な最大回数
	public static final int MAX_MAIN_MSG_AREA_CHARS = 131072;	//!< メインメッセージエリアの最大表示文字数
	public static final int MAX_MAIN_MSG_QUEUE_SIZE = 2048;	//!< メインメッセージエリアの表示文字列バッファサイズ
	public static final int NUM_DIV_OF_QTREE_SPACE = 4;	//!< 4分木空間の分割数 (2^numDivOfQTreeSpace)^2
	public static final double MAX_WORKSPACE_SIZE_LEVEL = 3;		//!< ワークスペースの最大の大きさレベル
	public static final double MIN_WORKSPACE_SIZE_LEVEL = -1;	//!< ワークスペースの最小の大きさレベル
	public static final String INITIAL_WORKSPACE_NAME = "メイン";	//!< 最初からあるワークスペースの名前
	public static final  int EXECUTOR_SHUTDOWN_TIMEOUT = 5;	//!< ExecutorService のシャットダウンを待つ時間 (sec)
	public static final int MAX_LOG_FILE_NUM = 4;	//!< ログファイルの最大個数
	public static final int LOG_FILE_SIZE_LIMIT = 1024 * 1024;	//!< ログファイル1つあたりの最大バイト数

	/**
	 * ファイルパス関連のパラメータ
	 */
	public static class Path {
		public static final String VIEW_DIR = "BhView"; //!< fxmlとcssファイルのあるフォルダ名
		public static final String CSS_DIR = "css";	//!< cssファイルのあるフォルダ名
		public static final String FXML_DIR = "fxml";	//!< fxmlファイルのあるフォルダ名
		public static final String BH_DEF_DIR = "BhDefine"; //!< ノードやコネクタの定義フォルダがあるフォルダ名
		public static final String NODE_DEF_DIR = "Node"; //!< ノード定義ファイルがあるフォルダ名
		public static final String CONNECTOR_DEF_DIR = "Connector"; //!< コネクタ定義ファイルがあるフォルダ名
		public static final String NODE_STYLE_DEF_DIR = "NodeStyle"; //!< ノードのスタイル定義ファイルがあるフォルダ名
		public static final String FUNCTIONS_DIR = "Functions";	//!< Javascript コードが書かれたファイルのあるトップフォルダ
		public static final String TEMPLATE_LIST_DIR = "TemplateList";	//ノードテンプレートの配置情報が書かれたファイルがあるフォルダ名
		public static final String LOG_DIR = "Log";
		public static final String LOG_FILE_NAME = "msg";
		public static final String lib = "lib";
		public static final String COMPILED_DIR = "Compiled";
		public static final String REMOTE_DIR = "Remote";
		public static final String FOUNDATION_FXML = "Foundation.fxml"; //!< アプリの基底部分のビューが定義してあるfxmlファイルの名前
		public static final String WORKSPACE_FXML = "Workspace.fxml"; // !< ワークスペース部分のビューが定義してあるfxmlファイルの名前
		public static final String NODE_SELECTION_PANEL_FXML = "NodeSelectionPanel.fxml";	//!< BhNodeテンプレートリスト部分のビューが定義してあるfxmlの名前
		public static final String IMIT_BUTTON_FXML = "ImitButton.fxml";	//!< イミテーションボタンが定義してあるfxmlの名前
		public static final String NODE_TEMPLATE_LIST_JSON = "NodeTemplateList.json";	//!< ノードテンプレートの配置情報が書かれたファイルの名前
		public static final String COMMON_EVENT_JS = "CommonEvent.js";
		public static final String COMMON_CODE_JS = "CommonCode.js";
		public static final String LOCAL_COMMON_CODE_JS = "LocalCommonCode.js";
		public static final String REMOTE_COMMON_CODE_JS = "RemoteCommonCode.js";
		public static final String APP_FILE_NAME_JS = "BhAppScript.js";
		public static final String GEN_COMPOUND_NODES_JS = "genCompoundNodes.js";
		public static final String REMOTE_EXEC_CMD_GENERATOR_JS = "remoteExecCmdGenerator.js";	//!< リモートのBhProgram実行環境をスタートさせるコマンドを生成するスクリプト名
		public static final String REMOTE_KILL_CMD_GENERATOR_JS = "remoteKillCmdGenerator.js";	//!< リモートのBhProgram実行環境を終わらせるコマンドを生成するスクリプト名
		public static final String COPY_CMD_GENERATOR_JS = "copyCmdGenerator.js";	//!< リモートのBhProgram実行環境にBhProgramファイルをコピーするコマンドを生成するスクリプト名
	}

	/**
	 * ノードやコネクタ定義のパラメータ
	 */
	public static class BhModelDef {
		public static final String ELEM_NAME_NODE = "Node";
		public static final String ELEM_NAME_SECTION = "Section";
		public static final String ELEM_NAME_CONNECTOR = "Connector";
		public static final String ELEM_NAME_PRIVATE_CONNECTOR = "PrivateConnector";
		public static final String ELEM_NAME_CONNECTOR_SECTION = "ConnectorSection";
		public static final String ELEM_NAME_IMITATION = "Imitation";
		public static final String ATTR_NAME_BHNODE_ID = "bhNodeID";
		public static final String ATTR_NAME_BHCONNECTOR_ID = "bhConnectorID";
		public static final String ATTR_NAME_DEFAULT_BHNODE_ID = "defaultBhNodeID";
		public static final String ATTR_NAME_INITIAL_BHNODE_ID = "initialBhNodeID";
		public static final String ATTR_NAME_TYPE = "type";
		public static final String ATTR_NAME_NAME = "name";
		public static final String ATTR_NAME_FIXED = "fixed";
		public static final String ATTR_NAME_ON_INPUT_TEXT = "onTextInput";
		public static final String ATTR_NAME_ON_MOVED_FROM_CHILD_TO_WS = "onMovedFromChildToWS";
		public static final String ATTR_NAME_ON_MOVED_TO_CHILD = "onMovedToChild";
		public static final String ATTR_NAME_ON_REPLACEABILITY_CHECKED = "onReplaceabilityChecked";
		public static final String ATTR_NAME_IMITATION_NODE_ID = "imitationNodeID";
		public static final String ATTR_NAME_CAN_CREATE_IMIT_MANUALLY = "canCreateImitManually";
		public static final String ATTR_NAME_NODE_INPUT_CONTROL = "nodeInputControl";
		public static final String ATTR_NAME_INIT_STRING = "initString";
		public static final String ATTR_NAME_IMITATION_ID = "imitationID";
		public static final String ARRT_NAME_IMIT_CNCT_POS ="imitCnctPos";
		public static final String ATTR_NAME_IMIT_SCOPE_NAME = "imitScopeName";
		public static final String ATTR_NAME_VALUE = "value";
		public static final String ATTR_VALUE_CONNECTIVE = "connective";
		public static final String ATTR_NAME_TEXT_FIELD = "textField";
		public static final String ATTR_NAME_COMBO_BOX = "comboBox";
		public static final String ATTR_NAME_LABEL = "label";
		public static final String ATTR_NAME_NO_VIEW = "noView";
		public static final String ATTR_VALUE_VOID = "void";
		public static final String ATTR_VALUE_TRUE = "true";
		public static final String ATTR_VALUE_FALSE = "false";
		public static final String ATTR_VALUE_DEFAULT_NODE_STYLE_ID = "";
		public static final String ATTR_VALUE_IMIT_ID_MANUAL = "imitIdManual";
		public static final String ATTR_VALUE_TAG_REFER_TO_PARENT = "tagReferToParent";
		public static final String ATTR_VALUE_INITIAL_BHNODE_ID = "initialBhNodeID";
	}

	/**
	 * javascript コードに内部でも使うキーワード
	 * */
	public static class JsKeyword {
		public static final String KEY_BH_THIS = "bhThis";	//!< スクリプトの呼び出し元オブジェクト
		public static final String KEY_BH_NODE_HANDLER = "bhNodeHandler";
		public static final String KEY_BH_NODE_VIEW = "bhNodeView";
		public static final String KEY_BH_MSG_TRANSPORTER = "bhMsgTransporter";
		public static final String KEY_BH_TEXT = "bhText";	//!< TextNode のString型フィールドアクセス用キーワード
		public static final String KEY_BH_OLD_PARENT = "bhOldParent";
		public static final String KEY_BH_OLD_ROOT = "bhOldRoot";
		public static final String KEY_BH_OLD_NODE_ID = "bhOldNodeID";
		public static final String KEY_BH_NEW_NODE_ID = "bhNewNodeID";
		public static final String KEY_BH_REPLACED_NEW_NODE = "bhReplacedNewNode";
		public static final String KEY_BH_REPLACED_OLD_NODE = "bhReplacedOldNode";
		public static final String KEY_BH_MANUALLY_REPLACED = "bhManuallyReplaced";	//!< 手動で子ノードからワークスペースに移動したかどうかのフラグ名
		public static final String KEY_BH_NEXT_SYMBOL_NAME = "bhNextSymbolName";
		public static final String KEY_BH_USER_OPE_CMD = "bhUserOpeCmd";
		public static final String KEY_BH_COMMON = "bhCommon";
		public static final String KEY_BH_NODE_TEMPLATES = "bhNodeTemplates";
		public static final String KEY_IP_ADDR = "ipAddr";
		public static final String KEY_UNAME = "uname";
		public static final String KEY_PASSWORD = "password";
		public static final String KEY_BH_PROGRAM_FILE_PATH = "bhProgramFilePath";
	}

	/**
	 * ノードのスタイル定義のパラメータ
	 */
	public static class NodeStyleDef {
		public static final String KEY_NODE_STYLE_ID = "nodeStyleID";
		public static final String KEY_TOP_MARGIN = "topMargin";
		public static final String KEY_BOTTOM_MATGIN = "bottomMargin";
		public static final String KEY_LEFT_MARGIN = "leftMargin";
		public static final String KEY_RIGHT_MARGIN = "rightMargin";
		public static final String KEY_WIDTH = "width";
		public static final String KEY_HEIGHT = "height";
		public static final String KEY_CONNECTOR_WIDTH = "connectorWidth";
		public static final String KEY_CONNECTOR_HEIGHT = "connectorHeight";
		public static final String KEY_CONNECTOR_SHAPE = "connectorShape";
		public static final String KEY_CONNECTOR_BOUNDS_RATE = "connectorBoundsRate";
		public static final String KEY_DRAW_BODY = "drawBody";
		public static final String KEY_CSS_CLASS = "cssClass";
		public static final String KEY_CONNECTIVE = "connective";
		public static final String KEY_IMITATION = "imitation";
		public static final String KEY_BUTTON_POS_X = "buttonPosX";
		public static final String KEY_BUTTON_POS_Y ="buttonPosY";
		public static final String KEY_TEXT_FIELD = "textField";
		public static final String KEY_LABEL = "label";
		public static final String KEY_COMBO_BOX = "comboBox";
		public static final String KEY_WHITE_SPACE_MATGIN = "whiteSpaceMargin";
		public static final String KEY_MIN_WHITE_SPACE = "minWhiteSpace";
		public static final String KEY_BACK_GROUND_COLOR = "backGroundColor";
		public static final String KEY_FONT_SIZE = "fontSize";
		public static final String KEY_FONT_FAMILY = "fontFamily";
		public static final String KEY_CONNECTOR_POS = "connectorPos";
		public static final String KEY_CONNECTOR_SHIFT = "connectorShift";
		public static final String KEY_INNER = "inner";
		public static final String KEY_OUTER = "outer";
		public static final String KEY_ARRANGEMENR = "arrangement";
		public static final String KEY_INTERVAL = "interval";
		public static final String KEY_CONNECTOR_LIST = "connectorList";
		public static final String KEY_SUB_GROUP = "subGroup";
		public static final String VAL_ARROW = "ARROW";
		public static final String VAL_CHAR_T = "CHAR_T";
		public static final String VAL_CHAR_U = "CHAR_U";
		public static final String VAL_CHAR_V = "CHAR_V";
		public static final String VAL_CROSS = "CROSS";
		public static final String VAL_DIAMOND = "DIAMOND";
		public static final String VAL_HEXAGON = "HEXAGON";
		public static final String VAL_INV_PENTAGON = "INV_PENTAGON";
		public static final String VAL_INV_TRAPEZOID = "INV_TRAPEZOID";
		public static final String VAL_INV_TRIANGLE = "INV_TRIANGLE";
		public static final String VAL_NONE = "NONE";
		public static final String VAL_OCTAGON = "OCTAGON";
		public static final String VAL_PENTAGON = "PENTAGON";
		public static final String VAL_SQARE = "SQUARE";
		public static final String VAL_STAR = "STAR";
		public static final String VAL_TRAPEZOID = "TRAPEZOID";
		public static final String VAL_TRIANGLE = "TRIANGLE";
		public static final String VAL_ROW = "Row";
		public static final String VAL_COLUMN = "Column";
		public static final String VAL_LEFT = "Left";
		public static final String VAL_TOP = "Top";
	}

	public static class NodeTemplateList {
		public static final String KEY_CSS_CLASS = "cssClass";
		public static final String KEY_CONTENTS = "contents";
	}

	/**
	 * FXMLファイル内のキーワード
	 * */
	public static class Fxml {
		public static final String ID_WS_PANE = "wsPane";
	}

	public static class CSS {
		public static final String PSEUDO_SELECTED = "selected";
		public static final String PSEUDO_HIGHLIGHT_IMIT = "highlightImit";
		public static final String PSEUDO_OVERLAPPED = "overlapped";
		public static final String PSEUDO_EMPTY = "empty";
		public static final String PSEUDO_BHNODE = "error";
		public static final String CLASS_BHNODE = "BhNode";
		public static final String CLASS_VOID_NODE = "voidNode";
		public static final String CALSS_COMBO_BOX_NODE = "comboBoxNode";
		public static final String CLASS_TEXT_FIELD_NODE = "textFieldNode";
		public static final String CLASS_LABEL_NODE = "labelNode";
		public static final String CLASS_CONNECTIVE_NODE = "ConnectiveNode";
		public static final String CLASS_SUFFIX_PANE = "-Pane";
	}

	/**
	 * BunnyHopと連携する外部プログラム関連のパラメータ
	 */
	public static class ExternalApplication {

		public static final String BH_PROGRAM_EXEC_ENV_JAR = "BhProgramExecEnvironment.jar";
		public static final int PROGRAM_EXEC_ENV_TERMINATION_TIMEOUT = 10;	//!< BhProgramExecEnvironment終了待ちのタイムアウト時間 (sec)
		public static final int FILE_COPY_TERMINATION_TIMEOUT = 10;	//!< ファイルコピープロセス終了待ちのタイムアウト (sec)
		public static final int POP_RECV_DATA_TIMEOUT = 3;	//!< BhProgram実行環境からの受信データ待ちタイムアウト (sec)
		public static final int POP_SEND_DATA_TIMEOUT = 3;	//!< BhProgram実行環境への送信データ待ちタイムアウト (sec)
		public static final int TCP_PORT_READ_TIMEOUT = 15;	//!< TCPポート読み取りのタイムアウト (sec)
		public static final int MAX_REMOTE_CMD_QUEUE_SIZE = 2048;
		public static final String RMI_TCP_PORT_SUFFIX = "@RmiTcpPort";	//BhProgram実行環境との通信に使うRMIオブジェクトを探す際のTCPポート
		public static final String LOLCAL_HOST = "localhost";
	}
}






