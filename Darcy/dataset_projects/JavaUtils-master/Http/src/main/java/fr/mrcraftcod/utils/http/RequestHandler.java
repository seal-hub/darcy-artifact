package fr.mrcraftcod.utils.http;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.mrcraftcod.utils.http.requestssenders.RequestSender;
import java.util.Optional;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 03/12/2016.
 *
 * @author Thomas Couchoud
 * @since 2016-12-03
 */
public class RequestHandler<T>
{
	private final RequestSender<T> requestSender;
	private final HttpResponse<T> result;

	public RequestHandler(RequestSender<T> requestSender) throws UnirestException
	{
		this.requestSender = requestSender;
		this.result = requestSender.getRequestResult();
	}

	public HttpResponse<T> getResult()
	{
		return this.result;
	}

	public T getRequestResult()
	{
		return this.getResult().getBody();
	}

	public Headers getHeaders()
	{
		return this.getResult().getHeaders();
	}

	public int getStatus()
	{
		return this.getResult().getStatus();
	}

	public String getStatusText()
	{
		return this.getResult().getStatusText();
	}

	public Optional<String> getSetCookies()
	{
		if(this.getHeaders().containsKey("Set-Cookie"))
			return Optional.of(this.getHeaders().get("Set-Cookie").get(0));
		return Optional.empty();
	}

	public long getLength()
	{
		long length = 0;
		if(this.getHeaders().containsKey("content-length"))
			for(String value : this.getHeaders().get("content-length"))
				length += Long.parseLong(value);
		else if(this.getHeaders().containsKey("Content-Length"))
			for(String value : this.getHeaders().get("Content-Length"))
				length += Long.parseLong(value);
		return length;
	}
}
