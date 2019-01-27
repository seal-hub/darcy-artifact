package de.thatsich.map;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface MapConverterService {
	String encode(Map<String, String> mapping) throws UnsupportedEncodingException;

	Map<String, String> decode(String encoded) throws UnsupportedEncodingException;
}
