package de.thatsich.autosort.filter;

import de.thatsich.autosort.cli.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NonPersistentStringRepository implements Repository<String, String> {
	private final Map<String, String> internal = new HashMap<>();

	@Override
	public void persist(String s, String path) {
		this.internal.put(s, path);
	}

	@Override
	public Optional<String> find(String s) {
		return Optional.ofNullable(this.internal.get(s));
	}

	@Override
	public Optional<String> remove(String alias) {
		return Optional.ofNullable(this.internal.remove(alias));
	}

	@Override
	public Map<String, String> unmodifiable() {
		return Collections.unmodifiableMap(this.internal);
	}
}
