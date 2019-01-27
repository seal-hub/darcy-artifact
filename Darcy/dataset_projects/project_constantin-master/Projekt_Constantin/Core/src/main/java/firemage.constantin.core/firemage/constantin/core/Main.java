package firemage.constantin.core;

import java.io.IOException;

import firemage.constantin.core.properties.CoreProperties;
import firemage.constantin.core.properties.CorePropertyName;

public class Main {

	public static void main(final String[] args) {
		try {
			System.out.println(CoreProperties.getInstance().getProperty(CorePropertyName.MAX_TIME_IN_MIND));
		} catch (IOException e) {
			System.err.println("Cann not find file \"" + CoreProperties.getPropertyFilePath() + "\"");
		}
	}

}
