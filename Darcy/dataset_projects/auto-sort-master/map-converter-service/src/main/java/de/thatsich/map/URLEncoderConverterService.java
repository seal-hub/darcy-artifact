package de.thatsich.map;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class URLEncoderConverterService implements MapConverterService {

	private static final String ENCODING = "UTF-8";

	public String encode(Map<String, String> mapping) throws UnsupportedEncodingException {
		final StringBuilder stringBuilder = new StringBuilder();

		for (Map.Entry<String, String> entry : mapping.entrySet()) {
			final String key = entry.getKey();
			final String value = entry.getValue();

			if (stringBuilder.length() > 0) {
				stringBuilder.append("&");
			}

			stringBuilder.append((key != null ? URLEncoder.encode(key, ENCODING) : ""));
			stringBuilder.append("=");
			stringBuilder.append(value != null ? URLEncoder.encode(value, ENCODING) : "");
		}

		return stringBuilder.toString();
	}

	public Map<String, String> decode(String encoded) throws UnsupportedEncodingException {
		final Map<String, String> map = new HashMap<>();
		final String[] nameValuePairs = encoded.split("&");

		for (String nameValuePair : nameValuePairs) {
			final String[] nameValue = nameValuePair.split("=");
			final String decodedKey = URLDecoder.decode(nameValue[0], ENCODING);
			final String decodedValue = nameValue.length > 1 ? URLDecoder.decode(nameValue[1], ENCODING) : "";
			map.put(decodedKey, decodedValue);
		}

		return map;
	}
}
