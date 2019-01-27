(function() {

	if (bhText.match(/[\/\*]/) != null) {
		return false;
	}
	if (bhText.length > 64) {
		return false;
	}
	
	return bhText !== "";
})();
