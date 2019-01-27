package fr.mrcraftcod.utils.config;

import fr.mrcraftcod.utils.base.Log;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MYSQLManager extends JDBCBase
{
	private String databaseURL;
	private int port;
	private String databaseName;
	private String user;
	private String password;

	public MYSQLManager(String databaseURL, int port, String databaseName, String user, String password, boolean log)
	{
		super("MYSQL/" + databaseURL + "/" + databaseName + ":" + port + ":" + user, log);
		this.databaseURL = databaseURL;
		this.port = port;
		this.databaseName = databaseName;
		this.user = user;
		this.password = password;
		login();
		Log.info("Initializing SQL connection...");
	}

	protected void login()
	{
		try
		{
			this.connection = DriverManager.getConnection("jdbc:mysql://" + this.databaseURL + ":" + this.port + "/" + this.databaseName + "?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=UTC", this.user, this.password);
		}
		catch(SQLException e)
		{
			Log.warning("Error connecting to SQL database!", e);
		}
	}
}
