package de.thatsich.autosort.cli;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public abstract class BaseProcessor<T> implements Processor<T> {
	protected abstract String getShortCommand();

	protected abstract String getLongCommand();

	protected abstract List<String> getAddArgs();

	protected abstract List<String> getDelArgs();

	protected abstract List<String> getListArgs();

	protected int getMaxArgs() {
		return Math.max(getAddArgs().size(), Math.max(getDelArgs().size(), getListArgs().size()));
	}

	protected String getArgNames() {
		return this.constructArgNames(List.of(getAddArgs(), getDelArgs(), getListArgs()));
	}

	private String constructArgNames(List<List<String>> args) {
		final StringJoiner joiner = new StringJoiner("|");
		for (List<String> arg : args) {
			joiner.add(arg.stream().collect(Collectors.joining(" ")));
		}

		return joiner.toString();
	}

}
