package com.threedr3am.bug.jackson.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * todo 总结
 * 仅 @JsonTypeInfo(use = Id.CLASS) 和 @JsonTypeInfo(use = Id.MINIMAL_CLASS) 可指定class反序列化
 * 但必须类型一致，或为接口interface的实现类
 *
 * @author threedr3am
 */
public class JsonTypeInfoTest {

  public static void main(String[] args) throws IOException {
//    testJsonTypeInfo();
//    testJsonTypeInfo2();
    testJsonTypeInfo3();
//    testJsonTypeInfo4();
//    testJsonTypeInfo5();
  }

  /**
   * 不使用 @JsonTypeInfo 或使用 @JsonTypeInfo(use = Id.NONE)
   *
   * @throws IOException
   */
  private static void testJsonTypeInfo() throws IOException {
    A a = new A();
    a.setObject(new B());
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(a);
    // {"i":1,"object":{"i":2},"aa":null}
    System.out.println(json);

    json = "{\"i\":1,\"object\":{\"i\":2},\"aa\":null}";
    a = mapper.readValue(json, A.class);
    //todo 返回的类型为java.util.LinkedHashMap，默认不使用 @JsonTypeInfo（等同 @JsonTypeInfo(use = Id.NONE)） 注解的反序列化实现
    System.out.println(a.getObject().getClass().getName());
  }

  /**
   * @JsonTypeInfo(use = Id.CLASS)
   *
   * @throws IOException
   */
  private static void testJsonTypeInfo2() throws IOException {
    A2 a = new A2();
    a.setObject(new B());
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(a);
    // {"i":1,"object":{"@class":"com.threedr3am.bug.jackson.test.JsonTypeInfoTest$B","i":2},"aa":null}
    System.out.println(json);

    json = "{\"i\":1,\"c\":{\"@class\":\"com.threedr3am.bug.jackson.test.JsonTypeInfoTest$C\",\"i\":2}}";
    a = mapper.readValue(json, A2.class);
    System.out.println(a.getC().getClass().getName());

    json = "{\"i\":1,\"aa\":{\"@class\":\"com.threedr3am.bug.jackson.test.JsonTypeInfoTest$AA\",\"xx\":2}}";
    a = mapper.readValue(json, A2.class);
    System.out.println(a.getAa().getClass().getName());

    json = "{\"i\":1,\"c\":{\"@class\":\"com.threedr3am.bug.jackson.test.JsonTypeInfoTest$D\",\"i\":2}}";
    a = mapper.readValue(json, A2.class);
    //todo 反序列化时，指定D类反序列化为C类，最终报错，因此，使用 @JsonTypeInfo(use = Id.CLASS) 注解的反序列化能利用，但必须和定义类型一致
    System.out.println(a.getC().getClass().getName());
  }

  /**
   * @JsonTypeInfo(use = Id.MINIMAL_CLASS)
   *
   * @throws IOException
   */
  private static void testJsonTypeInfo3() throws IOException {
    A3 a = new A3();
    a.setObject(new B());
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(a);
    // {"i":1,"object":{"@c":"com.threedr3am.bug.jackson.test.JsonTypeInfoTest$B","i":2},"aa":null}
    System.out.println(json);

    json = "{\"i\":1,\"c\":{\"@c\":\"com.threedr3am.bug.jackson.test.JsonTypeInfoTest$C\",\"i\":2}}";
    a = mapper.readValue(json, A3.class);
    System.out.println(a.getC().getClass().getName());

    json = "{\"i\":1,\"aa\":{\"@c\":\"com.threedr3am.bug.jackson.test.JsonTypeInfoTest$AA\",\"xx\":2}}";
    a = mapper.readValue(json, A3.class);
    System.out.println(a.getAa().getClass().getName());

    json = "{\"i\":1,\"c\":{\"@c\":\"com.threedr3am.bug.jackson.test.JsonTypeInfoTest$D\",\"i\":2}}";
    a = mapper.readValue(json, A3.class);
    //todo 反序列化时，指定D类反序列化为C类，最终报错，因此，使用 @JsonTypeInfo(use = Id.MINIMAL_CLASS) 注解的反序列化能利用，但必须和定义类型一致
    System.out.println(a.getC().getClass().getName());
  }

  /**
   * @JsonTypeInfo(use = Id.NAME)
   *
   * @throws IOException
   */
  private static void testJsonTypeInfo4() throws IOException {
    A4 a = new A4();
    a.setObject(new B());
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(a);
    // {"i":1,"object":{"@type":"JsonTypeInfoTest$B","i":2},"aa":null}
    System.out.println(json);

    json = "{\"i\":1,\"object\":{\"@type\":\"JsonTypeInfoTest$B\",\"i\":2},\"aa\":null}";
    a = mapper.readValue(json, A4.class);
    //todo 这边反序列化失败，证明 @JsonTypeInfo(use = Id.NAME) 并不能反序列化任意类
    System.out.println(a.getObject().getClass().getName());
  }

  /**
   * @JsonTypeInfo(use = Id.CUSTOM)
   *
   * @throws IOException
   */
  private static void testJsonTypeInfo5() throws IOException {
    A5 a = new A5();
    a.setObject(new B());
    ObjectMapper mapper = new ObjectMapper();
    //todo 直接序列化失败，需要自定义转换器
    String json = mapper.writeValueAsString(a);
    // {"i":1,"object":{"@type":"JsonTypeInfoTest$B","i":2},"aa":null}
    System.out.println(json);
  }

  static class A {
    private int i = 1;
    @JsonTypeInfo(use = Id.NONE)
    private Object object;
    private InterfaceA aa;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public Object getObject() {
      return object;
    }

    public void setObject(Object object) {
      this.object = object;
    }

    public InterfaceA getAa() {
      return aa;
    }

    public void setAa(InterfaceA aa) {
      this.aa = aa;
    }
  }

  static class A2 {
    private int i = 1;
    @JsonTypeInfo(use = Id.CLASS)
    private Object object;
    @JsonTypeInfo(use = Id.CLASS)
    private InterfaceA aa;
    @JsonTypeInfo(use = Id.CLASS)
    private C c;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public Object getObject() {
      return object;
    }

    public void setObject(Object object) {
      this.object = object;
    }

    public InterfaceA getAa() {
      return aa;
    }

    public void setAa(InterfaceA aa) {
      this.aa = aa;
    }

    public C getC() {
      return c;
    }

    public void setC(C c) {
      this.c = c;
    }
  }

  static class A3 {
    private int i = 1;
    @JsonTypeInfo(use = Id.MINIMAL_CLASS)
    private Object object;
    @JsonTypeInfo(use = Id.MINIMAL_CLASS)
    private InterfaceA aa;
    @JsonTypeInfo(use = Id.MINIMAL_CLASS)
    private C c;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public Object getObject() {
      return object;
    }

    public void setObject(Object object) {
      this.object = object;
    }

    public InterfaceA getAa() {
      return aa;
    }

    public void setAa(InterfaceA aa) {
      this.aa = aa;
    }

    public C getC() {
      return c;
    }

    public void setC(C c) {
      this.c = c;
    }
  }

  static class A4 {
    private int i = 1;
    @JsonTypeInfo(use = Id.NAME)
    private Object object;
    private InterfaceA aa;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public Object getObject() {
      return object;
    }

    public void setObject(Object object) {
      this.object = object;
    }

    public InterfaceA getAa() {
      return aa;
    }

    public void setAa(InterfaceA aa) {
      this.aa = aa;
    }
  }

  static class A5 {
    private int i = 1;
    @JsonTypeInfo(use = Id.CUSTOM)
    private Object object;
    private InterfaceA aa;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public Object getObject() {
      return object;
    }

    public void setObject(Object object) {
      this.object = object;
    }

    public InterfaceA getAa() {
      return aa;
    }

    public void setAa(InterfaceA aa) {
      this.aa = aa;
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

    public C() {
      System.out.println("call C()");
    }

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }
  }

  static class D {
    private int i = 3;

    public D() {
      System.out.println("call D()");
    }

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }
  }

  static class AA implements InterfaceA {
    int xx;

    public int getXx() {
      return xx;
    }

    public void setXx(int xx) {
      this.xx = xx;
    }
  }

  interface InterfaceA {

  }
}
