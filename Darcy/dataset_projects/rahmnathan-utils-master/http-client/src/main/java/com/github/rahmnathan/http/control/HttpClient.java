package com.github.rahmnathan.http.control;

import com.github.rahmnathan.http.data.HttpRequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class.getName());

    private HttpClient(){
        // No need to instantiate this
    }

    public static String getResponseAsString(String url, HttpRequestMethod requestMethod, String body, Map<String, String> headers) {
        HttpURLConnection connection = getUrlConnection(url);

        if (connection != null) {
            if(requestMethod != null){
                try {
                    connection.setRequestMethod(requestMethod.name());
                } catch (ProtocolException e){
                    logger.error("Failure setting request method", e);
                }
            }

            if(headers != null){
                headers.forEach(connection::addRequestProperty);
            }

            if(body != null){
                connection.setDoOutput(true);
                try(BufferedWriter br = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))){
                    br.write(body);
                } catch (IOException e){
                    logger.error("Failure writing to url", e);
                }
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                reader.lines().forEachOrdered(response::append);

                return response.toString();
            } catch (IOException e) {
                logger.error("Failure reading response", e);
            } finally {
                connection.disconnect();
            }
        }

        return "";
    }

    public static byte[] getResponseAsBytes(String urlString){
        HttpURLConnection connection = getUrlConnection(urlString);

        if(connection != null) {
            try (InputStream inputStream = connection.getInputStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int i;

                while (-1 != (i = inputStream.read(buffer))) {
                    baos.write(i);
                }

                return baos.toByteArray();
            } catch (Exception e) {
                logger.error("Error getting response from url", e);
            } finally {
                connection.disconnect();
            }
        }

        return new byte[0];
    }

    private static HttpURLConnection getUrlConnection(String urlString){
        try {
            return  (HttpURLConnection) new URL(urlString).openConnection();
        } catch (IOException e) {
            logger.error("Error opening http connection", e);
            return null;
        }
    }
}