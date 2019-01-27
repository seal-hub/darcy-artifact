package fr.mrcraftcod.utils.http.requestssenders.get;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 03/12/2016.
 *
 * @author Thomas Couchoud
 * @since 2016-12-03
 */
public class BinaryGetRequestSender extends GetRequestSender<InputStream>
{
	public BinaryGetRequestSender(String url) throws URISyntaxException, MalformedURLException
	{
		super(url);
	}
	
	public BinaryGetRequestSender(URL url) throws URISyntaxException
	{
		super(url, null);
	}
	
	public BinaryGetRequestSender(URL url, Map<String, String> headers) throws URISyntaxException
	{
		super(url, headers, null);
	}
	
	public BinaryGetRequestSender(URL url, Map<String, String> headers, Map<String, String> params) throws URISyntaxException
	{
		super(url, headers, params);
	}

	@Override
	public HttpResponse<InputStream> getRequestResult() throws UnirestException
	{
		return this.getRequest().asBinary();
	}
}
