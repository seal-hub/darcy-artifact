package de.javaakademie.cb.sessions.service;

import java.util.Collection;
import java.util.Optional;

import de.javaakademie.cb.api.ConferenceService;
import de.javaakademie.cb.api.annotation.Sessions;
import de.javaakademie.cb.api.model.Session;
import de.javaakademie.cb.sessions.persistence.SessionDAO;

/**
 * SessionService.
 * 
 * @author Guido.Oelmann
 */
@Sessions
public class SessionService implements ConferenceService<Session> {

	protected SessionDAO sessionDAO;

	public SessionService() {
		this.sessionDAO = new SessionDAO();
	}

	public Collection<Session> getAll() {
		return sessionDAO.getEntities();
	}

	public Optional<Session> get(String id) {
		return sessionDAO.get(id);
	}

	public void update(Session session) {
		sessionDAO.update(session);
	}

	public Optional<Session> persist(Session session) {
		return sessionDAO.persist(session);
	}

	public void remove(Session session) {
		sessionDAO.remove(session);
	}

}
