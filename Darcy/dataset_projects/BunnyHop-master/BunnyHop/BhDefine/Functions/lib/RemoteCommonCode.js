
	const _MOVE_CMD = {
		MOVE_FORWARD : '1',
		MOVE_BACKWARD : '2',
		TURN_RIGHT : '3',
		TURN_LEFT : '4'
	};
	
	function _moveAny(speed, time, cmd) {
	
		const _MAX_SPEED = 10;
		const _MIN_SPEED = 1;
		speed = (speed - _MIN_SPEED) / (_MAX_SPEED - _MIN_SPEED);
		speed = Math.min(Math.max(0.0, speed), 1.0);
		time *= 1000;
		time = Math.floor(time);
		let moveCmd = execPath + '/Actions' + '/bhMove';
		const procBuilder = new java.lang.ProcessBuilder([moveCmd, cmd, String(time), String(speed)]);
		try {
			const process =  procBuilder.start();
			process.waitFor();
			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
		}
		catch (e) {
			_println('ERR: _move ' + cmd + ' ' + e);
		}
	}

	function _moveForward(speed, time) {
		_moveAny(speed, time, _MOVE_CMD.MOVE_FORWARD);
	}

	function _moveBackward(speed, time) {
		_moveAny(speed, time, _MOVE_CMD.MOVE_BACKWARD);
	}

	function _turnRight(speed, time) {
		_moveAny(speed, time, _MOVE_CMD.TURN_RIGHT);
	}

	function _turnLeft(speed, time) {
		_moveAny(speed, time, _MOVE_CMD.TURN_LEFT);
	}

	function readStream(stream) {
		let readByte = [];
		while(true) {
			const rb = stream.read();
			if (rb === -1)
				break;
			readByte.push(rb);
		}
		readByte = Java.to(readByte, "byte[]");
		const string = Java.type("java.lang.String");
		return new string(readByte);
	}

	function _measureDistance() {
	
		let spiCmd = execPath + '/Actions' + '/bhSpiRead';
		const procBuilder = new java.lang.ProcessBuilder([spiCmd, '15', '1']);
		let distance;
		try {
			const process =  procBuilder.start();
			process.waitFor();
			distance = readStream(process.getInputStream());
			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
		}
		catch (e) {
			_println('ERR: _measureDistance ' + e);
		}
		distance = Number(distance);
		if (!isFinite(distance))
			return 0;
		if (distance >= 384)
			distance = 49840 * Math.pow(distance, -1.142);
		else
			distance = 467132 * Math.pow(distance, -1.522);
		return distance;
	}
