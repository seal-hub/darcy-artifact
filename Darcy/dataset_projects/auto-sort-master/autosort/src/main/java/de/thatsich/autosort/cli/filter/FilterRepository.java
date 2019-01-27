package de.thatsich.autosort.cli.filter;

import de.thatsich.autosort.cli.Persistence;
import de.thatsich.autosort.cli.Repository;
import de.thatsich.map.MapConverterService;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FilterRepository implements Repository<String, String> {

	private final Persistence persistence;
	private final MapConverterService aliasConverter;

	private final Map<String, String> cache;

	public FilterRepository(Persistence persistence, MapConverterService aliasConverter) {
		this.persistence = persistence;
		this.aliasConverter = aliasConverter;

		this.cache = new HashMap<>();
	}

	@Override
	public void persist(String key, String value) throws UnsupportedEncodingException {
		if (cache.isEmpty()) {
			final String encoded = this.persistence.retrieve();
			final Map<String, String> decoded = this.aliasConverter.decode(encoded);

			this.cache.putAll(decoded);
		}

		this.cache.put(key, value);
		this.persistCache();
	}

	@Override
	public Optional<String> find(String alias) {
		return Optional.ofNullable(this.cache.get(alias));
	}

	@Override
	public Optional<String> remove(String alias) throws UnsupportedEncodingException {
		final Optional<String> removed = Optional.ofNullable(this.cache.remove(alias));
		if (removed.isPresent()) {
			this.persistCache();
		}

		return removed;
	}

	@Override
	public Map<String, String> unmodifiable() {
		return Collections.unmodifiableMap(this.cache);
	}

	private void persistCache() throws UnsupportedEncodingException {
		final String encoded = this.aliasConverter.encode(this.cache);
		this.persistence.persist(encoded);
	}
}
