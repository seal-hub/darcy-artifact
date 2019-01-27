package de.javaakademie.cb.sessions.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import de.javaakademie.cb.api.model.Session;

/**
 * SessionDAO.
 * 
 * @author Guido.Oelmann
 */
public class SessionDAO {

	private final HashMap<String, Session> sessions = new HashMap<>();

	public Collection<Session> getEntities() {
		return sessions.values();
	}

	public Optional<Session> persist(Session session) {
		String id = UUID.randomUUID().toString();
		session.setId(id);
		this.sessions.put(id, session);

		return Optional.of(session);
	}

	public void remove(Session session) {
		this.sessions.remove(session.getId());
	}

	public Session update(Session sessions) {
		if (!this.sessions.keySet().contains(sessions.getId())) {
			throw new IllegalArgumentException("Session not found " + sessions.getId());
		}
		return this.sessions.put(sessions.getId(), sessions);
	}

	public Optional<Session> get(String id) {
		if (this.sessions.containsKey(id)) {
			return Optional.of(this.sessions.get(id));
		}
		return Optional.empty();
	}

}