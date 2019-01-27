package com.timberglund.poetry.content;

import java.util.stream.Stream;

public abstract class Poem {
  
  public abstract String getTitle();
  public abstract String getAuthor();
  public abstract Stream<String> getLines();
  
}
