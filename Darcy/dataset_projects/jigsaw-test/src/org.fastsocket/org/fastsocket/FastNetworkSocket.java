package org.fastsocket;

import com.socket.NetworkSocket;

public class FastNetworkSocket extends NetworkSocket {
  FastNetworkSocket() {
    System.out.println("Brand new Fast Socket!");
  }
  public void close() { }
}
