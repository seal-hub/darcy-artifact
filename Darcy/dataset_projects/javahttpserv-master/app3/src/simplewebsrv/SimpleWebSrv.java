package simplewebsrv;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import simplewebsrv.EchoGetHandler;
import simplewebsrv.EchoHeaderHandler;
import simplewebsrv.EchoPostHandler;
import simplewebsrv.RootHandler;

class SimpleWebSrv {
  public static void main(String[] args) {

    try {
      int port = 9000;
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
      System.out.println("server started at " + port);
      server.createContext("/", new RootHandler());
      server.createContext("/echoHeader", new EchoHeaderHandler());
      server.createContext("/echoGet", new EchoGetHandler());
      server.createContext("/echoPost", new EchoPostHandler());
      server.setExecutor(null);
      server.start();
    } catch (IOException ex) {
      System.out.println(ex);
    }
  }
}
