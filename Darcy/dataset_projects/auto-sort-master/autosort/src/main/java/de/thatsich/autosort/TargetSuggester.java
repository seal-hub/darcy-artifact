package de.thatsich.autosort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author thatsIch (thatsich@mail.de)
 * @version 1.0-SNAPSHOT 15.01.2018
 * @since 1.0-SNAPSHOT
 */
class TargetSuggester {

	private static final Logger LOGGER = LogManager.getLogger();

	private String getSuggestions(List<Path> videos, Map<String, Path> targetToDestination) {
		return videos.stream()
			.map(Path::getFileName)
			.map(Object::toString)
			.map(fileName -> fileName.split(" - "))
			.map(splits -> splits[0])
			.distinct()
			.filter(youtuber -> notInTargetToDestinations(youtuber, targetToDestination))
			.map(youtuber -> youtuber.replace(" ", "\\ "))
			.map(youtuber -> "\t" + youtuber + "=misc")
			.collect(Collectors.joining("\n"));
	}

	void printSuggestions(List<Path> videos, Map<String, Path> targetToDestination) {
		final String suggestions = this.getSuggestions(videos, targetToDestination);
		if (!suggestions.isEmpty()) {
			LOGGER.info("Suggestions:");
			LOGGER.info(suggestions);
		}
	}

	private boolean notInTargetToDestinations(String youtuber, Map<String, Path> targetToDestination) {
		return !targetToDestination.keySet().contains(youtuber);
	}
}
