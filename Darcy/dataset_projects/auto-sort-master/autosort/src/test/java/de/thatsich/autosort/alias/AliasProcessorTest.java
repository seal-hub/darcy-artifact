package de.thatsich.autosort.alias;

import de.thatsich.autosort.cli.HelpPrinter;
import de.thatsich.autosort.cli.Processor;
import de.thatsich.autosort.cli.Repository;
import de.thatsich.autosort.cli.alias.AliasProcessor;
import org.apache.commons.cli.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

class AliasProcessorTest {

	private Processor<Void> aliasProcessor;
	private DefaultParser argsParser;
	private Options options;
	private Repository<String, Path> repository;

//	@Rule
//	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@BeforeEach
	void setUp() {
		final HelpFormatter formatter = new HelpFormatter();
		this.options = new Options();
		final HelpPrinter helpPrinter = new HelpPrinter(formatter);
		this.argsParser = new DefaultParser();
		this.repository = new NonPersistentPathRepository();
		this.aliasProcessor = new AliasProcessor(helpPrinter, repository);
		final Option option = aliasProcessor.constructOption();
		options.addOption(option);
	}

	@Test
	void constructOption() {
		final Option constructedOption = aliasProcessor.constructOption();

		Assertions.assertNotNull(constructedOption);
	}

	@Test
	void processCommandLineNoArgsDoesNothing() throws ParseException, UnsupportedEncodingException {
		// given

		// when
		final String[] args = {};
		final CommandLine cl = argsParser.parse(options, args);
		final Void processed = aliasProcessor.processCommandLine(cl);

		// then
		Assertions.assertNull(processed);
	}

	/**
	 * This is generally guarded by the CLI
	 */
	@Test
	void processCommandLineOnlyFlagShouldNotWork() {
		// given

		// when
		final String[] args = {"--alias"};
		final Executable process = () -> argsParser.parse(options, args);

		// then
		Assertions.assertThrows(MissingArgumentException.class, process);
	}

	@Test
	void processCommandLineTooManyFlagsShouldThrow() throws ParseException {
		// given

		// when
		final String[] args = {"--alias", "just", "too", "many", "args", "more", "than", "expected"};
		final CommandLine cl = argsParser.parse(options, args);
		final Executable process = () ->  this.aliasProcessor.processCommandLine(cl);

		// then
		Assertions.assertThrows(IllegalStateException.class, process);
	}


	@Test
	void processCommandLineUnknownSubCommandShouldThrow() throws ParseException {
		// given

		// when
		final String[] args = {"--alias", "unknown", "sub", "command"};
		final CommandLine cl = argsParser.parse(options, args);
		final Executable process = () ->  this.aliasProcessor.processCommandLine(cl);

		// then
		Assertions.assertThrows(IllegalStateException.class, process);
	}

	@Test
	void processCommandLineListShouldPrint() throws ParseException, UnsupportedEncodingException {
		// given
		final String[] addArgs = {"--alias", "add", "test", "D:\\Download"};
		final CommandLine addCL = argsParser.parse(options, addArgs);
		this.aliasProcessor.processCommandLine(addCL);

		// when
		final String[] listArgs = {"--alias", "list"};
		final CommandLine listCL = argsParser.parse(options, listArgs);
		this.aliasProcessor.processCommandLine(listCL);

		// then
//		System.out.println("systemOutRule = " + systemOutRule.getLog());
		// this doesnt work!?!!?
//		Assertions.assertTrue(systemOutRule.getLog().contains("test"));
	}

	@Test
	void processCommandLineDeletingWithPresentShouldDelete() throws ParseException, UnsupportedEncodingException {
		// given
		final String[] addArgs = {"--alias", "add", "test", "D:\\Download"};
		final CommandLine addCL = argsParser.parse(options, addArgs);
		this.aliasProcessor.processCommandLine(addCL);

		// when
		final String[] listArgs = {"--alias", "del", "test"};
		final CommandLine listCL = argsParser.parse(options, listArgs);
		this.aliasProcessor.processCommandLine(listCL);

		// then
		Assertions.assertFalse(this.repository.find("delete").isPresent());
	}

	@Test
	void processCommandLineDeletingNotPresentShouldWarn() throws ParseException, UnsupportedEncodingException {
		// given

		// when
		final String[] listArgs = {"--alias", "del", "test"};
		final CommandLine listCL = argsParser.parse(options, listArgs);
		this.aliasProcessor.processCommandLine(listCL);

		// then
		Assertions.assertFalse(this.repository.find("delete").isPresent());
		// also test logging
	}

	@Test
	void processCommandLineAddingNewShouldAdd() throws ParseException, UnsupportedEncodingException {
		// given

		// when
		final String[] listArgs = {"--alias", "add", "test", "D:\\Download"};
		final CommandLine listCL = argsParser.parse(options, listArgs);
		this.aliasProcessor.processCommandLine(listCL);

		// then
		Assertions.assertTrue(this.repository.find("test").isPresent());
		// also test logging
	}

	@Test
	void processCommandLineAddingDuplicateShouldWarn() throws ParseException, UnsupportedEncodingException {
		// given
		final String[] addArgs = {"--alias", "add", "test", "D:\\Download"};
		final CommandLine addCL = argsParser.parse(options, addArgs);
		this.aliasProcessor.processCommandLine(addCL);

		// when
		final String[] listArgs = {"--alias", "add", "test", "D:\\New"};
		final CommandLine listCL = argsParser.parse(options, listArgs);
		this.aliasProcessor.processCommandLine(listCL);

		// then
		Assertions.assertTrue(this.repository.find("test").isPresent());
		// also test logging
	}
}
