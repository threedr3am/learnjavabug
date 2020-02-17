package com.threedr3am.bug.fastjson.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author xuanyh
 */
public class Test {

  public static void main(String[] args) {
    System.out.println("\n1.-------");
    A a = new A();
    a.setI(1);
    a.setName("threedr3am");
    String json = JSON.toJSONString(a);
    System.out.println(json);
    System.out.println(JSON.parse(json).getClass().getName());
    System.out.println(JSON.parseObject(json).getClass().getName());

    System.out.println("\n2.-------");
    String json2 = JSON.toJSONString(a, SerializerFeature.WriteClassName);
    System.out.println(json2);
    try {
      System.out.println(JSON.parse(json2).getClass().getName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      System.out.println(JSON.parseObject(json2).getClass().getName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    //todo 以上测试得出，默认不支持指定@type反序列化，即默认AutoTypeSupport=false

    System.out.println("*****************************************************************");
    ParserConfig.global.setAutoTypeSupport(true);

    String json3 = JSON.toJSONString(a, SerializerFeature.WriteClassName);
    System.out.println(json3);
    try {
      System.out.println(JSON.parse(json3).getClass().getName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      System.out.println(JSON.parseObject(json3).getClass().getName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    //todo 以上测试得出，当AutoTypeSupport=true时，JSON.parse返回具体类型，JSON.parseObject返回的是com.alibaba.fastjson.JSONObject
  }


  static class A {

    private int i;
    private String name;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
