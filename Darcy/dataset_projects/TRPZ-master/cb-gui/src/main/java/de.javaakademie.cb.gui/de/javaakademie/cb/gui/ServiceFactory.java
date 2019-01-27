package de.javaakademie.cb.gui;

import java.util.Optional;
import java.util.ServiceLoader;

import de.javaakademie.cb.api.ConferenceService;
import de.javaakademie.cb.api.annotation.Sessions;
import de.javaakademie.cb.api.annotation.Speaker;
import de.javaakademie.cb.api.model.Session;

/**
 * ServiceFactory.
 * 
 * @author Guido.Oelmann
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ServiceFactory {

	private ServiceLoader<ConferenceService> services = ServiceLoader.load(ConferenceService.class);

	private ConferenceService<?> getServiceByAnnotation(Class annotation) throws ClassNotFoundException {
		Optional<ConferenceService> service = services.stream()
				.filter(provider -> provider.type().isAnnotationPresent(annotation)).map(ServiceLoader.Provider::get)
				.findFirst();
		services.reload();
		if (service.isPresent()) {
			return service.get();
		} else {
			throw new ClassNotFoundException(annotation.getName() + "Service not found.");
		}
	}

	public ConferenceService<de.javaakademie.cb.api.model.Speaker> getSpeakerService() throws ClassNotFoundException {
		return (ConferenceService<de.javaakademie.cb.api.model.Speaker>) getServiceByAnnotation(Speaker.class);
	}

	public ConferenceService<Session> getSessionService() throws ClassNotFoundException {
		return (ConferenceService<Session>) getServiceByAnnotation(Sessions.class);
	}

}
