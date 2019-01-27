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


import static java.nio.file.FileVisitOption.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.common.tools.Util;
import net.seapanda.bunnyhop.configfilereader.BhScriptManager;
import net.seapanda.bunnyhop.model.BhNodeID;
import net.seapanda.bunnyhop.view.connectorshape.ConnectorShape;
import net.seapanda.bunnyhop.view.connectorshape.ConnectorShape.CNCTR_SHAPE;

/**
 * 描画時の見た目(大きさ, 色など)の情報を持つクラス
 * @author K.Koike
 * */
public class BhNodeViewStyle {

	public String nodeStyleID;							//!< ノードスタイルに付けられたID
	public double topMargin = 2.5 * BhParams.NODE_SCALE;			//!< ノード上部の余白
	public double bottomMargin = 2.5 * BhParams.NODE_SCALE;		//!< ノード下部の余白
	public double leftMargin = 2.5 * BhParams.NODE_SCALE;			//!< ノード左部の余白
	public double rightMargin = 2.5 * BhParams.NODE_SCALE;			//!< ノード右部の余白
	public double width = 0.0;				//!< ノードの余白とコネクタを除いた部分の幅
	public double height = 0.0;				//!< ノードの余白とコネクタを除いた部分の高さ
	public CNCTR_POS connectorPos = CNCTR_POS.TOP;		//!< コネクタの位置
	public double connectorShift = 0.5 * BhParams.NODE_SCALE;		//!< ノードの左上からのコネクタの位置
	public double connectorWidth = 1.5 * BhParams.NODE_SCALE;		//!< コネクタ部分の幅
	public double connectorHeight = 1.5 * BhParams.NODE_SCALE;		//!< コネクタ部分の高さ
	public ConnectorShape.CNCTR_SHAPE connectorShape = ConnectorShape.CNCTR_SHAPE.CNCTR_SHAPE_ARROW;	//!< コネクタの形
	public double connectorBoundsRate = 2.0;			//!< ドラッグ&ドロップ時などに適用されるコネクタの範囲
	public boolean drawBody = true;
	public String cssClass = "defaultNode";

	Connective connective = new Connective();
	public static class Connective {
		public double outerWidth = 0.0;						//!< 外部描画される部分の幅
		public double outerHeight = 0.0;					//!< 外部描画される部分の高さ
		public Arrangement inner = new Arrangement();
		public Arrangement outer = new Arrangement();
	}

	public static class Arrangement {
		public double interval = 2.5 * BhParams.NODE_SCALE;		//!< ノード内部に描画するノード同士の間隔
		public CHILD_ARRANGEMENT arrangement = CHILD_ARRANGEMENT.COLUMN;	//!< 子要素のノードとサブグループが並ぶ方向
		public List<String> cnctrNameList = new ArrayList<>();
		public List<Arrangement> subGroup = new ArrayList<>();

		void copy(Arrangement org) {
			interval = org.interval;
			arrangement = org.arrangement;
			cnctrNameList.addAll(org.cnctrNameList);
			org.subGroup.forEach(orgSubGroup -> {
				Arrangement newSubGroup =  new Arrangement();
				newSubGroup.copy(orgSubGroup);
				subGroup.add(newSubGroup);
			});

		}
	}

	TextField textField = new TextField();
	public static class TextField {
		public double whiteSpaceMargin = 0;	//!< 入力されたテキストの後に付くwhite space の数
		public double minWhiteSpace =  5;	//!< テキストーフィールドの最小幅 (white space 換算)
		public String cssClass = "defaultTextField";
	}

	Label label = new Label();
	public static class Label {
		public String cssClass = "defaultLabel";
	}

	ComboBox comboBox = new ComboBox();
	public static class ComboBox {
		public String cssClass = "defaultComboBox";
	}

	Imitation imitation = new Imitation();
	public static class Imitation {
		public double buttonPosX = 0.5 * BhParams.NODE_SCALE;
		public double buttonPosY = 0.5 * BhParams.NODE_SCALE;
		public String cssClass = "defaultImitButton";
	}

	private static final HashMap<String, BhNodeViewStyle> nodeStyleID_nodeStyleTemplate = new HashMap<>(); //!< ノードスタイルのテンプレートを格納するハッシュ. JSON ファイルの nodeStyleID がキー
	private static final HashMap<BhNodeID, String> nodeID_nodeStyleID = new HashMap<>(); //!< ノードIDとノードスタイルのペアを格納するハッシュ
	public static final HashMap<BhNodeID, String> nodeID_inputControlFileName = new HashMap<>();	//!< ノードIDとBhNodeの入力GUI部品のfxmlファイル名のペアを格納するハッシュ

	public enum CNCTR_POS {
		LEFT,
		TOP
	}

	public enum CHILD_ARRANGEMENT {
		ROW,
		COLUMN
	}

	/**
	 * コンストラクタ
	 * */
	public BhNodeViewStyle(){}

	/**
	 * コピーコンストラクタ
	 * @param org コピー元
	 * */
	private BhNodeViewStyle(BhNodeViewStyle org){

		this.nodeStyleID = org.nodeStyleID;
		this.topMargin = org.topMargin;
		this.bottomMargin = org.bottomMargin;
		this.leftMargin = org.leftMargin;
		this.rightMargin = org.rightMargin;
		this.width = org.width;
		this.height = org.height;
		this.connectorPos = org.connectorPos;
		this.connectorShift = org.connectorShift;
		this.connectorWidth = org.connectorWidth;
		this.connectorHeight = org.connectorHeight;
		this.connectorShape = org.connectorShape;
		this.connectorBoundsRate = org.connectorBoundsRate;
		this.drawBody = org.drawBody;
		this.connective.outerWidth = org.connective.outerWidth;
		this.connective.outerHeight = org.connective.outerHeight;
		this.connective.inner.copy(org.connective.inner);
		this.connective.outer.copy(org.connective.outer);
		this.cssClass = org.cssClass;
		this.textField.whiteSpaceMargin = org.textField.whiteSpaceMargin;
		this.textField.minWhiteSpace = org.textField.minWhiteSpace;
		this.textField.cssClass = org.textField.cssClass;
		this.label.cssClass = org.label.cssClass;
		this.comboBox.cssClass = org.comboBox.cssClass;
		this.imitation.cssClass = org.imitation.cssClass;
		this.imitation.buttonPosX = org.imitation.buttonPosX;
		this.imitation.buttonPosY = org.imitation.buttonPosY;
	}

	/**
	 * 外部ノードを含まない本体部分のサイズを取得する
	 * @param includeCnctr コネクタ部分の大きさを含む場合true
	 * @return コネクタ部分や外部ノードを含まない本体部分のサイズ
	 * */
	public Point2D getBodySize(boolean includeCnctr) {

		Point2D cnctrSize = getConnectorSize();
		double bodyWidth = leftMargin + width  + rightMargin;
		if (includeCnctr && (connectorPos == CNCTR_POS.LEFT))
			bodyWidth += cnctrSize.x;

		double bodyHeight = topMargin  + height + bottomMargin;
		if (includeCnctr && (connectorPos == CNCTR_POS.TOP))
			bodyHeight += cnctrSize.y;

		return new Point2D(bodyWidth, bodyHeight);
	}

	/**
	 * 外部ノードを含む本体部分のサイズを取得する
	 * @param includCnctr コネクタ部分の大きさを含む場合true
	 * @return コネクタ部分や外部ノードを含まない本体部分のサイズ
	 * */
	public Point2D getBodyAndOuterSize(boolean includCnctr) {

		Point2D bodySize = getBodySize(includCnctr);
		double totalWidth  = bodySize.x;
		double totalHeight = bodySize.y;
		if (connectorPos == CNCTR_POS.LEFT) {		//外部ノードが右に接続される
			totalWidth += connective.outerWidth;
			totalHeight = Math.max(totalHeight, connective.outerHeight);
		}
		else {												//外部ノードが下に接続される
			totalWidth = Math.max(totalWidth, connective.outerWidth);
			totalHeight += connective.outerHeight;
		}
		return new Point2D(totalWidth, totalHeight);
	}

	/**
	 * コネクタの大きさを取得する
	 * @return コネクタの大きさ
	 */
	public Point2D getConnectorSize() {

		double cnctrWidth = 0.0;
		if (connectorShape != CNCTR_SHAPE.CNCTR_SHAPE_NONE)
			cnctrWidth = connectorWidth;

		double cnctrHeight = 0.0;
		if (connectorShape != CNCTR_SHAPE.CNCTR_SHAPE_NONE)
			cnctrHeight = connectorHeight;

		return new Point2D(cnctrWidth, cnctrHeight);
	}

	public static boolean genViewStyleTemplate() {

		//コネクタファイルパスリスト取得
		Path dirPath = Paths.get(Util.EXEC_PATH, BhParams.Path.VIEW_DIR, BhParams.Path.NODE_STYLE_DEF_DIR);
		Stream<Path> paths = null;	//読み込むファイルパスリスト
		try {
			paths = Files.walk(dirPath, FOLLOW_LINKS).filter(path -> path.getFileName().toString().endsWith(".json"));
		}
		catch (IOException e) {
			MsgPrinter.INSTANCE.errMsgForDebug("style directory not found " + dirPath);
			return false;
		}

		boolean succes = paths.map(filePath -> {
			ScriptObjectMirror jsonObj = BhScriptManager.INSTANCE.parseJsonFile(filePath);
			if (jsonObj == null)
				return false;

			String styleID = filePath.getFileName().toString();
			Optional<BhNodeViewStyle> bhNodeViewStyle = genBhNodeViewStyle((ScriptObjectMirror)jsonObj, filePath.toAbsolutePath().toString(), styleID);
			bhNodeViewStyle.ifPresent(viewStyle -> nodeStyleID_nodeStyleTemplate.put(viewStyle.nodeStyleID, viewStyle));
			return bhNodeViewStyle.isPresent();
		}).allMatch(success -> success);

		nodeStyleID_nodeStyleTemplate.put(BhParams.BhModelDef.ATTR_VALUE_DEFAULT_NODE_STYLE_ID, new BhNodeViewStyle());
		paths.close();
		return succes;
	}

	/**
	 * jsonオブジェクト から BhNodeViewStyle オブジェクトを作成する
	 * @param jsonObj .JSON ファイルを読み込んで作ったトップレベルオブジェクト
	 * @param fileName jsonObj が記述してある .JSON ファイルのファイル名
	 * @return BhNodeViewStyle (オプション)
	 * */
	private static Optional<BhNodeViewStyle> genBhNodeViewStyle(ScriptObjectMirror jsonObj, String fileName, String styleID) {

		BhNodeViewStyle bhNodeViewStyle = new BhNodeViewStyle();

		//styleID
		bhNodeViewStyle.nodeStyleID = styleID;

		//topMargin
		Optional<Object> val = readValue(BhParams.NodeStyleDef.KEY_TOP_MARGIN, Number.class, jsonObj, fileName);
		val.ifPresent(topMargin -> bhNodeViewStyle.topMargin = ((Number)topMargin).doubleValue() * BhParams.NODE_SCALE);

		//bottomMargin
		val = readValue(BhParams.NodeStyleDef.KEY_BOTTOM_MATGIN, Number.class, jsonObj, fileName);
		val.ifPresent(bottomMargin -> bhNodeViewStyle.bottomMargin = ((Number)bottomMargin).doubleValue() * BhParams.NODE_SCALE);

		//leftMargin
		val = readValue(BhParams.NodeStyleDef.KEY_LEFT_MARGIN, Number.class, jsonObj, fileName);
		val.ifPresent(leftMargin -> bhNodeViewStyle.leftMargin = ((Number)leftMargin).doubleValue() * BhParams.NODE_SCALE);

		//rightMargin
		val = readValue(BhParams.NodeStyleDef.KEY_RIGHT_MARGIN, Number.class, jsonObj, fileName);
		val.ifPresent(rightMargin -> bhNodeViewStyle.rightMargin = ((Number)rightMargin).doubleValue() * BhParams.NODE_SCALE);

		//width
		val = readValue(BhParams.NodeStyleDef.KEY_WIDTH, Number.class, jsonObj, fileName);
		val.ifPresent(width -> bhNodeViewStyle.width = ((Number)width).doubleValue() * BhParams.NODE_SCALE);

		//height
		val = readValue(BhParams.NodeStyleDef.KEY_HEIGHT, Number.class, jsonObj, fileName);
		val.ifPresent(height -> bhNodeViewStyle.height = ((Number)height).doubleValue() * BhParams.NODE_SCALE);

		//connectorPos
		val = readValue(BhParams.NodeStyleDef.KEY_CONNECTOR_POS, String.class, jsonObj, fileName);
		val.ifPresent(connectorPos -> {
				String posStr = (String)connectorPos;
				if (posStr.equals(BhParams.NodeStyleDef.VAL_TOP)) {
					bhNodeViewStyle.connectorPos = CNCTR_POS.TOP;
				}
				else if (posStr.equals(BhParams.NodeStyleDef.VAL_LEFT)) {
					bhNodeViewStyle.connectorPos = CNCTR_POS.LEFT;
				}
				else {
					MsgPrinter.INSTANCE.errMsgForDebug("\"" + BhParams.NodeStyleDef.KEY_CONNECTOR_POS + "\"" + " (" + posStr + ") " + "format is invalid.  " + "(" + fileName + ")");
				}
			});

		//connectorShift
		val = readValue(BhParams.NodeStyleDef.KEY_CONNECTOR_SHIFT , Number.class, jsonObj, fileName);
		val.ifPresent(connectorShift -> bhNodeViewStyle.connectorShift = ((Number)connectorShift).doubleValue() * BhParams.NODE_SCALE);

		//connectorWidth
		val = readValue(BhParams.NodeStyleDef.KEY_CONNECTOR_WIDTH, Number.class, jsonObj, fileName);
		val.ifPresent(connectorWidth -> bhNodeViewStyle.connectorWidth = ((Number)connectorWidth).doubleValue() * BhParams.NODE_SCALE);

		//connectorHeight
		val = readValue(BhParams.NodeStyleDef.KEY_CONNECTOR_HEIGHT, Number.class, jsonObj, fileName);
		val.ifPresent(connectorHeight -> bhNodeViewStyle.connectorHeight = ((Number)connectorHeight).doubleValue() * BhParams.NODE_SCALE);

		//connectorShape
		val = readValue(BhParams.NodeStyleDef.KEY_CONNECTOR_SHAPE, String.class, jsonObj, fileName);
		val.ifPresent(
			connectorShape -> {
				String shapeStr = (String)connectorShape;
				CNCTR_SHAPE shapeType = ConnectorShape.stringToCNCTR_SHAPE(shapeStr , fileName);
				bhNodeViewStyle.connectorShape = shapeType;
			});

		//connectorBoundsRate
		val = readValue(BhParams.NodeStyleDef.KEY_CONNECTOR_BOUNDS_RATE, Number.class, jsonObj, fileName);
		val.ifPresent(connectorBoundsRate -> bhNodeViewStyle.connectorBoundsRate = ((Number)connectorBoundsRate).doubleValue());

		//DrawBody
		val = readValue(BhParams.NodeStyleDef.KEY_DRAW_BODY, Boolean.class, jsonObj, fileName);
		val.ifPresent(drawBody -> bhNodeViewStyle.drawBody = ((Boolean)drawBody));

		//bodyCssClass
		val = readValue(BhParams.NodeStyleDef.KEY_CSS_CLASS, String.class, jsonObj, fileName);
		val.ifPresent(bodyCssClass -> bhNodeViewStyle.cssClass = (String)bodyCssClass);

		//connective
		Optional<Object> connectiveOpt = readValue(BhParams.NodeStyleDef.KEY_CONNECTIVE, ScriptObjectMirror.class, jsonObj, fileName);
		connectiveOpt.ifPresent(connective -> fillConnectiveParams(bhNodeViewStyle.connective, (ScriptObjectMirror)connective, fileName));

		//textField
		Optional<Object> textFieldOpt = readValue(BhParams.NodeStyleDef.KEY_TEXT_FIELD, ScriptObjectMirror.class, jsonObj, fileName);
		textFieldOpt.ifPresent(textField -> fillTextFieldParams(bhNodeViewStyle.textField, (ScriptObjectMirror)textField, fileName));

		//label
		Optional<Object> labelOpt = readValue(BhParams.NodeStyleDef.KEY_LABEL, ScriptObjectMirror.class, jsonObj, fileName);
		labelOpt.ifPresent(label -> fillLabelParams(bhNodeViewStyle.label, (ScriptObjectMirror)label, fileName));

		//comboBox
		Optional<Object> comboBoxOpt = readValue(BhParams.NodeStyleDef.KEY_COMBO_BOX, ScriptObjectMirror.class, jsonObj, fileName);
		comboBoxOpt.ifPresent(comboBox -> fillComboBoxParams(bhNodeViewStyle.comboBox, (ScriptObjectMirror)comboBox, fileName));

		//imitation
		Optional<Object> imitationOpt = readValue(BhParams.NodeStyleDef.KEY_IMITATION, ScriptObjectMirror.class, jsonObj, fileName);
		imitationOpt.ifPresent(imitation -> fillImitationParams(bhNodeViewStyle.imitation, (ScriptObjectMirror)imitation, fileName));

		return Optional.of(bhNodeViewStyle);
	}

	/**
	 * BhNodeViewStyle.Connective にスタイル情報を格納する
	 * @param jsonObj key = "connective" の value であるオブジェクト
	 * @param fileName jsonObj が記述してある .JSON ファイルの名前
	 * */
	private static void fillConnectiveParams(BhNodeViewStyle.Connective connectiveStyle, ScriptObjectMirror jsonObj, String fileName) {
		// inner
		Optional<Object> val = readValue(BhParams.NodeStyleDef.KEY_INNER, ScriptObjectMirror.class, jsonObj, fileName);
		val.ifPresent(innerArrange -> fillArrangementParams(connectiveStyle.inner, (ScriptObjectMirror)innerArrange, fileName));

		// outer
		val = readValue(BhParams.NodeStyleDef.KEY_OUTER, ScriptObjectMirror.class, jsonObj, fileName);
		val.ifPresent(outerArrange -> fillArrangementParams(connectiveStyle.outer, (ScriptObjectMirror)outerArrange, fileName));

	}

	/**
	 * BhNodeViewStyle.Arrangement にスタイル情報を格納する
	 * @param arrangement 並べ方に関するパラメータの格納先
	 * @param jsonObj key = "inner" または "outer" の value であるオブジェクト
	 * @param fileName jsonObj が記述してある .JSON ファイルの名前
	 * */
	private static void fillArrangementParams(BhNodeViewStyle.Arrangement arrangement, ScriptObjectMirror jsonObj, String fileName) {

		//interval
		Optional<Object> val = readValue(BhParams.NodeStyleDef.KEY_INTERVAL, Number.class, jsonObj, fileName);
		val.ifPresent(interval -> {arrangement.interval = ((Number)interval).doubleValue() * BhParams.NODE_SCALE;});

		//arrangement
		val = readValue(BhParams.NodeStyleDef.KEY_ARRANGEMENR, String.class, jsonObj, fileName);
		val.ifPresent(childArrange -> {
				String arrangeStr = (String)childArrange;
				if (arrangeStr.equals(BhParams.NodeStyleDef.VAL_ROW)) {
					arrangement.arrangement = CHILD_ARRANGEMENT.ROW;
				}
				else if (arrangeStr.equals(BhParams.NodeStyleDef.VAL_COLUMN)) {
					arrangement.arrangement = CHILD_ARRANGEMENT.COLUMN;
				}
				else {
					MsgPrinter.INSTANCE.errMsgForDebug("\"" + BhParams.NodeStyleDef.KEY_ARRANGEMENR + "\"" + " (" + arrangeStr + ") " + "format is invalid.  " + "(" + fileName + ")");
				}
			});

		//cnctrNameList
		val = readValue(BhParams.NodeStyleDef.KEY_CONNECTOR_LIST, ScriptObjectMirror.class, jsonObj, fileName);
		val.ifPresent(cnctrs -> {
			ScriptObjectMirror cnctrList = (ScriptObjectMirror)cnctrs;
			if (cnctrList.isArray()) {
				cnctrList.values().forEach(cnctrName -> arrangement.cnctrNameList.add(cnctrName.toString()));
			}
		});

		//subGroup
		int groupID = 0;
		while (true) {
			String subGroupKeyName = BhParams.NodeStyleDef.KEY_SUB_GROUP + groupID;
			val = readValue(subGroupKeyName, ScriptObjectMirror.class, jsonObj, fileName);
			if (val.isPresent()) {
				Arrangement subGroup = new Arrangement();
				fillArrangementParams(subGroup, (ScriptObjectMirror)val.get(), fileName);
				arrangement.subGroup.add(subGroup);
			}
			else {
				break;
			}
			++groupID;
		}
	}


	/**
	 * BhNodeViewStyle.Imitaion を埋める
	 * @param imitation jsonObj の情報を格納するオブジェクト
	 * @param jsonObj key = "imitation" の value であるオブジェクト
	 * @param fileName jsonObj が記述してある .json ファイルの名前
	 * */
	private static void fillImitationParams(BhNodeViewStyle.Imitation imitation, ScriptObjectMirror jsonObj, String fileName) {

		//buttonPosX
		Optional<Object> val = readValue(BhParams.NodeStyleDef.KEY_BUTTON_POS_X, Number.class, jsonObj, fileName);
		val.ifPresent(btnPosX -> {imitation.buttonPosX = ((Number)btnPosX).doubleValue() * BhParams.NODE_SCALE;});

		//buttonPosY
		val = readValue(BhParams.NodeStyleDef.KEY_BUTTON_POS_Y, Number.class, jsonObj, fileName);
		val.ifPresent(btnPosY -> {imitation.buttonPosY = ((Number)btnPosY).doubleValue() * BhParams.NODE_SCALE;});

		//buttonCssClass
		val = readValue(BhParams.NodeStyleDef.KEY_CSS_CLASS, String.class, jsonObj, fileName);
		val.ifPresent(buttonCssClass -> imitation.cssClass = (String)buttonCssClass);
	}

	/**
	 * BhNodeViewStyle.TextField を埋める
	 * @param textField jsonオブジェクトから読み取った内容を格納するオブジェクト
	 * @param jsonObj key = "textField" の value であるオブジェクト
	 * @param fileName jsonObj が記述してある .json ファイルの名前
	 * */
	private static void fillTextFieldParams(BhNodeViewStyle.TextField textField, ScriptObjectMirror jsonObj, String fileName) {

		//whiteSpaceMargin
		Optional<Object> val = readValue(BhParams.NodeStyleDef.KEY_WHITE_SPACE_MATGIN, Number.class, jsonObj, fileName);
		val.ifPresent(wsMargine -> textField.whiteSpaceMargin = ((Number)wsMargine).doubleValue());

		//minWhiteSpace
		val = readValue(BhParams.NodeStyleDef.KEY_MIN_WHITE_SPACE, Number.class, jsonObj, fileName);
		val.ifPresent(minWS -> textField.minWhiteSpace = ((Number)minWS).doubleValue());

		//cssClass
		val = readValue(BhParams.NodeStyleDef.KEY_CSS_CLASS, String.class, jsonObj, fileName);
		val.ifPresent(textCssClass -> textField.cssClass = (String)textCssClass);
	}

	/**
	 * BhNodeViewStyle.Label を埋める
	 * @param label jsonオブジェクトから読み取った内容を格納するオブジェクト
	 * @param jsonObj key = "label" の value であるオブジェクト
	 * @param fileName jsonObj が記述してある .json ファイルの名前
	 * */
	private static void fillLabelParams(BhNodeViewStyle.Label textField, ScriptObjectMirror jsonObj, String fileName) {
		//cssClass
		Optional<Object> val = readValue(BhParams.NodeStyleDef.KEY_CSS_CLASS, String.class, jsonObj, fileName);
		val.ifPresent(textCssClass -> textField.cssClass = (String)textCssClass);
	}

	/**
	 * BhNodeViewStyle.ComboBox を埋める
	 * @param comboBox jsonオブジェクトから読み取った内容を格納するオブジェクト
	 * @param jsonObj key = "comboBox" の value であるオブジェクト
	 * @param fileName jsonObj が記述してある .json ファイルの名前
	 * */
	private static void fillComboBoxParams(BhNodeViewStyle.ComboBox comboBox, ScriptObjectMirror jsonObj, String fileName) {
		//cssClass
		Optional<Object> val = readValue(BhParams.NodeStyleDef.KEY_CSS_CLASS, String.class, jsonObj, fileName);
		val.ifPresent(comboBoxCssClass -> comboBox.cssClass = (String)comboBoxCssClass);
	}

	/**
	 * Json オブジェクトからエラーチェック付きで Value を読む
	 * @param keyName keyの名前
	 * @param valueType 想定される value の型 (String, Number, Boolean, ScriptObjectMirror, ...)
	 * @param jsonObj key と value が格納されているJson オブジェクト
	 * @param fileName jsonObj を読み取ったファイルの名前
	 * @return JsonValue オブジェクト (オプション)
	 * */
	private static Optional<Object> readValue(String keyName, Class<?> valueType, ScriptObjectMirror jsonObj, String fileName) {

		Object val = jsonObj.get(keyName);
		if (val == null)
			return Optional.empty();

		if (!valueType.isAssignableFrom(val.getClass())) {
			MsgPrinter.INSTANCE.errMsgForDebug("The type of " + keyName + " must be " + valueType.getSimpleName() + ".  \n"
										+"The actual type is " + val.getClass().getSimpleName() + ". " + "(" + fileName + ")");
			return Optional.empty();
		}
		return Optional.of(val);
	}

	/**
	 * ノードIDとノードスタイルID のペアを登録する
	 * @param nodeID ノードID (bhNodeID属性)
	 * @param nodeStyleID ノードスタイルID (nodeStyleID属性)
	 * */
	public static void putNodeID_NodeStyleID(BhNodeID nodeID, String nodeStyleID) {
		nodeID_nodeStyleID.put(nodeID, nodeStyleID);
	}

	/**
	 * ノードID から ノードスタイルオブジェクトを取得する
	 * @param nodeID ノードID (bhNodeID属性)
	 * @return ノードスタイルオブジェクト
	 * */
	public static BhNodeViewStyle getNodeViewStyleFromNodeID(BhNodeID nodeID) {

		String nodeStyleID = nodeID_nodeStyleID.get(nodeID);
		BhNodeViewStyle nodeStyle = nodeStyleID_nodeStyleTemplate.get(nodeStyleID);
		return new BhNodeViewStyle(nodeStyle);
	}

	/**
	 * 登録された全てのBhNodeIDに対してノードスタイルのテンプレートが存在するかどうかチェック
	 * @return 登録された全てのBhNodeIDに対してノードスタイルのテンプレートが存在する場合 true
	 * */
	public static boolean checkNodeIdAndNodeTemplate() {

		return nodeID_nodeStyleID.values().stream().allMatch(nodeStyleID -> {
				if (!nodeStyleID_nodeStyleTemplate.containsKey(nodeStyleID)) {
					MsgPrinter.INSTANCE.errMsgForDebug("A node style file " + "(" + nodeStyleID +")" + " is not found among *.json files");
					return false;
				}
				else {
					return true;
				}
			});
	}
}
