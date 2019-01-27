package fr.mrcraftcod.utils.resources;

import fr.mrcraftcod.utils.base.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

public class ResourcesBase
{
	private static final HashMap<String, Properties> properties = new HashMap<>();
	private final Class rootClass;

	public ResourcesBase(Class rootClass)
	{
		this.rootClass = rootClass;
	}

	public URL getResource(ResourceElement resourceElement, String path)
	{
		return this.rootClass.getResource("/" + resourceElement.getRootPath() + (resourceElement.getRootPath().equals("") ? "" : "/") + path);
	}

	public WritableImage getImage(ResourceElement resourceElement, String path, int width, int height)
	{
		try
		{
			return SwingFXUtils.toFXImage(ImageUtils.resizeBufferedImage(ImageIO.read(getResource(resourceElement, path)), width, height), null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String getPropertyString(ResourceElement resourceElement, String path, String key)
	{
		try
		{
			return getProperties(resourceElement, path).getProperty(key, "");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "--";
	}

	public Properties getProperties(ResourceElement resourceElement, String path) throws IOException
	{
		if(properties.containsKey(path))
			return properties.get(path);
		Properties prop = new Properties();
		prop.load(new File("./", path + ".properties").exists() ? new InputStreamReader(new FileInputStream(new File("./", path + ".properties")), "UTF-8") : new InputStreamReader(getResource(resourceElement, path + ".properties").openStream(), "UTF-8"));
		properties.put(path, prop);
		return prop;
	}

	public Font getFont(ResourceElement resourceElement, String path, double size) throws IOException
	{
		return Font.loadFont(getResource(resourceElement, path).openConnection().getInputStream(), size);
	}
}
