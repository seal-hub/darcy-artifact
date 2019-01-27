package de.thatsich.autosort.cli.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author thatsIch (thatsich@mail.de)
 * @version 1.0-SNAPSHOT 09.01.2018
 * @since 1.0-SNAPSHOT
 */
public class PropertiesLoader {

	private static final String ALIAS_FILE_NAME = "alias.destination";
	private static final String TARGETS_FILE_NAME = "target.destination";

	Map<String, String> loadAliases() throws IOException {
		return load(ALIAS_FILE_NAME);
	}

	Map<String, String> loadTargets() throws IOException {
		return load(TARGETS_FILE_NAME);
	}

	private Map<String, String> load(String fileName) throws IOException {
		final InputStream resourceStream = AliasParser.class.getResourceAsStream(fileName);
		final Properties properties = new Properties();
		properties.load(resourceStream);

		return properties.entrySet()
			.stream()
			.collect(Collectors.toMap(this::stringifyKey, this::stringifyValue));
	}

	private String stringifyKey(Map.Entry<Object, Object> entry) {
		return entry.getKey().toString();
	}

	private String stringifyValue(Map.Entry<Object, Object> entry) {
		return entry.getValue().toString();
	}
}
