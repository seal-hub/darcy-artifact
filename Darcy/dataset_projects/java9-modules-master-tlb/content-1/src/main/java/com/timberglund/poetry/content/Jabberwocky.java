package com.timberglund.poetry.content;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.stream.Stream;

public class Jabberwocky extends Poem {

  public String getAuthor() {
    return "Lewis Carol";
  }
  
  public String getTitle() {
    return "Jabberwocky";
  }

  public Stream<String> getLines() {
    String[] lines = {
      "'Beware the Jabberwock, my son!",
      "  The jaws that bite, the claws that catch!",
      "Beware the Jubjub bird, and shun",
      "  The frumious Bandersnatch!'",
      "He took his vorpal sword in hand:",
      "  Long time the manxome foe he sought --",
      "So rested he by the Tumtum tree,",
      "  And stood awhile in thought.",
      "And, as in uffish thought he stood,",
      "  The Jabberwock, with eyes of flame,",
      "Came whiffling through the tulgey wood,",
      "  And burbled as it came!",
      "One, two! One, two! And through and through",
      "  The vorpal blade went snicker-snack!",
      "He left it dead, and with its head",
      "  He went galumphing back.",
      "'And, has thou slain the Jabberwock?",
      "  Come to my arms, my beamish boy!",
      "O frabjous day! Callooh! Callay!'",
      "  He chortled in his joy."
    };
    
    return Arrays.asList(lines).stream();
  }
}
