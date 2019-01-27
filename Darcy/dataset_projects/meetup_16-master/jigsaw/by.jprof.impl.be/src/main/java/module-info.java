module by.jprof.impl.be {
	requires by.jprof.api;
	exports by.jprof.impl.be;
	provides by.jprof.api.GreetingsService with by.jprof.impl.be.GreetingsServiceBe;
}
