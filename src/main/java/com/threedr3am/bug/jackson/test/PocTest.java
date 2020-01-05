package com.threedr3am.bug.jackson.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * @author threedr3am
 */
public class PocTest {

  public static void main(String[] args) throws IOException {
    testGlobalDefaultType();
  }

  private static void testGlobalDefaultType() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();
    // {"i":1,"object":["com.threedr3am.bug.jackson.test.DefaultTypeTest$B",{"i":2}]}

    String json = "{\"obj\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":1}],\"i\":1}";
    A a = mapper.readValue(json, A.class);
    System.out.println(a.getObj().getClass().getName());
  }

  static class A {
    private int i = 1;
    private Object obj;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public Object getObj() {
      return obj;
    }

    public void setObj(Object obj) {
      this.obj = obj;
    }
  }

  static class B {
    private int i = 2;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }
  }

  static class C {
    private int i = 3;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }
  }
}
