
(function() {
	return ['plink', '-ssh', ipAddr, '-l', uname, '-pw', password, 
			'bash', ('~/BunnyHop/StopBhProgramExecEnv.sh')];
})();