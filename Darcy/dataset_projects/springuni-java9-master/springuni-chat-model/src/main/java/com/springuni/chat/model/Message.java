package com.springuni.chat.model;

import java.time.LocalDateTime;

/**
 * Created by lcsontos on 6/12/17.
 */
public class Message {

  private String sender;
  private String recipient;

  private String text;
  private LocalDateTime time;

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
  }

}
