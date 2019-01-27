package de.thatsich.autosort.cli.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author thatsIch (thatsich@mail.de)
 * @version 1.0-SNAPSHOT 09.01.2018
 * @since 1.0-SNAPSHOT
 */
public class TargetDestinationParser {
	private final PropertiesLoader loader;
	private final Map<Alias, Path> aliases;

	public TargetDestinationParser(PropertiesLoader loader, Map<Alias, Path> aliases) {
		this.loader = loader;
		this.aliases = aliases;
	}

	private static Alias extractAlias(Map.Entry<String, String> entry) {
		return Alias.valueOf(entry.getValue().toUpperCase());
	}

	public Map<String, Path> parse() throws IOException {
		final Map<String, String> loaded = loader.loadTargets();

		return loaded.entrySet()
			.stream()
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> aliases.get(extractAlias(entry)))) ;
	}
}
