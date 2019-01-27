package br.com.phtcosta.media.movie.nfo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.phtcosta.media.movie.exception.MoviePersistenceException;
import br.com.phtcosta.media.movie.model.Movie;
import br.com.phtcosta.media.movie.nfo.NfoParser;

public class NfoParserTest {
	private static NfoParser parser;

	@BeforeClass
	public static void runOnceBeforeClass() {
		parser = new NfoParser();
	}

	@Test
	public void testReadNfoFile() throws MoviePersistenceException {
		File arquivo = getResourceAsFile("/nfo/movie.nfo");
		Movie movie = parser.read(arquivo);
		assertNotNull("Movie shouldn't be null",movie);		
		assertEquals("Matrix", movie.getTitle());		
	}
	
	@Test(expected=MoviePersistenceException.class)
	public void testReadNfoFileFail() throws MoviePersistenceException {
		File arquivo = new File("/nfo/inexistent.nfo");
		Movie movie = parser.read(arquivo);
		assertNull("Movie should be null",movie);				
	}



	private static File getResourceAsFile(String resourcePath) {
		try {
			InputStream in = NfoParserTest.class.getResourceAsStream(resourcePath);
			if (in == null) {
				return null;
			}

			File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
			tempFile.deleteOnExit();

			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
			return tempFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
