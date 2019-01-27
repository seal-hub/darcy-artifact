package de.thatsich.autosort.cli.alias;

import de.thatsich.autosort.cli.Persistence;
import de.thatsich.autosort.cli.Repository;
import de.thatsich.map.MapConverterService;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AliasRepository implements Repository<String, Path> {

	private final Persistence persistence;
	private final PathConverterService pathConverter;
	private final MapConverterService aliasConverter;

	private final Map<String, Path> cache;

	public AliasRepository(Persistence persistence, PathConverterService converter, MapConverterService aliasConverter) {
		this.persistence = persistence;
		this.pathConverter = converter;
		this.aliasConverter = aliasConverter;

		this.cache = new HashMap<>();
	}

	@Override
	public void persist(String alias, Path path) throws UnsupportedEncodingException {
		if (cache.isEmpty()) {
			final String encoded = this.persistence.retrieve();
			final Map<String, String> decoded = this.aliasConverter.decode(encoded);

			this.cache.putAll(this.pathConverter.toPaths(decoded));
		}

		this.cache.put(alias, path);
		this.persistCache();
	}

	@Override
	public Optional<Path> find(String alias) {
		return Optional.ofNullable(this.cache.get(alias));
	}

	@Override
	public Optional<Path> remove(String alias) throws UnsupportedEncodingException {
		final Optional<Path> removed = Optional.ofNullable(this.cache.remove(alias));
		if (removed.isPresent()) {
			this.persistCache();
		}

		return removed;
	}

	@Override
	public Map<String, Path> unmodifiable() {
		return Collections.unmodifiableMap(this.cache);
	}

	private void persistCache() throws UnsupportedEncodingException {
		final Map<String, String> stringed = this.pathConverter.toStrings(this.cache);
		final String encoded = this.aliasConverter.encode(stringed);
		this.persistence.persist(encoded);
	}
}
