(function() {

	const newNodeName = bhReplacedNewNode.getSymbolName();
	const section = bhReplacedNewNode.findSymbolInDescendants('*');
	let sectionName = null;
	if (section !== null) {
		sectionName = section.getSymbolName();
	}
    
	return newNodeName === 'LineFeed' ||
		   newNodeName === 'StrLiteral' ||
		   sectionName === 'StringExpSctn' ||
		   sectionName === 'AnyExpSctn';
})();