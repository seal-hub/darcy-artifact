package firemage.constantin.database.basic;

import java.sql.Connection;

public class Connector {

	private Connection connection;

	private Connector() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	public boolean insert(DBObject obj) {
		throw new UnsupportedOperationException();
	}

	public boolean delete(DBObject obj) {
		throw new UnsupportedOperationException();
	}

	public boolean update(DBObject obj) {
		throw new UnsupportedOperationException();
	}

	public DBObject find(String textElement) {
		throw new UnsupportedOperationException();
	}

	public DBObject find(DBEmotion emotion) {
		throw new UnsupportedOperationException();
	}

}
