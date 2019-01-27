package ch.qos.modRules.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String[] args) {
		System.out.println("This is " + Main.class);

		if (args.length == 0) {
			System.out.println("USage: ch.qos.modRules.app.Main [1|2|3|4|5]");
			return;
		}

		String argStr = args[0];
		int command = Integer.valueOf(argStr);
		switch (command) {
		case 1:
			new ch.qos.modRules.named.Boo();
			break;
		case 2:
			new ch.qos.modRules.auto.Boo();
			break;
		case 3:
			invokeConstructor("ch.qos.modRules.unnamed.Boo");
			break;
		}
	}

	private static void invokeConstructor(String className) {
		try {
			Class<?> aClass = Class.forName(className);
			Constructor<?> cons = aClass.getConstructor();
			cons.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Failed to invoke constructor of "+className);
			e.printStackTrace();
		}

	}
}
