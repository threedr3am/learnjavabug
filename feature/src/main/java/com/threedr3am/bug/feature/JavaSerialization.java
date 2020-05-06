package com.threedr3am.bug.feature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Java原生序列化数据特征
 *
 * @author threedr3am
 */
public class JavaSerialization implements Serializable {

  public static void main(String[] args) throws IOException {
    printAndMatch(object1());
    printAndMatch(object1_());
    printAndMatch(object1_2());
    printAndMatch(object2());
    printAndMatch(object2_());
    printAndMatch(object3());
    printAndMatch(object3_());
    printAndMatch(object4());
    printAndMatch(object4_());
    printAndMatch(object5());
    printAndMatch(object5_());
    printAndMatch(object6());
    printAndMatch(object6_());
  }

  private static void printAndMatch(byte[] bytes) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      stringBuilder.append(String.format("\\x%x ", bytes[i]));
    }
    System.out.println(stringBuilder.toString());
    System.out.println(stringBuilder.toString().replaceAll(" ", "").contains("\\xac\\xed\\x0\\x5"));
  }

  private static byte[] object1() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeBytes("test");
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object1_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeBytes("test");
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object1_2() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeBytes("threedr3am");
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object2() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(new JavaSerialization.A());
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object2_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(new JavaSerialization.A());
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object3() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(new JavaSerialization.B(new JavaSerialization.A()));
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object3_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(new JavaSerialization.B(new JavaSerialization.A()));
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object4() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(new JavaSerialization());
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object4_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(new JavaSerialization());
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object5() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    map.put("foo", "foo");
    out.writeObject(map);
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object5_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    map.put("foo", "foo");
    out.writeObject(map);
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object6() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    out.writeObject(map);
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object6_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    out.writeObject(map);
    out.close();
    return bos.toByteArray();
  }

  static class A implements Serializable {}
  static class B implements Serializable {
    JavaSerialization.A a;

    public B(JavaSerialization.A a) {
      this.a = a;
    }
  }
}
