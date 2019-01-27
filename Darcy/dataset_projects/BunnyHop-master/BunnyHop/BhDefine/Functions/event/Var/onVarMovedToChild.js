(function() {

	/**
	 * any-type Node を対応する static type Node に入れ替える.
	 * bhThis を static type Node の下につなぎ直す.
	 * @param anyTypeNode static type Node と入れ替えるノード
	 * @param path bhThis の any-type 内の新しい接続先.
	 * @param isStat any-type Nodeがステートの場合true
	 */
	function replaceAnyTypeNodeWithNewStaticTypeNode(anyTypeNode, path, isStat) {
	
		let staticTypeNodeID = bhCommon.getStaticTypeNodeID(anyTypeNode.getSymbolName(), bhThis.getSymbolName());
		if (staticTypeNodeID !== null) {
			let posOnWS = bhCommon.Util.getPosOnWS(anyTypeNode);
			let staticTypeNode = bhCommon.addNewNodeToWS(
				staticTypeNodeID,
				bhThis.getWorkspace(),
				posOnWS,
				bhNodeHandler,
				bhNodeTemplates,
				bhUserOpeCmd);

			//変数ノードの繋ぎ換え
			bhCommon.replaceDescendant(staticTypeNode, path, bhThis, bhNodeHandler, bhUserOpeCmd);

			//ステートノードの入れ替え
			if (isStat)
				bhCommon.replaceStatWithNewStat(anyTypeNode, staticTypeNode, bhNodeHandler, bhUserOpeCmd);
			else
				bhNodeHandler.exchangeNodes(anyTypeNode, staticTypeNode, bhUserOpeCmd);
				
			bhNodeHandler.deleteNode(anyTypeNode, bhUserOpeCmd);
		}
	}
	
	let anyTypeNode = bhThis.findSymbolInAncestors('*', 3, false);
	switch (anyTypeNode.getSymbolName()) {
		case 'AnyAssignStat':
			replaceAnyTypeNodeWithNewStaticTypeNode(anyTypeNode, ['*', 'LeftVar', '*'], true);
			break;
			
		case 'AnyArrayAppendStat':
		case 'AnyArrayClearStat':
		case 'AnyArrayInsertStat':
		case 'AnyArrayPopStat':
		case 'AnyArrayPushStat':
		case 'AnyArrayRemoveStat':
		case 'AnyArraySetStat':
			replaceAnyTypeNodeWithNewStaticTypeNode(anyTypeNode, ['*', 'Arg0', '*'], true);
			break;
		
		case 'AnyArrayGetExp':
		case 'AnyArrayGetLastExp':
			replaceAnyTypeNodeWithNewStaticTypeNode(anyTypeNode, ['*', 'Array', '*'], false);
			break;
		
		default:
	}

})();










