package by.jprof.impl.be;

import by.jprof.api.GreetingsService;

import java.text.MessageFormat;
import java.util.PropertyResourceBundle;

public class GreetingsServiceBe implements GreetingsService {
	@Override
	public String greet(String who) {
		return MessageFormat.format(PropertyResourceBundle.getBundle("by.jprof.impl.be.internal.strings").getString("greet"), who);
	}
}
