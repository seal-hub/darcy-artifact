package firemage.constantin.core;

import java.util.Vector;

public class Mind {

	private static Mind INSTANCE;
	
	public static synchronized Mind getInstance() {
		if(INSTANCE == null)
			INSTANCE = new Mind();
		
		return INSTANCE;
	}
	
	private Vector<Association> associations;

	public Vector<Association> getAssociations() {
		return associations;
	}

	public void setAssociations(Vector<Association> associations) {
		this.associations = associations;
	}
}
