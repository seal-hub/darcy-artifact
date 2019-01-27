package de.thatsich.autosort.filter;

import de.thatsich.autosort.cli.JUPreferencesPersistence;
import de.thatsich.autosort.cli.Persistence;
import de.thatsich.autosort.cli.filter.FilterRepository;
import de.thatsich.map.URLEncoderConverterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class FilterRepositoryTest {
	private Preferences preferences;
	private FilterRepository filterRepository;

	@BeforeEach
	void setUp() {
		this.preferences = Preferences.userNodeForPackage(FilterRepositoryTest.class);
		final Persistence persistence = new JUPreferencesPersistence("filter", preferences);
		final URLEncoderConverterService aliasConverterService = new URLEncoderConverterService();
		this.filterRepository = new FilterRepository(persistence, aliasConverterService);
	}

	@AfterEach
	void tearDown() throws BackingStoreException {
		this.preferences.clear();
	}

	@Test
	void persist() throws UnsupportedEncodingException {
		// given
		final String expected = "D:\\Download";

		// when
		filterRepository.persist("Test", expected);

		// then
		final Optional<String> finding = filterRepository.find("Test");
		Assertions.assertTrue(finding.isPresent());
		Assertions.assertEquals(expected, finding.get());
	}

	@Test
	void find() throws UnsupportedEncodingException {
		// given
		final String expected = "D:\\Download";
		filterRepository.persist("Test", expected);

		// when
		final Optional<String> finding = filterRepository.find("Test");

		// then
		Assertions.assertEquals(Optional.of(expected), finding);
	}

	@Test
	void remove() throws UnsupportedEncodingException {
		// given
		final String expected = "D:\\Download";
		filterRepository.persist("Test", expected);

		// when
		final Optional<String> maybeRemoved = filterRepository.remove("Test");

		// then
		Assertions.assertTrue(maybeRemoved.isPresent());
	}

	@Test
	void unmodifiable() {
		// given
		final Map<String, String> unmodifiable = filterRepository.unmodifiable();

		// when
		final Executable process = () -> unmodifiable.put("Test", "D:\\Download");

		// then
		Assertions.assertThrows(UnsupportedOperationException.class, process);
	}
}
