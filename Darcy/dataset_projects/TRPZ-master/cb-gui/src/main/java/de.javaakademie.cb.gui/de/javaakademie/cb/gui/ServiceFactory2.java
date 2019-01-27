package de.javaakademie.cb.gui;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import de.javaakademie.cb.api.ConferenceService;
import de.javaakademie.cb.api.annotation.Sessions;
import de.javaakademie.cb.api.model.Session;
import de.javaakademie.cb.api.model.Speaker;

/**
 * Alternative ServiceFactory.
 * 
 * @author Guido.Oelmann
 */
@SuppressWarnings("unchecked")
public class ServiceFactory2 {

	private Map<Class<?>, ConferenceService<?>> services = new HashMap<>();

	public ServiceFactory2() {
		ServiceLoader.load(ConferenceService.class).stream().forEach(provider -> {
			Class<? extends Annotation> annotation = provider.type().getAnnotations()[0].annotationType();
			services.put(annotation, provider.get());
		});
	}

	private ConferenceService<?> getService(Class<?> annotation) throws ClassNotFoundException {
		ConferenceService<?> service = services.get(annotation);
		if (service != null) {
			return service;
		} else {
			throw new ClassNotFoundException(annotation.getName() + "Service not found.");
		}
	}

	public ConferenceService<Speaker> getSpeakerService() throws ClassNotFoundException {
		return (ConferenceService<Speaker>) getService(de.javaakademie.cb.api.annotation.Speaker.class);
	}

	public ConferenceService<Session> getSessionService() throws ClassNotFoundException {
		return (ConferenceService<Session>) getService(Sessions.class);
	}

}
