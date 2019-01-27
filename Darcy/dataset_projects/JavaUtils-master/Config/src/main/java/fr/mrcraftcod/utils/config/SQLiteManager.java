package fr.mrcraftcod.utils.config;

import fr.mrcraftcod.utils.base.FileUtils;
import fr.mrcraftcod.utils.base.Log;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteManager extends JDBCBase
{
	private File databaseURL;

	public SQLiteManager(File databaseURL, boolean log) throws ClassNotFoundException
	{
		super("SQLITE/" + databaseURL, log);
		Class.forName("org.sqlite.JDBC");
		FileUtils.createDirectories(databaseURL);
		this.databaseURL = databaseURL;
		login();
		Log.info("Initializing SQL connection...");
	}

	protected void login()
	{
		try
		{
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.databaseURL.getAbsolutePath());
		}
		catch(SQLException e)
		{
			Log.warning("Error connecting to SQL database!", e);
		}
	}
}
