package de.javaakademie.cb.api;

import java.util.Collection;
import java.util.Optional;

/**
 * ConferenceService.
 * 
 * @author Guido.Oelmann
 */
public interface ConferenceService<T> {

	public Collection<T> getAll();

	public Optional<?> get(String id);

	public void update(T item);

	public Optional<?> persist(T item);

	public void remove(T item);
}
