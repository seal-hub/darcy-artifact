package de.javaakademie.cb.speaker.service;

import java.util.Collection;
import java.util.Optional;

import de.javaakademie.cb.api.ConferenceService;
import de.javaakademie.cb.api.model.Speaker;
import de.javaakademie.cb.speaker.persistence.SpeakerDAO;

/**
 * SpeakerService.
 * 
 * @author Guido.Oelmann
 */
@de.javaakademie.cb.api.annotation.Speaker
public class SpeakerService implements ConferenceService<Speaker> {

	protected SpeakerDAO speakerDAO;

	public SpeakerService() {
		this.speakerDAO = new SpeakerDAO();
	}

	@Override
	public Collection<Speaker> getAll() {
		return speakerDAO.getEntities();
	}

	@Override
	public Optional<Speaker> get(String id) {
		return speakerDAO.get(id);
	}

	@Override
	public void update(Speaker speaker) {
		speakerDAO.update(speaker);
	}

	@Override
	public Optional<Speaker> persist(Speaker speaker) {
		Speaker sp = speaker;
		return speakerDAO.persist(sp);
	}

	@Override
	public void remove(Speaker speaker) {
		speakerDAO.remove(speaker);
	}
}
