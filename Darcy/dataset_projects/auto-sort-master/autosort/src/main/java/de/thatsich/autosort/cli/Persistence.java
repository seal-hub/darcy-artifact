package de.thatsich.autosort.cli;

public interface Persistence {
	void persist(String toBePersisted);

	String retrieve();
}
