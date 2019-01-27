
	const outArgs = [];

	function _copyArgs(dest, args, beginIdx) {
		dest.length = 0;
		for (let i = beginIdx; i < args.length; ++i)
			dest.push(args[i]);
	}

	function _boolToStr(boolVal) {
		return (boolVal === true) ? '真' : '偽';
	}
	
	function _strToNum(strVal) {
		const num = Number(strVal);
		if (isFinite(num))
			return num;
		return 0;
	}

	function _randomInt(min, max) {
		min = Math.ceil(min);
		max = Math.ceil(max);
		return Math.floor(Math.random() * (max - min + 1)) + min;
	}

	function _println(str) {
		inout.println(str);
	}

	function _sleep(sec) {
		try {
			java.lang.Thread.sleep(Math.round(sec * 1000));
		}
		catch (e) {}
	}

	function _scan(str) {
		inout.println(str);
		let input = inout.scan();
		return (input === null) ? "" : input;
	}

	function _aryPush(ary, val) {
		ary.push(val);
	}

	function _aryPop(ary) {
		ary.pop();
	}

	function _aryInsert(ary, idx, val) {
		idx = Math.floor(idx);
		if (0 <= idx && idx <= ary.length)
			ary.splice(idx, 0, val);
	}

	function _aryRemove(ary, idx) {
		idx = Math.floor(idx);
		if (0 <= idx && idx < ary.length)
			ary.splice(idx, 1);
	}

	function _aryClear(ary) {
		ary.length = 0;
	}

	function _aryAddAll(aryA, aryB) {
		Array.prototype.push.apply(aryA, aryB);
	}

	function _aryGet(ary, idx, dflt) {
		idx = Math.floor(idx);
		if (0 <= idx && idx < ary.length)
			return ary[idx];
		return dflt;
	}

	function _arySet(ary, idx, val) {
		idx = Math.floor(idx);
		if (0 <= idx && idx < ary.length)
			ary[idx] = val;
	}

