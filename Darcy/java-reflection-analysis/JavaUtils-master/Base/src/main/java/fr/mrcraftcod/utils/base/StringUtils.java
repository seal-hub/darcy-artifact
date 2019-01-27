package fr.mrcraftcod.utils.base;

import java.text.DecimalFormat;

public class StringUtils
{
	private final static String[] UNITS_PREFIX = {"", "K", "M", "G", "T", "P"};

	public static String getDownloadSizeText(double size)
	{
		int unit = 0;
		while(size >= 1024)
		{
			unit++;
			size /= 1024;
		}
		return new DecimalFormat("0.00").format(size) + " " + UNITS_PREFIX[unit] + "B";
	}

	public static String getEnding(String string, char separator, int maxSeparator, String replaceSeparator)
	{
		while(countChar(string, separator) > maxSeparator)
			string = string.substring(string.indexOf(separator) + 1);
		return string.replace("" + separator, replaceSeparator);
	}

	private static int countChar(String string, char c)
	{
		int count = 0;
		for(char ch : string.toCharArray())
			if(ch == c)
				count++;
		return count;
	}
}
