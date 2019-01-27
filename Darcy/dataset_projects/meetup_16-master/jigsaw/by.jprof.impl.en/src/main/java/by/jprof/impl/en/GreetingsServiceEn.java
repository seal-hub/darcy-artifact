package by.jprof.impl.en;

import by.jprof.api.GreetingsService;

public class GreetingsServiceEn implements GreetingsService {
	@Override
	public String greet(String who) {
		return "Hello, " + who + "!";
	}
}
