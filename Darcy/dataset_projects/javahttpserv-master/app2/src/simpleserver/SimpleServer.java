package simpleserver;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

class Main {
  public static void main(String[] args) {
    try {
      String payload = "Hello World for Java Server!\n";

      HttpServer server = HttpServer.create(new InetSocketAddress(4250), 0);
      HttpContext context = server.createContext("/helloworld");
      context.setHandler(
          (he) -> {
            he.sendResponseHeaders(200, payload.getBytes().length);
            final OutputStream output = he.getResponseBody();
            output.write(payload.getBytes());
            output.flush();
            he.close();
          });

      server.start();
    } catch (IOException ex) {
      System.out.println(ex);
    }
  }
}
