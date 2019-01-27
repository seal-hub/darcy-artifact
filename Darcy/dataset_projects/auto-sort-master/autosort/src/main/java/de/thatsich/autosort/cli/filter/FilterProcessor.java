package de.thatsich.autosort.cli.filter;

import de.thatsich.autosort.cli.BaseProcessor;
import de.thatsich.autosort.cli.HelpPrinter;
import de.thatsich.autosort.cli.Repository;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FilterProcessor extends BaseProcessor<Void> {
	private static final String DESCRIPTION = "manages filters defined in the filter mapping.";

	private static final Logger LOGGER = LogManager.getLogger();

	private final HelpPrinter helpPrinter;
	private final Repository<String, String> repository;

	public FilterProcessor(final HelpPrinter helpPrinter, Repository<String, String> repository) {
		this.helpPrinter = helpPrinter;
		this.repository = repository;
	}

	@Override
	public Option constructOption() {
		return Option.builder(getShortCommand())
				.longOpt(getLongCommand())
				.desc(DESCRIPTION)
				.hasArgs()
				.valueSeparator(' ')
				.argName(getArgNames())
				.build();
	}

	@Override
	public Void processCommandLine(CommandLine cl) throws UnsupportedEncodingException {
		if (cl.hasOption(getLongCommand())) {
			// can never be null because the cl filters that case
			final String[] filterArgs = cl.getOptionValues(getLongCommand());

			final Option option = this.constructOption();
			final Options options = new Options();
			options.addOption(option);

			if (filterArgs.length > getMaxArgs()) {
				helpPrinter.printOptions(options);

				throw new IllegalArgumentException("filter commands requires specific arguments with max '"+getMaxArgs()+"' arguments.");
			}

			final String subCommand = filterArgs[0];
			final boolean processed = this.tryAdding(subCommand, filterArgs) ||
					this.tryDeleting(subCommand, filterArgs) ||
					this.tryListing(subCommand);

			if (!processed) {
				final String message = "processing command 'filter' but found no matching sub-command '" + subCommand + "' with args '"+ Arrays.toString(filterArgs)+"'.";
				LOGGER.error(message);

				helpPrinter.printOptions(options);

				throw new UnsupportedOperationException(message);
			}
		}

		return null;
	}

	private boolean tryAdding(String subCommand, String[] aliasArgs) throws UnsupportedEncodingException {
		if (subCommand.equals(getAddArgs().get(0))) {
			final String regex = aliasArgs[1];
			final Optional<String> binding = this.repository.find(regex);
			if (binding.isPresent()) {
				LOGGER.warn("Filter '"+regex+"' is already present with the binding '" + binding.get() + "'.");
			} else {
				final String destination = aliasArgs[2];

				this.repository.persist(regex, destination);
			}

			return true;
		}

		return false;
	}

	private boolean tryDeleting(String subCommand, String[] aliasArgs) throws UnsupportedEncodingException {
		if (subCommand.equals(getDelArgs().get(0))) {
			final String regex = aliasArgs[1];

			final Optional<String> binding = this.repository.remove(regex);
			if (!binding.isPresent()) {
				LOGGER.warn("No binding found for regex '" + regex + "'.");
			}

			return true;
		}

		return false;
	}

	private boolean tryListing(String subCommand) {
		if (subCommand.equals(getListArgs().get(0))) {
			this.repository.unmodifiable().forEach((regex, binding) -> LOGGER.info(regex + " -> " + binding));

			return true;
		}

		return false;
	}

	@Override
	protected String getShortCommand() {
		return null;
	}

	@Override
	protected String getLongCommand() {
		return "filter";
	}

	@Override
	protected List<String> getAddArgs() {
		return List.of("add", "regex", "destination");
	}

	@Override
	protected List<String> getDelArgs() {
		return List.of("delete", "regex");
	}

	@Override
	protected List<String> getListArgs() {
		return List.of("list");
	}
}
