package com.timberglund.logger;

import java.util.stream.Stream;

public class Logger {
  public void log(String s) {
    System.out.println(s);
  }
  
  public void log(Stream<String> stream) {
    stream.forEach(line -> log(line));
  }
}
