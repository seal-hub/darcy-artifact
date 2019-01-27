package de.thatsich.autosort.cli;

import java.util.prefs.Preferences;

public class JUPreferencesPersistence implements Persistence {
	private final String key;
	private final Preferences preferences;
	private String cache;

	public JUPreferencesPersistence(String key, Preferences preferences) {
		this.key = key;
		this.preferences = preferences;
	}

	@Override
	public void persist(String toBePersisted) {
		this.preferences.put(key, toBePersisted);

		this.cache = toBePersisted;
	}

	@Override
	public String retrieve() {
		if (this.cache == null) {
			this.cache = this.preferences.get(key, "");
		}

		return this.cache;
	}
}
