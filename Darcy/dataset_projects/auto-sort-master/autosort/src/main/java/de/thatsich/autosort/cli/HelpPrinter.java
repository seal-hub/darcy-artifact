package de.thatsich.autosort.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class HelpPrinter {

	private final HelpFormatter formatter;

	public HelpPrinter(HelpFormatter formatter) {
		this.formatter = formatter;
	}

	public void printOptions(Options options) {
		formatter.setOptionComparator(null);
		formatter.setWidth(140);
		formatter.printHelp("de/thatsich/autosort", options);
	}
}
