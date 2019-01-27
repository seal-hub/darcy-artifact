package fr.mrcraftcod.utils.base;

import java.io.IOException;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 25/09/2016.
 *
 * @author Thomas Couchoud
 * @since 2016-09-25
 */
public class OSUtils
{
	public static void notify(String title, String message)
	{
		if (isMac())
			try
			{
				Runtime.getRuntime().exec(new String[] { "osascript", "-e", "display notification \"" + message + "\" with title \"" + title + "\""});
			}
			catch(IOException e)
			{
				Log.warning("Error while sending notification", e);
			}
	}

	public static boolean isMac()
	{
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}
}
