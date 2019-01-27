package fr.mrcraftcod.utils.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 12/09/2016.
 *
 * @author Thomas Couchoud
 * @since 2016-09-12
 */
public class SQLValue
{
	private final Object value;
	private final Type type;

	public enum Type
	{
		INT, STRING, LONG, DOUBLE
	}

	public SQLValue(Type type, Object value)
	{
		this.type = type;
		this.value = value;
	}

	public void fill(int i, PreparedStatement statement) throws SQLException
	{
		switch(this.type)
		{
			case INT:
				statement.setInt(i, (Integer) this.value);
				break;
			case LONG:
				statement.setLong(i, (Long) this.value);
				break;
			case DOUBLE:
				statement.setDouble(i, (Double) this.value);
				break;
			case STRING:
			default:
				statement.setString(i, this.value.toString());
				break;
		}
	}

	@Override
	public String toString()
	{
		return this.type.toString() + " -> " + this.value.toString();
	}
}
