
	const _MAX_SPEED = 10;
	const _MIN_SPEED = 1;

	function _moveForward(speed, time) {
		speed = Math.min(Math.max(_MIN_SPEED, speed), _MAX_SPEED);
		_println("速度 " + speed + " で " + time + " 秒間「前進」した")
	}

	function _moveBackward(speed, time) {
		speed = Math.min(Math.max(_MIN_SPEED, speed), _MAX_SPEED);
		_println("速度 " + speed + " で " + time + " 秒間「後退」した")
	}

	function _turnRight(speed, time) {
		speed = Math.min(Math.max(_MIN_SPEED, speed), _MAX_SPEED);
		_println("速度 " + speed + " で " + time + " 秒間「右旋回」した")
	}

	function _turnLeft(speed, time) {
		speed = Math.min(Math.max(_MIN_SPEED, speed), _MAX_SPEED);
		_println("速度 " + speed + " で " + time + " 秒間「左旋回」した")
	}
	
	function _measureDistance() {
	
		let dist = _scan('距離を入力してください (半角)');
		dist = Number(dist);
		if (!isFinite(dist))
			dist = 0;
		
		_println("距離 = " + dist);
		return dist;
	}
	
