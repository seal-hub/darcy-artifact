package com.timberglund.poetry.content;

import java.util.Arrays;
import java.util.stream.Stream;


public class Dream extends Poem {

  public String getAuthor() {
    return "Lord Byron";
  }

  public String getTitle() {
    return "The Dream";
  }

  public Stream<String> getLines() {
    String[] dream = {
      "I SAW two beings in the hues of youth",
      "Standing upon a hill, a gentle hill,",
      "Green and of mild declivity, the last",
      "As ’twere the cape of a long ridge of such,",
      "Save that there was no sea to lave its base,",
      "But a most living landscape, and the wave",
      "Of woods and cornfields, and the abodes of men",
      "Scatter’d at intervals, and wreathing smoke",
      "Arising from such rustic roofs; the hill",
      "Was crown’d with a peculiar diadem",
      "Of trees, i circular array, so fix’d,",
      "Not by the sport of nature, but of man:",
      "These two, a maiden and a youth, were there",
      "Gazing—the one on all that was beneath",
      "Fair as herself—but the boy gazed on her;",
      "And both were young, and one was beautiful:",
      "And both were young—yet not alike in youth.",
      "As the sweet moon on the horizon’s verge,",
      "The maid was on the eve of womanhood;",
      "The boy had fewer summers, but his heart",
      "Had far outgrown his years, and to his eye",
      "There was but one beloved face on earth,",
      "And that was shining on him; he had look’d",
      "Upon it till it could not pass away;",
      "He had no breath, no being, but in hers;",
      "She was his voice; he did not speak to her,",
      "But trembled on her words; she was his sight,",
      "For his eye follow’d hers, and saw with hers,",
      "Which colour’d all his objects:—he had ceased",
      "To live within himself; she was his life,",
      "The ocean to the river of his thoughts,",
      "Which terminated all: upon a tone,",
      "A touch of hers, his blood would ebb and flow,",
      "And his cheek change tempestuously—his heart",
      "Unknowing of its cause of agony.",
      "But she in these fond feelings had no share:",
      "Her sighs were not for him; to her he was",
      "Even as a brother—but no more; ’twas much,",
      "For brotherless she was, save in the name",
      "Her infant friendship had bestow’d on him;",
      "Herself the solitary scion left",
      "Of a time-honour’d race.—It was a name",
      "Which pleased him, and yet pleased him not—and why?",
      "Time taught him a deep answer—when she loved",
      "Another; even now she loved another,",
      "And on the summit of that hill she stood",
      "Looking afar if yet her lover’s steed",
      "Kept pace with her expectancy and flew."
    };
    
    return Arrays.asList(dream).stream();
  }
}
