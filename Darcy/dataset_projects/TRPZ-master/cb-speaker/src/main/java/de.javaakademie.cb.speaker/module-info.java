module de.javaakademie.cb.speaker {
	requires de.javaakademie.cb.api;
	provides de.javaakademie.cb.api.ConferenceService with de.javaakademie.cb.speaker.service.SpeakerService;
}