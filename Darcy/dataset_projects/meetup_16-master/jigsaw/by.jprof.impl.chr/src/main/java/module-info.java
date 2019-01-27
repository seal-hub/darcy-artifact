module by.jprof.impl.chr {
	requires by.jprof.api;
	exports by.jprof.impl.chr;
	provides by.jprof.api.GreetingsService with by.jprof.impl.chr.GreetingsServiceChr;
}
