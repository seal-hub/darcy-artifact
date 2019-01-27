package fr.mrcraftcod.utils.config;

import fr.mrcraftcod.utils.base.Log;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 12/09/2016.
 *
 * @author Thomas Couchoud
 * @since 2016-09-12
 */
public class PreparedStatementFiller
{
	HashMap<Integer, SQLValue> values;

	public PreparedStatementFiller(SQLValue... sqlValues)
	{
		this.values = new HashMap<>();
		for(int i = 0; i < sqlValues.length; i++)
			this.values.put(i + 1, sqlValues[i]);
	}

	public void fill(PreparedStatement statement)
	{
		for(int i : values.keySet())
			try
			{
				this.values.get(i).fill(i, statement);
			}
			catch(SQLException e)
			{
				Log.warning("Error filling prepared statement with parameter: " + i + " -> " + this.values.get(i));
			}
	}

	@Override
	public String toString()
	{
		return this.values.toString();
	}
}
