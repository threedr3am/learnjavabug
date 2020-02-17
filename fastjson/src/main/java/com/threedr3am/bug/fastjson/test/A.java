package com.threedr3am.bug.fastjson.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

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

  public static void main(String[] args) {
    ParserConfig.global.setAutoTypeSupport(true);

    String json3 = JSON.toJSONString(new A(new D()), SerializerFeature.WriteClassName);
    System.out.println(json3);

    JSON.parse(json3);

  }
}

class C implements B {

  @Override
  public void x() {

  }
}

class D implements B {

  @Override
  public void x() {

  }
}

interface B {
  void x();
}
