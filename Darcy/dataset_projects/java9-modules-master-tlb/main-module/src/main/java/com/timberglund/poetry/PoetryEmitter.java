package com.timberglund.poetry;

import com.timberglund.logger.Logger;
import com.timberglund.poetry.content.Poem;
import java.util.List;

public class PoetryEmitter {

  public Poem createContentSource(String poemName)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    return (Poem)Class.forName("com.timberglund.poetry.content." + poemName).newInstance();
  }

  public void emit(Logger logger, String poemName) 
    throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Poem poem = createContentSource(poemName);
    logger.log(poem.getLines());
  }

  public static void main(String args[])
    throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    PoetryEmitter pe = new PoetryEmitter();
    pe.emit(new Logger(), args[0]);
  }
}
