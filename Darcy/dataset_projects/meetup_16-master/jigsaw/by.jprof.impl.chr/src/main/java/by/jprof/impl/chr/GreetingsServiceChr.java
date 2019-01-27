package by.jprof.impl.chr;

import by.jprof.api.GreetingsService;

import java.text.MessageFormat;
import java.util.PropertyResourceBundle;

public class GreetingsServiceChr implements GreetingsService {
	@Override
	public String greet(String who) {
		return MessageFormat.format(PropertyResourceBundle.getBundle("by.jprof.impl.chr.internal.strings").getString("greet"), who);
	}
}
