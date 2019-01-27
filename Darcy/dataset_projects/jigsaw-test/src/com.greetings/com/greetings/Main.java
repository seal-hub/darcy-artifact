package com.greetings;

import com.socket.NetworkSocket;

public class Main{
  public static void main(String[] args) throws Exception {
    tryit();
    if(args.length>0){
      while(true){
        Thread.sleep(Long.parseLong(args[0]));
        tryit();
      }
    }
  }

  public static void tryit(){
    try{
      NetworkSocket s = NetworkSocket.open();
      System.out.println(s.getClass());
    } catch(Exception e) {
      System.err.println("Anyone loaded");
    }
  }
}
