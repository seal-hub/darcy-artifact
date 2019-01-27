package de.thatsich.autosort.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class JUPreferencesPersistenceIT {


	private Preferences preferences;

	@BeforeEach
	void setUp() {
		this.preferences = Preferences.userNodeForPackage(JUPreferencesPersistenceIT.class);
	}

	@AfterEach
	void tearDown() throws BackingStoreException {
		preferences.clear();
	}

	@Test
	void testPersistRetrieve() {
		final Persistence persistence = new JUPreferencesPersistence("demo", this.preferences);
		final String expected = "Test";
		persistence.persist(expected);
		final String actual = persistence.retrieve();

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void testRetrieveWithoutPersist() {
		final Persistence  persistence = new JUPreferencesPersistence("demo", this.preferences);
		final String expected = "";
		final String actual = persistence.retrieve();

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void testRetrieveWithoutPersistFromPreferences() {
		// given
		final String expected = "Test";
		this.preferences.put("demo", expected);
		final JUPreferencesPersistence persistence = new JUPreferencesPersistence("demo", this.preferences);

		// when
		final String actual = persistence.retrieve();

		// then
		Assertions.assertEquals(expected, actual);
	}
}
