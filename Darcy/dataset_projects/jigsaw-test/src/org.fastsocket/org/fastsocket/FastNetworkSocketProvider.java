package org.fastsocket;

import com.socket.spi.NetworkSocketProvider;
import com.socket.NetworkSocket;

public class FastNetworkSocketProvider extends NetworkSocketProvider {

  public FastNetworkSocketProvider(){

  }

  @Override
  public NetworkSocket openNetworkSocket(){
    return new FastNetworkSocket();
  }
}
