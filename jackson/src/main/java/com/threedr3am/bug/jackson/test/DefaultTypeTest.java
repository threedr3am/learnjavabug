package com.threedr3am.bug.jackson.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * todo 总结
 * JAVA_LANG_OBJECT 只要有Object类型属性，就能反序列化为任何实例
 * OBJECT_AND_NON_CONCRETE 包含上述 JAVA_LANG_OBJECT 的特性，并且对于interface定义的属性，可以反序列化为任意实现类实例
 * NON_CONCRETE_AND_ARRAYS 包含上述 JAVA_LANG_OBJECT 和 OBJECT_AND_NON_CONCRETE 的特性，增加了数组支持
 * NON_FINAL 包含上述所有特性，除了final定义的属性不能反序列化，其他都可以
 *
 * enableDefaultTyping() 无参方法默认开启 OBJECT_AND_NON_CONCRETE
 *
 * @author threedr3am
 */
public class DefaultTypeTest {

  public static void main(String[] args) throws IOException {
//    testGlobalDefaultType();
//    testGlobalDefaultType2();
//    testGlobalDefaultType3();
    testGlobalDefaultType4();
  }

  /**
   * JAVA_LANG_OBJECT
   * JAVA_LANG_OBJECT 使特定类，例如Object类可以反序列化成指定的class对应的实例
   *
   * todo 看测试情况，开启了这种全局默认类型，只有存在Object才可以利用反序列化
   *
   * @throws IOException
   */
  private static void testGlobalDefaultType() throws IOException {
    A a = new A();
    a.setObject(new B());
    AA aa = new AA();
    a.setAa(aa);
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
    String json = mapper.writeValueAsString(a);
    // {"i":1,"object":["com.threedr3am.bug.jackson.test.DefaultTypeTest$B",{"i":2}]}
    System.out.println(json);

    json = "{\"i\":1,\"object\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}]}";
    a = mapper.readValue(json, A.class);
    System.out.println(a.getObject().getClass().getName());

    //todo 通过指定非Object class为其他class反序列化失败测试，可以知道，开启了这种全局默认类型，只有存在Object才可以利用反序列化
    json = "{\"i\":1,\"object\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$C\",{\"i\":2}],\"c\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$C\",{\"i\":0}]}";
    a = mapper.readValue(json, A.class);
    System.out.println(a.getObject().getClass().getName());
  }

  /**
   * OBJECT_AND_NON_CONCRETE
   *
   * @throws IOException
   */
  private static void testGlobalDefaultType2() throws IOException {
    AA aa = new AA();
    aa.setXx(2);
    A a = new A();
    a.setObject(new B());
    a.setAa(aa);
    ObjectMapper mapper = new ObjectMapper();
    //todo 会发现，如果改成 JAVA_LANG_OBJECT 就会报错，OBJECT_AND_NON_CONCRETE 控制接口类的反序列化
    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
    String json = mapper.writeValueAsString(a);
    // {"i":1,"object":["com.threedr3am.bug.jackson.test.DefaultTypeTest$B",{"i":2}],"aa":["com.threedr3am.bug.jackson.test.DefaultTypeTest$AA",{"xx":2}]}
    System.out.println(json);

    //todo 通过指定非Object class为其他class反序列化成功测试，可以知道，开启了这种全局默认类型，就算不存在Object也可以利用反序列化
    json = "{\"i\":1,\"object\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}],\"aa\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$AA\",{\"xx\":2}]}";
    a = mapper.readValue(json, A.class);
    System.out.println(a.getAa().getClass().getName());

    //todo 通过指定非Object class为其他class反序列化成功测试，可以知道，开启了这种全局默认类型，就算不存在Object也可以利用反序列化，但需要的是接口interface类型字段
    json = "{\"i\":1,\"object\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}],\"c\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$C\",{\"i\":2}]}";
    a = mapper.readValue(json, A.class);
    System.out.println(a.getAa().getClass().getName());
  }

  /**
   * NON_CONCRETE_AND_ARRAYS
   *
   * @throws IOException
   */
  private static void testGlobalDefaultType3() throws IOException {
    AA aa = new AA();
    aa.setXx(2);
    B[] bArray = new B[3];
    bArray[0] = new B();
    bArray[1] = new B();
    bArray[2] = new B();
    A a = new A();
    a.setObject(bArray);
    a.setAa(aa);
    ObjectMapper mapper = new ObjectMapper();
    //todo NON_CONCRETE_AND_ARRAYS 支持前面两种类型的数组类型
    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
    String json = mapper.writeValueAsString(a);
    // {"i":1,"object":["[Lcom.threedr3am.bug.jackson.test.DefaultTypeTest$B;",[{"i":2},{"i":2},{"i":2}]],"aa":["com.threedr3am.bug.jackson.test.DefaultTypeTest$AA",{"xx":2}]}
    System.out.println(json);

    //todo 理论上前面能支持的，这个都支持
    json = "{\"i\":1,\"object\":[\"[Lcom.threedr3am.bug.jackson.test.DefaultTypeTest$B;\",[{\"i\":2},{\"i\":2},{\"i\":2}]],\"aa\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$AA\",{\"xx\":2}]}";
    a = mapper.readValue(json, A.class);
    System.out.println(a.getAa().getClass().getName());

    //todo 通过指定非Object class为其他class反序列化成功测试，可以知道，开启了这种全局默认类型，就算不存在Object也可以利用反序列化，但需要的是接口interface类型字段
    json = "{\"i\":1,\"object\":[\"[Lcom.threedr3am.bug.jackson.test.DefaultTypeTest$B;\",[{\"i\":2},{\"i\":2},{\"i\":2}]],\"c\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$C\",{\"i\":2}]}";
    a = mapper.readValue(json, A.class);
    System.out.println(a.getAa().getClass().getName());
  }

  /**
   * NON_FINAL
   *
   * @throws IOException
   */
  private static void testGlobalDefaultType4() throws IOException {
    AA aa = new AA();
    aa.setXx(2);
    B[] bArray = new B[3];
    bArray[0] = new B();
    bArray[1] = new B();
    bArray[2] = new B();
    A a = new A();
    a.setObject(bArray);
    a.setAa(aa);
    ObjectMapper mapper = new ObjectMapper();
    //todo 支持前面所有特性，除final不能反序列化，其他都可以
    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    String json = mapper.writeValueAsString(a);
    // ["com.threedr3am.bug.jackson.test.DefaultTypeTest$A",{"i":1,"object":["[Lcom.threedr3am.bug.jackson.test.DefaultTypeTest$B;",[["com.threedr3am.bug.jackson.test.DefaultTypeTest$B",{"i":2}],["com.threedr3am.bug.jackson.test.DefaultTypeTest$B",{"i":2}],["com.threedr3am.bug.jackson.test.DefaultTypeTest$B",{"i":2}]]],"aa":["com.threedr3am.bug.jackson.test.DefaultTypeTest$AA",{"xx":2}]}]
    System.out.println(json);

    json = "[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$A\",{\"i\":1,\"object\":[\"[Lcom.threedr3am.bug.jackson.test.DefaultTypeTest$B;\",[[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}],[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}],[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}]]],\"aa\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$AA\",{\"xx\":2}]}]";
    a = mapper.readValue(json, A.class);
    System.out.println(a.getAa().getClass().getName());

    //todo 这个测试的通过，证明了前面几种Type，存在着Object、interface的限制，只有 NON_FINAL 毫无限制
    json = "[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$A\",{\"i\":1,\"object\":[\"[Lcom.threedr3am.bug.jackson.test.DefaultTypeTest$B;\",[[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}],[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}],[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$B\",{\"i\":2}]]],\"aa\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$AA\",{\"xx\":2}],\"c\":[\"com.threedr3am.bug.jackson.test.DefaultTypeTest$C\",{\"i\":2}]}]";
    a = mapper.readValue(json, A.class);
    System.out.println(a.getC().getClass().getName());
  }

  static class A {
    private int i = 1;
    private Object object;
    private InterfaceA aa;
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
