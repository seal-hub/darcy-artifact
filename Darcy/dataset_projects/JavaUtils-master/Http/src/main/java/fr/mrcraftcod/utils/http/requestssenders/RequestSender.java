package fr.mrcraftcod.utils.http.requestssenders;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.mrcraftcod.utils.http.RequestHandler;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 03/12/2016.
 *
 * @author Thomas Couchoud
 * @since 2016-12-03
 */
public interface RequestSender<T>
{
	HttpResponse<T> getRequestResult() throws UnirestException;

	default RequestHandler<T> getRequestHandler() throws UnirestException
	{
		return new RequestHandler<>(this);
	}
}
