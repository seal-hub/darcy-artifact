(function() {
	bhCommon.appendRemovedNode(bhReplacedNewNode, bhThis, bhManuallyReplaced, bhNodeHandler, bhUserOpeCmd);
	
	let loopCtrlStatList = [];
	bhThis.findSymbolInDescendants(1, true, loopCtrlStatList, 'BreakStat', 'ContinueStat');
	for (let i = 0; i < loopCtrlStatList.length; ++i) {
		const loopStat = loopCtrlStatList[i].findSymbolInAncestors('LoopStat', 1, true);
		if (loopStat === null)
			bhNodeHandler.deleteNode(loopCtrlStatList[i], bhUserOpeCmd);
	}
	
})();
