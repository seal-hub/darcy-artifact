package fr.mrcraftcod.utils.base;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Log
{
	private static Logger logger;
	private static final ArrayList<LogListener> listeners = new ArrayList<>();

	public interface LogListener
	{
		void onLogMessage(Level level, String message);

		void onLogMessage(Level level, String message, Throwable throwable);
	}

	public static void addListener(LogListener listener)
	{
		listeners.add(listener);
	}

	public static Logger getLogger()
	{
		return logger != null ? logger : setAppName("MCCUtils");
	}

	public static Logger setAppName(String name)
	{
		logger = Logger.getLogger(name);
		return logger;
	}

	public static void warning(String s)
	{
		log(Level.WARNING, s);
	}

	public static void warning(String s, Throwable e)
	{
		log(Level.WARNING, s, e);
	}

	public static void warning(boolean log, String s, Throwable e)
	{
		if(log)
			warning(s, e);
	}

	public static void info(String s)
	{
		log(Level.INFO, s);
	}

	public static void info(boolean log, String s)
	{
		if(log)
			info(s);
	}

	public static void error(String s)
	{
		log(Level.SEVERE, s);
	}

	public static void error(String s, Throwable e)
	{
		log(Level.SEVERE, s ,e);
	}

	public static void error(boolean log, String s, Throwable e)
	{
		if(log)
			error(s, e);
	}

	public static void log(Level level, String s)
	{
		getLogger().log(level, s);
		listeners.forEach(logListener -> logListener.onLogMessage(level, s));
	}

	public static void log(Level level, String s, Throwable e)
	{
		getLogger().log(level, s, e);
		listeners.forEach(logListener -> logListener.onLogMessage(level, s, e));

	}
}
