package com.timberglund.poetry.content;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.stream.Stream;

public class CloudApp 
  extends Poem {
  
  public String getAuthor() {
    return "Tim Berglund";
  }
  
  public String getTitle() {
    return "I Wandered Lonely as a Cloud App";
  }

  public Stream<String> getLines() {
    String[] daffodils = {
      "I wandered lonely as a cloud app",
      "Late of Heroku purchases",
      "When all at once I saw a crowd,",
      "A host of microservices",
      "Beyond the build, beneath the stats",
      "My code in chunks and bits thereat",
      "",
      "Continuous was the app deployed",
      "In all its many-splendored parts",
      "They spread in Dockerfiles alloyed",
      "With CoreOS hosts’ very hearts",
      "Ten thousand saw I when I looked",
      "At Kubernetes’ dashboard hooks",
      "",
      "The Mesos cluster’s web display",
      "Showed every resource fully used",
      "Yet the goal at start of day",
      "Was just an app for scraping news",
      "I gazed—and gazed—but little thought",
      "That wealth to me this stack had brought",
      "",
      "Now oft, when at my screen I stare,",
      "Commenting on Hacker News,",
      "My only company, despair",
      "And the bill from EC2,",
      "Then filled my heart with sorrow is—",
      "The sorrow of the services."
    };
    
    return Arrays.asList(daffodils).stream();
  }
}
