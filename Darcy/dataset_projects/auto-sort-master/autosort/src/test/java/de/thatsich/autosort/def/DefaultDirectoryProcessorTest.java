package de.thatsich.autosort.def;

import de.thatsich.autosort.cli.HelpPrinter;
import de.thatsich.autosort.cli.JUPreferencesPersistence;
import de.thatsich.autosort.cli.def.DefaultDirectoryProcessor;
import org.apache.commons.cli.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class DefaultDirectoryProcessorTest {

	private Preferences preferences;
	private DefaultDirectoryProcessor defaultDirectoryProcessor;
	private DefaultParser argsParser;
	private Options options;

	@BeforeEach
	void setUp() {
		final HelpFormatter formatter = new HelpFormatter();
		this.options = new Options();
		final HelpPrinter helpPrinter = new HelpPrinter(formatter);
		this.argsParser = new DefaultParser();
		this.preferences = Preferences.userNodeForPackage(DefaultDirectoryProcessorTest.class);
		final JUPreferencesPersistence persistence = new JUPreferencesPersistence("default", preferences);
		this.defaultDirectoryProcessor = new DefaultDirectoryProcessor(helpPrinter, persistence);
		final Option option = defaultDirectoryProcessor.constructOption();
		options.addOption(option);
	}

	@AfterEach
	void tearDown() throws BackingStoreException {
		preferences.clear();
	}

	@Test
	void constructOption() {
		final HelpFormatter formatter = new HelpFormatter();
		final HelpPrinter helpPrinter = new HelpPrinter(formatter);
		final Preferences preferences = Preferences.userNodeForPackage(DefaultDirectoryProcessorTest.class);
		final JUPreferencesPersistence persistence = new JUPreferencesPersistence("default", preferences);
		final DefaultDirectoryProcessor defaultDirectoryProcessor = new DefaultDirectoryProcessor(helpPrinter, persistence);

		final Option constructedOption = defaultDirectoryProcessor.constructOption();

		Assertions.assertNotNull(constructedOption);
	}

	@Test
	void processCommandLineGetterShouldWork() throws ParseException {
		// given

		// when
		final String[] args = {"--default"};
		final CommandLine cl = argsParser.parse(options, args);
		final Path defaultPath = defaultDirectoryProcessor.processCommandLine(cl);

		// then
		Assertions.assertEquals(defaultPath, Paths.get("").toAbsolutePath());
	}

	@Test
	void processCommandLineSetterShouldWork() throws ParseException {
		// given

		// when
		final String[] args = {"--default", "C:"};
		final CommandLine cl = argsParser.parse(options, args);
		final Path defaultPath = defaultDirectoryProcessor.processCommandLine(cl);

		// then
		Assertions.assertEquals(defaultPath, Paths.get("C:").toAbsolutePath());
	}

	@Test
	void processCommandLineGetterAfterSetterShouldWork() throws ParseException {
		// given

		// when
		// setting
		final String[] setArgs = {"--default", "C:"};
		final CommandLine setCL = argsParser.parse(options, setArgs);
		final Path setPath = defaultDirectoryProcessor.processCommandLine(setCL);


		// getting
		// setting
		final String[] getArgs = {"--default"};
		final CommandLine getCL = argsParser.parse(options, getArgs);
		final Path defaultPath = defaultDirectoryProcessor.processCommandLine(getCL);

		// then
		Assertions.assertEquals(defaultPath, setPath);
	}

	@Test
	void processCommandLineWithTooManyArgsShouldThrow() throws ParseException {
		// given

		// when
		// setting
		final String[] setArgs = {"--default", "C:", "D:"};
		final CommandLine setCL = argsParser.parse(options, setArgs);

		// then
		Assertions.assertThrows(IllegalArgumentException.class, () -> defaultDirectoryProcessor.processCommandLine(setCL));
	}

	@Test
	void processCommandLineWithoutFlagDoNothing() throws ParseException {
		// given

		// when
		// setting
		final String[] setArgs = {};
		final CommandLine setCL = argsParser.parse(options, setArgs);
		final Path absentPath = defaultDirectoryProcessor.processCommandLine(setCL);

		// then
		Assertions.assertNull(absentPath);
	}
}
