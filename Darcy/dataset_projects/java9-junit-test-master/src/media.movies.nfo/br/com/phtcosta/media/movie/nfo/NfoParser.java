package br.com.phtcosta.media.movie.nfo;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.com.phtcosta.media.movie.exception.MoviePersistenceException;
import br.com.phtcosta.media.movie.model.Movie;

public class NfoParser {

	public void save(Movie movie, File out) throws MoviePersistenceException{
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(movie, out);
		} catch (JAXBException e) {
			throw new MoviePersistenceException("Error writing XML file", e);
		}
	}
	
	public Movie read(File in) throws MoviePersistenceException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (Movie) jaxbUnmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			throw new MoviePersistenceException("Error reading XML file", e);
		}
	}
}
