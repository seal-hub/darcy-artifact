(function() {

	try {
		java.lang.Double.parseDouble(bhText);
		return true;
	}
	catch(e){}

	if (bhText.length >= 2) {
		if (bhText.substr(0,2) === "0x") {
			try {
				java.lang.Integer.parseInt(bhText.substr(2, bhText.length), 16);
				return true;
			}
			catch(e){}
		}
	}
	
	return false;
})();


