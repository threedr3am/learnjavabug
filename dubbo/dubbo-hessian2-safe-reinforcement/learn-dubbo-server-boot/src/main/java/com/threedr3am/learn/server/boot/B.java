package com.threedr3am.learn.server.boot;

import java.io.Serializable;

/**
 * @author xuanyh
 */
public class B implements Serializable {

  String name;

  public B() {
    System.out.println("B被实例化了");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
