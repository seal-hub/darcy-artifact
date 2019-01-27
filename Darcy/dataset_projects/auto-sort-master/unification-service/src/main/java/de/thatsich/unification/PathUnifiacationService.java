package de.thatsich.unification;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.UUID.randomUUID;

public class PathUnifiacationService {
	public Path uniquefy(Path file) {
		final String fileName = file.getFileName().toString();

		final String extension = FilenameUtils.getExtension(fileName);
		final boolean hasExtension = !extension.isEmpty();
		final String base = FilenameUtils.removeExtension(fileName);
		final String uniqueFactor = randomUUID().toString();

		final String uniqueName = base + "-" + uniqueFactor + (hasExtension? "." + extension : "");

		return Paths.get(uniqueName);
	}
}
