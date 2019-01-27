module de.javaakademie.cb.sessions {
	requires de.javaakademie.cb.api;
	provides de.javaakademie.cb.api.ConferenceService with de.javaakademie.cb.sessions.service.SessionService;
}