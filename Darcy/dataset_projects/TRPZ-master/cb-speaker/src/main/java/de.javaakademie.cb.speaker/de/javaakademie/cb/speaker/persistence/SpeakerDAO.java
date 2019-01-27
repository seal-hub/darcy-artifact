package de.javaakademie.cb.speaker.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import de.javaakademie.cb.api.model.Speaker;

/**
 * SpeakerDAO.
 * 
 * @author Guido.Oelmann
 */
public class SpeakerDAO {

	private final HashMap<String, Speaker> speaker = new HashMap<>();

	public Collection<Speaker> getEntities() {
		return speaker.values();
	}

	public Optional<Speaker> persist(Speaker speaker) {
		String id = UUID.randomUUID().toString();
		speaker.setId(id);
		this.speaker.put(id, speaker);

		return Optional.of(speaker);
	}

	public void remove(Speaker speaker) {
		this.speaker.remove(speaker.getId());
	}

	public Speaker update(Speaker speaker) {
		if (!this.speaker.keySet().contains(speaker.getId())) {
			throw new IllegalArgumentException("Speaker not found " + speaker.getId());
		}
		return this.speaker.put(speaker.getId(), speaker);
	}

	public Optional<Speaker> get(String id) {
		if (this.speaker.containsKey(id)) {
			return Optional.of(this.speaker.get(id));
		}
		return Optional.empty();
	}

}