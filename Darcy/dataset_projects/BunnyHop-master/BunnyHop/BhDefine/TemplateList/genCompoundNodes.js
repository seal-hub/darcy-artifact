(function() {

	let BhNodeID = Java.type("net.seapanda.bunnyhop.model.BhNodeID");

	function registerNodeTemplate(bhNodeID, bhNode) {
		bhNodeTemplates.registerNodeTemplate(BhNodeID.createBhNodeID(bhNodeID), bhNode);
	}
	
	function genBhNode(bhNodeID, bhUserOpeCmd) {
		return bhNodeTemplates.genBhNode(BhNodeID.createBhNodeID(bhNodeID), bhUserOpeCmd);
	}

	function connect(parentNode, childNode, cnctrPath) {
		const cnctr = parentNode.findSymbolInDescendants(cnctrPath);
		cnctr.connectNode(childNode, bhUserOpeCmd);
	}

	//カウンタ付き回数指定ループノード作成
	function addRepeatAndCountNode(nodeID) {
	
		let compoundNode = genBhNode('idCompoundStat', bhUserOpeCmd);
		let countVarNode = genBhNode('idNumVarDecl', bhUserOpeCmd);
		let initAssignStatNode = genBhNode('idNumAssignStat', bhUserOpeCmd);
		let repeatStatNode = genBhNode('idRepeatStat', bhUserOpeCmd);
		let updateAssignStatNode = genBhNode('idNumAddAssignStat', bhUserOpeCmd);
		
		connect(compoundNode, initAssignStatNode, ['*', '*', 'StatList']);
		connect(compoundNode, countVarNode, ['*', '*', 'LocalVarDecl']);
		connect(initAssignStatNode, repeatStatNode, ['*', 'NextStat']);
		connect(repeatStatNode, updateAssignStatNode,  ['*', 'LoopStat']);
		
		//変数名変更
		let countVarName = countVarNode.findSymbolInDescendants(['*', 'VarName', '*']);
		countVarName.setText('カウンター');
		
		//カウンタ更新幅変更
		let updateDiff = updateAssignStatNode.findSymbolInDescendants(['*', 'RightExp', '*']);
		updateDiff.setText('1');
		
		registerNodeTemplate(nodeID, compoundNode);
	}
	
	//移動ノードの初期速度を変更して再登録
	function setInitialMoveSpeed() {
		let newMoveStat = genBhNode('idMoveStat', bhUserOpeCmd);
		let moveSpeed = newMoveStat.findSymbolInDescendants('*', 'Arg0', '*');
		let moveTime = newMoveStat.findSymbolInDescendants('*', 'Arg1', '*');
		moveSpeed.setText('2');
		moveTime.setText('1');
		registerNodeTemplate('idMoveStat', newMoveStat);
	}
	
	//リストと変数名を変更して再登録
	function setVarAndListName() {
		
		//数値変数
		var varDecl = genBhNode('idNumVarDecl', bhUserOpeCmd);
		var varName = varDecl.findSymbolInDescendants('*', 'VarName', '*');
		varName.setText('数値変数');
		registerNodeTemplate('idNumVarDecl', varDecl);

		//文字列変数
		var varDecl = genBhNode('idStrVarDecl', bhUserOpeCmd);
		var varName = varDecl.findSymbolInDescendants('*', 'VarName', '*');
		varName.setText('文字列変数');
		registerNodeTemplate('idStrVarDecl', varDecl);

		//論理変数
		varDecl = genBhNode('idBoolVarDecl', bhUserOpeCmd);
		varName = varDecl.findSymbolInDescendants('*', 'VarName', '*');
		varName.setText('論理変数');
		registerNodeTemplate('idBoolVarDecl', varDecl);
		
		//数値リスト
		varDecl = genBhNode('idNumListDecl', bhUserOpeCmd);
		varName = varDecl.findSymbolInDescendants('*', 'ListName', '*');
		varName.setText('数値リスト');
		registerNodeTemplate('idNumListDecl', varDecl);

		//文字列リスト
		varDecl = genBhNode('idStrListDecl', bhUserOpeCmd);
		varName = varDecl.findSymbolInDescendants('*', 'ListName', '*');
		varName.setText('文字列リスト');
		registerNodeTemplate('idStrListDecl', varDecl);
		
		//論理リスト
		varDecl = genBhNode('idBoolListDecl', bhUserOpeCmd);
		varName = varDecl.findSymbolInDescendants('*', 'ListName', '*');
		varName.setText('論理リスト');
		registerNodeTemplate('idBoolListDecl', varDecl);
	}
	
	addRepeatAndCountNode('idRepeatAndCount');
	setInitialMoveSpeed();
	setVarAndListName();
})();


