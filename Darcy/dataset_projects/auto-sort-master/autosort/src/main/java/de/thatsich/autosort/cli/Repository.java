package de.thatsich.autosort.cli;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

public interface Repository<K, V> {
	void persist(K key, V value) throws UnsupportedEncodingException;

	Optional<V> find(K key);

	Optional<V> remove(K alias) throws UnsupportedEncodingException;

	Map<K, V> unmodifiable();
}
