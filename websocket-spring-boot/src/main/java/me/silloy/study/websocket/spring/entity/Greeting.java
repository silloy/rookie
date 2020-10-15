package me.silloy.study.websocket.spring.entity;

import lombok.Data;

/**
 * @author shaohuasu
 * @since 1.8
 */
@Data
public class Greeting {
  private String content;

  public Greeting() {
  }

  public Greeting(String content) {
    this.content = content;
  }
}
