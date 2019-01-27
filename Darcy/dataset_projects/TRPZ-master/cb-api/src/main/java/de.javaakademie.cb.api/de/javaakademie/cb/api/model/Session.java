package de.javaakademie.cb.api.model;

import java.util.List;

/**
 * Session.
 * 
 * @author Guido.Oelmann
 */
public class Session {

	private String id;
	private String title;
	private String language;
	private String talkType;
	private String track;
	private String summary;
	private List<Object> speakers;

	public Session() {
	}

	public Session(String title, String language, String talkType, String track, String summary,
			List<Object> speakers) {
		this.title = title;
		this.language = language;
		this.talkType = talkType;
		this.track = track;
		this.summary = summary;
		this.speakers = speakers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTalkType() {
		return talkType;
	}

	public void setTalkType(String talkType) {
		this.talkType = talkType;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<Object> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(List<Object> speakers) {
		this.speakers = speakers;
	}

	public String toString() {
		return "Session{" + "id='" + id + '\'' + ", title='" + title + '\'' + ", language='" + language + '\''
				+ ", talkType='" + talkType + '\'' + ", track='" + track + '\'' + ", summary='" + summary + '\''
				+ ", speakers=" + speakers + '}';
	}
}