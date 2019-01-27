
(function() {
	return ['plink', '-ssh', ipAddr, '-l', uname, '-pw', password,
			'bash', ('~/BunnyHop/StartBhProgramExecEnv.sh'), ipAddr];
})();