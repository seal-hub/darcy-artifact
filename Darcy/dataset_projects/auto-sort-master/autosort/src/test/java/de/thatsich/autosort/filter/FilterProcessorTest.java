package de.thatsich.autosort.filter;

import de.thatsich.autosort.cli.HelpPrinter;
import de.thatsich.autosort.cli.filter.FilterProcessor;
import org.apache.commons.cli.*;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.UnsupportedEncodingException;

class FilterProcessorTest {

	private FilterProcessor filterProcessor;
	private DefaultParser argsParser;
	private Options options;
	private NonPersistentStringRepository repository;

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();

	@BeforeEach
	void setUp() {
		final HelpFormatter formatter = new HelpFormatter();
		this.options = new Options();
		final HelpPrinter helpPrinter = new HelpPrinter(formatter);
		this.argsParser = new DefaultParser();
		this.repository = new NonPersistentStringRepository();
		this.filterProcessor = new FilterProcessor(helpPrinter, repository);
		final Option option = filterProcessor.constructOption();
		options.addOption(option);
	}

	@Test
	void constructOption() {
		Assertions.assertEquals(options.getOptions().size(), 1);
	}

	@Test
	void processCommandLineNoInputShouldDoNothing() throws ParseException, UnsupportedEncodingException {
		// given

		// when
		final String[] args = {};
		final CommandLine setCL = argsParser.parse(options, args);
		final Void processed = filterProcessor.processCommandLine(setCL);

		// then
		Assertions.assertNull(processed);
	}

	@Test
	void processCommandLineWithFlagThrowsExceptionByFramework() {
		// given

		// when
		final String[] args = {"--filter"};
		final Executable setCL = () -> argsParser.parse(options, args);

		// then
		Assertions.assertThrows(MissingArgumentException.class, setCL);
	}

	@Test
	void processCommandLineWithTooManyFlagsThrowsException() throws ParseException {
		// given
//		System.setOut(new PrintStream());

		// when
		final String[] args = {"--filter", "foo", "bar", "batz", "foobar"};
		final CommandLine setCL = argsParser.parse(options, args);
		final Executable process = () -> filterProcessor.processCommandLine(setCL);

		// then
		Assertions.assertThrows(IllegalArgumentException.class, process);
	}

	@Test
	void processCommandLineWithUnknownArgThrowsException() throws ParseException {
		// given
//		System.setOut(new PrintStream());

		// when
		final String[] args = {"--filter", "foo"};
		final CommandLine setCL = argsParser.parse(options, args);
		final Executable process = () -> filterProcessor.processCommandLine(setCL);

		// then
		Assertions.assertThrows(UnsupportedOperationException.class, process);
	}

	@Test
	void processCommandLineWithCorrectAddShouldAdd() throws ParseException, UnsupportedEncodingException {
		// given

		// when
		final String[] args = {"--filter", "add", "*.mp4", "D:\\Download\\Anime"};
		final CommandLine setCL = argsParser.parse(options, args);
		filterProcessor.processCommandLine(setCL);

		// then
		Assertions.assertTrue(this.repository.find("*.mp4").isPresent());
		// also test logging
	}

	@Test
	void processCommandLineAddingDuplicateShouldWarn() throws ParseException, UnsupportedEncodingException {
		// given
		final String[] firstArgs = {"--filter", "add", "*.mp4", "D:\\Download\\Anime"};
		final CommandLine firstCL = argsParser.parse(options, firstArgs);
		this.filterProcessor.processCommandLine(firstCL);

		// when
		final String[] secArgs = {"--filter", "add", "*.mp4", "D:\\Download\\Anime"};
		final CommandLine secCL = argsParser.parse(options, secArgs);
		this.filterProcessor.processCommandLine(secCL);

		// then
		Assertions.assertTrue(this.repository.find("*.mp4").isPresent());
		// also test logging
	}

	@Test
	void processCommandLineWithCorrectRemoveShouldWork() throws ParseException, UnsupportedEncodingException {
		// given
		final String[] addArgs = {"--filter", "add", "*.mp4", "D:\\Download\\Anime"};
		final CommandLine addCL = argsParser.parse(options, addArgs);
		filterProcessor.processCommandLine(addCL);

		// when
		final String[] delArgs = {"--filter", "delete", "*.mp4"};
		final CommandLine delCL = argsParser.parse(options, delArgs);
		filterProcessor.processCommandLine(delCL);

		// then
		Assertions.assertFalse(this.repository.find("*.mp4").isPresent());
	}

	@Test
	void processCommandLineDeletingNotPresentShouldWarn() throws ParseException, UnsupportedEncodingException {
		// given

		// when
		final String[] listArgs = {"--filter", "delete", "*.mp4"};
		final CommandLine listCL = argsParser.parse(options, listArgs);
		this.filterProcessor.processCommandLine(listCL);

		// then
		Assertions.assertFalse(this.repository.find("*.mp4").isPresent());
		// also test logging
	}

	@Test
	void processCommandLineWithListPrints() throws ParseException, UnsupportedEncodingException {
		// given

		// when
		final String[] args = {"--filter", "list"};
		final CommandLine setCL = argsParser.parse(options, args);
		filterProcessor.processCommandLine(setCL);

		// then
		Assertions.assertNotNull(systemOutRule.getLog());
	}

	// needs to list all filters
	// should work with empty list
	// one filter
	// more filters
	// can check against the output stream and see if logs contains them
	//	final String[] args = {"--filter", "list"};

	// missing argument
	//	final String[] args = {"--filter", "delete"};
	// should work if filter exists, else it doesnt
	//	final String[] args = {"--filter", "delete", "non-existing-filter"};

	// missing arguments
	//	final String[] args = {"--filter", "add"};
	//	final String[] args = {"--filter", "add", "regex"};

	// invalid case if neither of list, delete or add is being called
	// validate regex being a regex

	//	final String[] args = {};
	//	final String[] args = {};
	//	final String[] args = {};

}
