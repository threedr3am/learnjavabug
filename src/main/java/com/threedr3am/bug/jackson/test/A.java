package com.threedr3am.bug.jackson.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * @author xuanyh
 */
public class A {
  private B b;

  public A() {
  }

  public A(B b) {
    this.b = b;
  }

  public B getB() {
    return b;
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

//    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
//    String json = mapper.writeValueAsString(new A(new D()));
//    System.out.println(json);

//    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
//    String json = mapper.writeValueAsString(new A(new D()));
//    System.out.println(json);

//    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
//    String json = mapper.writeValueAsString(new A(new D()));
//    System.out.println(json);

    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    String json = mapper.writeValueAsString(new A(new D()));
    System.out.println(json);

    A a = mapper.readValue(json, A.class);
    a.b.x();
  }
}
class C implements B {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void x() {
    System.out.println("C.x()");
  }
}
class D implements B {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void x() {
    System.out.println("D.x()");
  }
}
interface B {
  void x();
}
