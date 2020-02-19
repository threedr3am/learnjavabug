package com.threedr3am.learn.server.boot;

import java.io.Serializable;

/**
 * @author xuanyh
 */
public class A implements Serializable {

  String name;

  public A() {
    System.out.println("A被实例化了");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object obj) {
    System.out.println("A.equals");
    return super.equals(obj);
  }

  @Override
  public String toString() {
    System.out.println("A.toString");
    return super.toString();
  }
}
