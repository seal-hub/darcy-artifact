package de.thatsich.autosort.cli.alias;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PathConverterService {
	public Map<String, String> toStrings(Map<String, Path> paths) {
		final Map<String, String> result = new HashMap<>(paths.size());
		for (Map.Entry<String, Path> entry : paths.entrySet()) {
			final String value = entry.getValue().toString();
			result.put(entry.getKey(), value);
		}

		return result;
	}

	public Map<String, Path> toPaths(Map<String, String> strings) {
		final Map<String, Path> result = new HashMap<>(strings.size());
		for (Map.Entry<String, String> entry : strings.entrySet()) {
			final String value = entry.getValue();
			result.put(entry.getKey(), Paths.get(value));
		}

		return result;
	}
}
