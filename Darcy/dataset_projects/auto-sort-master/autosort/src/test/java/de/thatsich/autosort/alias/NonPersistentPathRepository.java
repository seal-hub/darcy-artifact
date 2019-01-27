package de.thatsich.autosort.alias;

import de.thatsich.autosort.cli.Repository;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NonPersistentPathRepository implements Repository<String, Path> {
	private final Map<String, Path> internal = new HashMap<>();

	@Override
	public void persist(String s, Path path) throws UnsupportedEncodingException {
		this.internal.put(s, path);
	}

	@Override
	public Optional<Path> find(String s) {
		return Optional.ofNullable(this.internal.get(s));
	}

	@Override
	public Optional<Path> remove(String alias) throws UnsupportedEncodingException {
		return Optional.ofNullable(this.internal.remove(alias));
	}

	@Override
	public Map<String, Path> unmodifiable() {
		return Collections.unmodifiableMap(this.internal);
	}
}
