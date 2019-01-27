package de.thatsich.autosort.alias;

import de.thatsich.autosort.cli.JUPreferencesPersistence;
import de.thatsich.autosort.cli.Persistence;
import de.thatsich.autosort.cli.alias.AliasRepository;
import de.thatsich.autosort.cli.alias.PathConverterService;
import de.thatsich.map.URLEncoderConverterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class AliasRepositoryTest {

	private Preferences preferences;
	private AliasRepository aliasRepository;

	@BeforeEach
	void setUp() {
		this.preferences = Preferences.userNodeForPackage(AliasRepositoryTest.class);
		final Persistence persistence = new JUPreferencesPersistence("alias", preferences);
		final PathConverterService pathConverterService = new PathConverterService();
		final URLEncoderConverterService aliasConverterService = new URLEncoderConverterService();
		this.aliasRepository = new AliasRepository(persistence, pathConverterService, aliasConverterService);
	}

	@AfterEach
	void tearDown() throws BackingStoreException {
		this.preferences.clear();
	}

	@Test
	void persist() throws UnsupportedEncodingException {
		// given
		final Path expected = Paths.get("D:\\Download");

		// when
		aliasRepository.persist("Test", expected);

		// then
		Assertions.assertTrue(aliasRepository.find("Test").isPresent());
		Assertions.assertEquals(expected, aliasRepository.find("Test").get());
	}

	@Test
	void find() throws UnsupportedEncodingException {
		// given
		final Path expected = Paths.get("D:\\Download");
		aliasRepository.persist("Test", expected);

		// when
		final Optional<Path> finding = aliasRepository.find("Test");

		// then
		Assertions.assertEquals(Optional.of(expected), finding);
	}

	@Test
	void remove() throws UnsupportedEncodingException {
		// given
		final Path expected = Paths.get("D:\\Download");
		aliasRepository.persist("Test", expected);

		// when
		final Optional<Path> maybeRemoved = aliasRepository.remove("Test");

		// then
		Assertions.assertTrue(maybeRemoved.isPresent());
	}

	@Test
	void unmodifiable() throws UnsupportedEncodingException {
		// given
		final Map<String, Path> unmodifiable = aliasRepository.unmodifiable();

		// when
		final Executable process = () -> unmodifiable.put("Test", Paths.get("D:\\Download"));

		// then
		Assertions.assertThrows(UnsupportedOperationException.class, process);
	}
}
