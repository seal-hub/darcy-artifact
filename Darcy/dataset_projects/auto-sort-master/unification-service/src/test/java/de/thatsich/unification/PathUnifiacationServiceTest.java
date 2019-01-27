package de.thatsich.unification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

class PathUnifiacationServiceTest {

	@Test
	void uniquefy() {
		// given
		final PathUnifiacationService service = new PathUnifiacationService();

		final Path start = Paths.get("").toAbsolutePath().resolve("pom.xml");

		// when
		final Path unique = service.uniquefy(start);

		// then
		Assertions.assertNotEquals(start, unique);
	}

	@Test
	void uniquefyWithoutExtensionShouldWork() {
		// given
		final PathUnifiacationService service = new PathUnifiacationService();

		final Path start = Paths.get("").toAbsolutePath().resolve("LICENSE");

		// when
		final Path unique = service.uniquefy(start);

		// then
		Assertions.assertNotEquals(start, unique);
	}
}
