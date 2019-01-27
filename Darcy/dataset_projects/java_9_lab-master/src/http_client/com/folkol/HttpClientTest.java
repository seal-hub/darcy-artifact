package http_client;

import java.net.URI;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

public class HttpClientTest
{
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(new URI("https://www.google.se/"))
                                         .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandler.asString());

        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
