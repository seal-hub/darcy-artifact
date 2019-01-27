module by.jprof.impl.en {
	requires by.jprof.api;
	exports by.jprof.impl.en;
	provides by.jprof.api.GreetingsService with by.jprof.impl.en.GreetingsServiceEn;
}
