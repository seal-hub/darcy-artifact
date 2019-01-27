package de.thatsich.autosort.cli.def;

import de.thatsich.autosort.cli.HelpPrinter;
import de.thatsich.autosort.cli.JUPreferencesPersistence;
import de.thatsich.autosort.cli.Processor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class DefaultDirectoryProcessor implements Processor<Path> {
	private static final String SHORT_COMMAND = null;
	private static final String LONG_COMMAND = "default";
	private static final String SET_ARGS = "destination";
	private static final int MAX_ARGS = 1;
	private static final String DESCRIPTION = "manages default-directory.";
	private static final String ARG_NAME = "?" + SET_ARGS;

	private static final Logger LOGGER = LogManager.getLogger();

	private final HelpPrinter helpPrinter;
	private final JUPreferencesPersistence persistence;

	public DefaultDirectoryProcessor(final HelpPrinter helpPrinter, JUPreferencesPersistence persistence) {
		this.helpPrinter = helpPrinter;
		this.persistence = persistence;
	}

	@Override
	public Option constructOption() {
		return Option.builder(SHORT_COMMAND)
				.longOpt(LONG_COMMAND)
				.desc(DESCRIPTION)
				.hasArgs()
				.numberOfArgs(2)
				.optionalArg(true)
				.valueSeparator(' ')
				.argName(ARG_NAME)
				.build();
	}

	@Override
	public Path processCommandLine(CommandLine cl) {
		if (cl.hasOption(LONG_COMMAND)) {
			final String[] filterArgs = cl.getOptionValues(LONG_COMMAND);

			// we only get 'default' as command thus we want to print the default directory
			if (filterArgs == null) {
				final String defaultDirectory = persistence.retrieve();
				if (defaultDirectory.isEmpty()) {
					LOGGER.warn("Default directory has yet to be set. Defaulting to current working directory. Use the option 'default <directory>'.");
				}
				final Path defaultDirectoryPath = Paths.get(defaultDirectory).toAbsolutePath();

				LOGGER.info("Default directory: " + defaultDirectoryPath.toString());

				return defaultDirectoryPath;
			// guard against invalid inputs
			} else if (filterArgs.length > MAX_ARGS) {
				final Options options = new Options();
				options.addOption(this.constructOption());
				this.helpPrinter.printOptions(options);

				throw new IllegalArgumentException("At least '" + MAX_ARGS + "' arguments required but given '" + filterArgs.length + "' with '" + Arrays.toString(filterArgs) + "'.");
			}
			// we have exactly the required directory
			// we want to set the directory
			else {
				final String maybeDirectory = filterArgs[0];
				final Path defaultDirectoryPath = Paths.get(maybeDirectory).toAbsolutePath();
				final String stringified = defaultDirectoryPath.toString();
				persistence.persist(stringified);

				LOGGER.info("Stored '" + stringified + "' as default directory.");

				return defaultDirectoryPath;
			}
		}

		return null;
	}
}
