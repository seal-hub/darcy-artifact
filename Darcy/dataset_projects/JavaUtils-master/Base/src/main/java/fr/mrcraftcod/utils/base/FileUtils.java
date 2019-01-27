package fr.mrcraftcod.utils.base;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class FileUtils
{
	public static File getAppDataFolder()
	{
		if(OSUtils.isMac())
			return new File(getHomeFolder(), "/Library/Application Support");
		return new File(getHomeFolder(),"AppData\\Roaming\\");
	}

	public static File getAppDataFolder(String path)
	{
		return new File(getAppDataFolder(), path);
	}

	public static void createDirectories(File file)
	{
		if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
	}

	public static File getDesktopFolder()
	{
		return new File(getHomeFolder(), "Desktop");
	}

	public static File getDesktopFolder(String path)
	{
		return new File(getDesktopFolder(), path);
	}

	public static File getHomeFolder()
	{
		return new File(System.getProperty("user.home"));
	}

	public static File getHomeFolder(String path)
	{
		return new File(getHomeFolder(), path);
	}

	public static String sanitizeFileName(String name)
	{
		return name.chars().mapToObj(i -> (char) i).filter(c -> Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == ' ' || c == '.').map(String::valueOf).collect(Collectors.joining());
	}

	public static boolean forceDelete(File file)
	{
		try
		{
			org.apache.commons.io.FileUtils.forceDelete(file);
			return true;
		}
		catch(IOException e){}
		return false;
	}

	public static File askDirectory()
	{
		return askDirectory(null);
	}

	public static File askDirectory(File defaultFile)
	{
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(defaultFile == null ? new File(".") : defaultFile);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		return null;
	}

	public static File askFile()
	{
		return askFile(null);
	}

	private static File askFile(File defaultFile)
	{
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(defaultFile == null ? new File(".") : defaultFile);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		return null;
	}
}
