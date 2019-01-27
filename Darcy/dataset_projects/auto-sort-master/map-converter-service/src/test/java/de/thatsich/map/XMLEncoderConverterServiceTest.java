package de.thatsich.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

class XMLEncoderConverterServiceTest {

	@Test
	void both() throws UnsupportedEncodingException {
		final MapConverterService converterService = new XMLEncoderConverterService();
		final Map<String, String> decoded = new HashMap<>();
		decoded.put("anime", "D:\\Download\\Anime");
		decoded.put("love", "D:\\Love Love");

		Assertions.assertEquals(converterService.decode(converterService.encode(decoded)), decoded);
	}

	@Test
	void convertEmptyMap() throws UnsupportedEncodingException {
		final MapConverterService converterService = new XMLEncoderConverterService();
		final Map<String, String> empty = new HashMap<>();

		Assertions.assertEquals(converterService.encode(empty),
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<java version=\"9.0.1\" class=\"java.beans.XMLDecoder\">\n" +
				" <object class=\"java.util.HashMap\"/>\n" +
				"</java>\n");
	}
}
