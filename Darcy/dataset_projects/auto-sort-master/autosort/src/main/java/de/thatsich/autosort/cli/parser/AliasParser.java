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
public class AliasParser {

	private final PropertiesLoader loader;

	public AliasParser(PropertiesLoader loader) {
		this.loader = loader;
	}

	public Map<Alias, Path> parse(Path base) throws IOException {
		final Map<String, String> loaded = loader.loadAliases();

		return loaded.entrySet()
			.stream()
			.collect(Collectors.toMap(this::extractAlias, entry -> this.extractPath(entry, base)));
	}

	private Alias extractAlias(Map.Entry<String, String> entry) {
		final String propertyName = entry.getKey();
		final String upper = propertyName.toUpperCase();

		return Alias.valueOf(upper);
	}

	private Path extractPath(Map.Entry<String, String> entry, Path base) {
		final String value = entry.getValue();

		return base.resolve(value);
	}
}
