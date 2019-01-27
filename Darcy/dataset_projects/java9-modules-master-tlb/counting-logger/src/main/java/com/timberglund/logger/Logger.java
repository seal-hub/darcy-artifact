package com.timberglund.logger;

import java.util.stream.Stream;

public class Logger {
  private static long lineCount = 0;
    
  public void log(String s) {
    System.out.printf("%8d %s\n", ++lineCount, s);
  }

  public void log(Stream<String> stream) {
    stream.forEach(line -> log(line));
  }
}
