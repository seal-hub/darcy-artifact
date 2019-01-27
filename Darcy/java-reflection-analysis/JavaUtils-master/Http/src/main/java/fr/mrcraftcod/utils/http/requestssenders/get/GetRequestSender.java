package fr.mrcraftcod.utils.http.requestssenders.get;

import com.mashape.unirest.request.GetRequest;
import fr.mrcraftcod.utils.http.URLHandler;
import fr.mrcraftcod.utils.http.requestssenders.RequestSender;
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
public abstract class GetRequestSender<T> implements RequestSender<T>
{
	private final GetRequest request;
	
	public GetRequestSender(String url) throws URISyntaxException, MalformedURLException
	{
		this(new URL(url));
	}
	
	public GetRequestSender(URL url) throws URISyntaxException
	{
		this(url, null);
	}
	
	public GetRequestSender(URL url, Map<String, String> headers) throws URISyntaxException
	{
		this(url, headers, null);
	}
	
	public GetRequestSender(URL url, Map<String, String> headers, Map<String, String> params) throws URISyntaxException
	{
		request = URLHandler.getRequest(url, headers, params);
	}

	public GetRequest getRequest()
	{
		return this.request;
	}
}
