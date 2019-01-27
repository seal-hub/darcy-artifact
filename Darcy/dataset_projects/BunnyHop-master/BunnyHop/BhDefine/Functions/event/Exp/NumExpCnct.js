(function() {

	const newNodeName = bhReplacedNewNode.getSymbolName();
	const section = bhReplacedNewNode.findSymbolInDescendants('*');
	let sectionName = null;
	if (section !== null) {
		sectionName = section.getSymbolName();
	}

	return newNodeName === 'NumLiteral' ||
		   sectionName === 'NumberExpSctn' ||
		   sectionName === 'AnyExpSctn';
})();
