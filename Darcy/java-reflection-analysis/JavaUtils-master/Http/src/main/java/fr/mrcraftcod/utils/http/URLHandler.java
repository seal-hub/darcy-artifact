package fr.mrcraftcod.utils.http;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class URLHandler
{
	private static final int TIMEOUT = 30000;
	private static final String USER_AGENT_KEY = "User-Agent";
	private static final String USER_AGENT = "MrCraftCod/Utils";
	private static final String CHARSET_TYPE_KEY = "charset";
	private static final String CHARSET_TYPE = "utf-8";
	private static final String CONTENT_TYPE_KEY = "Content-Type";
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	private static final String LANGUAGE_TYPE_KEY = "Accept-Language";
	private static final String LANGUAGE_TYPE = Locale.getDefault().toString() + ";q=1,en;q=0.8";

	private static HttpClient makeClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException
	{
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build(), NoopHostnameVerifier.INSTANCE);
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setDefaultRequestConfig(globalConfig).setConnectionTimeToLive(TIMEOUT, TimeUnit.MILLISECONDS).setUserAgent(USER_AGENT).build();
	}

	public static GetRequest getRequest(URL url, Map<String, String> headers, Map<String, String> params) throws URISyntaxException
	{
		Unirest.clearDefaultHeaders();
		Unirest.setDefaultHeader(USER_AGENT_KEY, USER_AGENT);
		URIBuilder uriBuilder = new URIBuilder(new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef()));
		if(params != null)
			for(String key : params.keySet())
				uriBuilder.addParameter(key, params.get(key));
		return Unirest.get(uriBuilder.build().toString()).headers(headers).header(LANGUAGE_TYPE_KEY, LANGUAGE_TYPE).header(CONTENT_TYPE_KEY, CONTENT_TYPE).header(CHARSET_TYPE_KEY, CHARSET_TYPE).header(USER_AGENT_KEY, USER_AGENT);
	}

	private static GetRequest headRequest(URL url, Map<String, String> headers, Map<String, String> params) throws URISyntaxException
	{
		Unirest.clearDefaultHeaders();
		Unirest.setDefaultHeader(USER_AGENT_KEY, USER_AGENT);
		URIBuilder uriBuilder = new URIBuilder(new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef()));
		if(params != null)
			for(String key : params.keySet())
				uriBuilder.addParameter(key, params.get(key));
		return Unirest.head(uriBuilder.build().toString()).headers(headers).header(LANGUAGE_TYPE_KEY, LANGUAGE_TYPE).header(CONTENT_TYPE_KEY, CONTENT_TYPE).header(CHARSET_TYPE_KEY, CHARSET_TYPE).header(USER_AGENT_KEY, USER_AGENT);
	}

	public static void exit()
	{
		try
		{
			Unirest.shutdown();
		}
		catch(IOException ignored)
		{
		}
	}

	public static RequestBodyEntity postRequest(URL url, HashMap<String, String> headers, HashMap<String, String> params, String body) throws URISyntaxException
	{
		Unirest.clearDefaultHeaders();
		Unirest.setDefaultHeader(USER_AGENT_KEY, USER_AGENT);
		URIBuilder uriBuilder = new URIBuilder(new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef()));
		if(params != null)
			for(String key : params.keySet())
				uriBuilder.addParameter(key, params.get(key));
		return Unirest.post(uriBuilder.build().toString()).headers(headers).header(LANGUAGE_TYPE_KEY, LANGUAGE_TYPE).header(CONTENT_TYPE_KEY, CONTENT_TYPE).header(CHARSET_TYPE_KEY, CHARSET_TYPE).header(USER_AGENT_KEY, USER_AGENT).body(body);
	}

	static
	{
		try
		{
			Unirest.setHttpClient(makeClient());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
