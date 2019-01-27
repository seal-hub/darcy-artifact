package de.thatsich.autosort.alias;

import de.thatsich.autosort.cli.alias.PathConverterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

class PathConverterServiceTest {

	@Test
	void toStrings() {
		final PathConverterService service = new PathConverterService();
		final String folder = "D:\\Download";
		final Map<String, Path> mapping = Map.of("Test", Paths.get(folder));

		final Map<String, String> stringified = service.toStrings(mapping);

		Assertions.assertEquals(folder, stringified.get("Test"));
	}

	@Test
	void toPaths() {
		final PathConverterService service = new PathConverterService();
		final String folder = "D:\\Download";
		final Map<String, String> mapping = Map.of("Test", folder);

		final Map<String, Path> pathified = service.toPaths(mapping);

		Assertions.assertEquals(Paths.get(folder), pathified.get("Test"));
	}
}
