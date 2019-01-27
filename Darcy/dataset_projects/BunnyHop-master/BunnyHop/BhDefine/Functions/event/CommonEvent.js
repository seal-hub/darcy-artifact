(function() {

	let ControllerType = Java.type('net.seapanda.bunnyhop.modelprocessor.NodeMVCBuilder.ControllerType');
	let NodeMVCBuilder = Java.type('net.seapanda.bunnyhop.modelprocessor.NodeMVCBuilder');
	let BhNodeID = Java.type("net.seapanda.bunnyhop.model.BhNodeID");
	let BhNodeState = Java.type('net.seapanda.bunnyhop.model.BhNode.State');
	let Util =  Java.type("net.seapanda.bunnyhop.common.tools.Util");

	let bhCommon = {};

	// 入れ替わってWSに移ったノードを末尾に再接続する
	function appendRemovedNode(newNode, oldNode, manuallyReplaced, bhNodeHandler, bhUserOpeCmd) {

		let outerEnd = newNode.findOuterEndNode();
		if ((outerEnd.type === "void") && outerEnd.canBeReplacedWith(oldNode) && !manuallyReplaced) {
			bhNodeHandler.replaceChild(outerEnd, oldNode, bhUserOpeCmd);
			bhNodeHandler.deleteNode(outerEnd, bhUserOpeCmd);
	    }
	}

	/**
	 * 引数で指定したノードを作成し, ワークスペースに追加する
	 * @param bhNodeID 作成するノードのID
	 * @param pos 追加時のワークスペース上の位置
	 * @param bhNodeHandler ノード操作用オブジェクト
	 * @param bhNodeTemplates ノードテンプレート管理オブジェクト
	 * @param bhUserOpeCmd undo/redo用コマンドオブジェクト
	 * @return 新規作成したノード
	 * */
	function addNewNodeToWS(bhNodeID, workspace, pos, bhNodeHandler, bhNodeTemplates, bhUserOpeCmd) {

		let nodeMVCBuilder = new NodeMVCBuilder(ControllerType.Default);
		let newNode = bhNodeTemplates.genBhNode(BhNodeID.createBhNodeID(bhNodeID), bhUserOpeCmd);
		newNode.accept(nodeMVCBuilder);
		bhNodeHandler.addRootNode(workspace, newNode, pos.x, pos.y, bhUserOpeCmd);
		return newNode;
	}

	/**
	 * 子孫ノードを入れ替える. 古いノードは削除される.
	 * @param rootNode このノードの子孫ノードを入れ替える
	 * @param descendantPath 入れ替えられる子孫ノードの rootNode からのパス
	 * @param newNode 入れ替える新しいノード
	 * @param bhNodeHandler ノード操作用オブジェクト
	 * @param bhUserOpeCmd undo/redo用コマンドオブジェクト
	 * */
	function replaceDescendant(rootNode, descendantPath, newNode, bhNodeHandler, bhUserOpeCmd) {

		//コピーし直さないと, findSymbolInDescendants(descendantPath) => findSymbolInDescendants('a,b,c') と解釈されてしまう.
		let path = [];
		for (let i = 0; i < descendantPath.length; ++i)
			path[i] = descendantPath[i];
		let oldNode = rootNode.findSymbolInDescendants(path);
		bhNodeHandler.replaceChild(oldNode, newNode, bhUserOpeCmd);
		bhNodeHandler.deleteNode(oldNode, bhUserOpeCmd);
	}

	/**
	 * ステートノードを入れ替える.
	 * @param oldStat 入れ替えられる古いステートノード
	 * @param newStat 入れ替える新しいステートノード
	 * @param bhNodeHandler ノード操作用オブジェクト
	 * @param userOpeCmd undo/redo用コマンドオブジェクト
	 * */
	function replaceStatWithNewStat(oldStat, newStat, bhNodeHandler, bhUserOpeCmd) {
	
		let nextStatOfOldStat = oldStat.findSymbolInDescendants('*', 'NextStat', '*');
		let nextStatOfNewStat = newStat.findSymbolInDescendants('*', 'NextStat', '*');
		bhNodeHandler.exchangeNodes(nextStatOfOldStat, nextStatOfNewStat, bhUserOpeCmd);
		bhNodeHandler.exchangeNodes(oldStat, newStat, bhUserOpeCmd);
	}

	//any-Type Node -> static-type Node
	let anyTypeToStaticTypeNode = {
		'AnyAssignStat' : {
			'NumVar'   : 'idNumAssignStat',
			'StrVar'   : 'idStrAssignStat',
			'BoolVar'  : 'idBoolAssignStat',
			'ColorVar' : 'idColorAssignStat',
			'SoundVar' : 'idSoundAssignStat'
		},

		'AnyArrayAppendStat' : {
			'NumList'   : 'idNumArrayAppendStat',
			'StrList'   : 'idStrArrayAppendStat',
			'BoolList'  : 'idBoolArrayAppendStat',
			'ColorList' : 'idColorArrayAppendStat',
			'SoundList' : 'idSoundArrayAppendStat'
		},

		'AnyArrayClearStat' : {
			'NumList'   : 'idNumArrayClearStat',
			'StrList'   : 'idStrArrayClearStat',
			'BoolList'  : 'idBoolArrayClearStat',
			'ColorList' : 'idColorArrayClearStat',
			'SoundList' : 'idSoundArrayClearStat'
		},

		'AnyArrayGetExp' : {
			'NumList'   : 'idNumArrayGetExp',
			'StrList'   : 'idStrArrayGetExp',
			'BoolList'  : 'idBoolArrayGetExp',
			'ColorList' : 'idColorArrayGetExp',
			'SoundList' : 'idSoundArrayGetExp',
			'idNumExpCnctr'   : 'idNumArrayGetExp',
			'idStrExpCnctr'   : 'idStrArrayGetExp',
			'idBoolExpCnctr'  : 'idBoolArrayGetExp',
			'idColorExpCnctr' : 'idColorArrayGetExp',
			'idSoundExpCnctr' : 'idSoundArrayGetExp'

		},

		'AnyArrayGetLastExp' : {
			'NumList'   : 'idNumArrayGetLastExp',
			'StrList'   : 'idStrArrayGetLastExp',
			'BoolList'  : 'idBoolArrayGetLastExp',
			'ColorList' : 'idColorArrayGetLastExp',
			'SoundList' : 'idSoundArrayGetLastExp',
			'idNumExpCnctr'   : 'idNumArrayGetLastExp',
			'idStrExpCnctr'   : 'idStrArrayGetLastExp',
			'idBoolExpCnctr'  : 'idBoolArrayGetLastExp',
			'idColorExpCnctr' : 'idColorArrayGetLastExp',
			'idSoundExpCnctr' : 'idSoundArrayGetLastExp'

		},

		'AnyArrayInsertStat' : {
			'NumList'   : 'idNumArrayInsertStat',
			'StrList'   : 'idStrArrayInsertStat',
			'BoolList'  : 'idBoolArrayInsertStat',
			'ColorList' : 'idColorArrayInsertStat',
			'SoundList' : 'idSoundArrayInsertStat'
		},

		'AnyArrayPopStat' : {
			'NumList'   : 'idNumArrayPopStat',
			'StrList'   : 'idStrArrayPopStat',
			'BoolList'  : 'idBoolArrayPopStat',
			'ColorList' : 'idColorArrayPopStat',
			'SoundList' : 'idSoundArrayPopStat'
		},

		'AnyArrayPushStat' : {
			'NumList'   : 'idNumArrayPushStat',
			'StrList'   : 'idStrArrayPushStat',
			'BoolList'  : 'idBoolArrayPushStat',
			'ColorList' : 'idColorArrayPushStat',
			'SoundList' : 'idSoundArrayPushStat'
		},

		'AnyArrayRemoveStat' : {
			'NumList'   : 'idNumArrayRemoveStat',
			'StrList'   : 'idStrArrayRemoveStat',
			'BoolList'  : 'idBoolArrayRemoveStat',
			'ColorList' : 'idColorArrayRemoveStat',
			'SoundList' : 'idSoundArrayRemoveStat'
		},

		'AnyArraySetStat' : {
			'NumList'   : 'idNumArraySetStat',
			'StrList'   : 'idStrArraySetStat',
			'BoolList'  : 'idBoolArraySetStat',
			'ColorList' : 'idColorArraySetStat',
			'SoundList' : 'idSoundArraySetStat'
		}
	};

	/**
	 * any-Type Node と static type Node から対応する static-type Node を取得する
	 */
	function getStaticTypeNodeID(anyTypeNode, staticTypeNode) {
		
		let staticTypeNodeToNodeID = anyTypeToStaticTypeNode[anyTypeNode];
		if (!staticTypeNodeToNodeID)
			return null;
			
		let staticTypeNodeID = staticTypeNodeToNodeID[staticTypeNode];
		if (!staticTypeNodeID)
			return null;

		return staticTypeNodeID;
	}

    bhCommon['appendRemovedNode'] = appendRemovedNode;
    bhCommon['getStaticTypeNodeID'] = getStaticTypeNodeID;
    bhCommon['addNewNodeToWS'] = addNewNodeToWS;
    bhCommon['replaceDescendant'] = replaceDescendant;
    bhCommon['replaceStatWithNewStat'] = replaceStatWithNewStat;
    bhCommon['Util'] = Util;
    return bhCommon;
})();
