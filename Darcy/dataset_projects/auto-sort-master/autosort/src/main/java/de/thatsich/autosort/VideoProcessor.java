package de.thatsich.autosort;

import de.thatsich.unification.PathUnifiacationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author thatsIch (thatsich@mail.de)
 * @version 1.0-SNAPSHOT 15.01.2018
 * @since 1.0-SNAPSHOT
 */
public class VideoProcessor {

	private static final Logger LOGGER = LogManager.getLogger();

	private final PathUnifiacationService unifiacationService;

	VideoProcessor(PathUnifiacationService unifiacationService) {
		this.unifiacationService = unifiacationService;
	}

	public void process(Path workingDirectory) throws IOException {
		final List<Path> videos = Files.walk(workingDirectory, 1)
			.filter(Files::isRegularFile)
			.filter(file -> file.getFileName().toString().endsWith(".mp4"))
			.collect(Collectors.toList());

		// TODO
		final Map<String, Path> targetToDestination = new HashMap<>();

		final TargetSuggester suggester = new TargetSuggester();
		suggester.printSuggestions(videos, targetToDestination);

		videos.forEach(file -> {
			final String fileName = file.getFileName().toString();
			targetToDestination.forEach((key, dest) -> {
				if (fileName.startsWith(key)) {
					try {
						LOGGER.info(fileName);
						LOGGER.info("\tDestination: " + dest);
						final Path existing = getResolvedExistingPath(dest, fileName);
						Files.move(file, existing);
						LOGGER.info("\tMoved.");
					} catch (FileAlreadyExistsException e) {
						final Path existing = dest.resolve(fileName);
						LOGGER.info("\tAlready existing.");
						LOGGER.info("\tChecking for file size.");
						try {
							final long sourceSize = Files.size(existing);
							final long targetSize = Files.size(file);

							// in case the files are called the same and have the same size we can argue, that it is very likely that it is the same video content
							if (sourceSize == targetSize) {
								LOGGER.info("\t\tFound same size: deleting source file.");
								Files.delete(file);
								LOGGER.info("Deleted.");
							} else {
								LOGGER.info("\t\tFound different size: starting unification.");
								final Path unique = unifiacationService.uniquefy(file);
								LOGGER.info("\t\tUnique name: " + unique);
								final Path moved = Files.move(file, dest.resolve(unique));
								LOGGER.info("\t\tMoved: " + moved);
							}
						} catch (IOException el) {
							LOGGER.error("\tDeduplication failed.", el);
						}
					} catch (IOException e) {
						LOGGER.error("Failed", e);
					}
				}
			});
		});
	}

	private Path getResolvedExistingPath(Path parent, String name) throws IOException {
		final Path path = parent.resolve(name);

		if (!Files.exists(parent))
			Files.createDirectory(parent);

		return path;
	}
}
