package it4u.site.java;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.URI;

/**
 * HttpClient API测试
 */
public class HttpClientTest {

    public static void main(String[] args) {
        // jdk9 中使用HttpClient替换原有的HttpUrlCollection
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://www.baidu.com")).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandler.asString());
            System.out.println(response.statusCode());
            System.out.println(response.version().name());
            System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
