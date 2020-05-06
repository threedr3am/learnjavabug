package com.threedr3am.bug.feature;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.UnsafeSerializer;
import com.caucho.hessian.io.WriteReplaceSerializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Hessian序列化数据特征
 *
 * 4d 74 00 ... 7a
 *
 * @author threedr3am
 */
public class HessianSerialization implements Serializable {

  public static void main(String[] args) throws IOException {
    printAndMatch(object1());
    printAndMatch(object1_());
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
    System.out.println(stringBuilder.toString().replaceAll(" ", "").contains("\\x4d\\x74\\x0"));
  }

  private static byte[] object1() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
    sf.setAllowNonSerializable(true);
    out.setSerializerFactory(sf);
    out.writeString("test");
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object1_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    out.writeString("test");
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object1_2() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    out.writeString("threedr3am");
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object2() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
    sf.setAllowNonSerializable(true);
    out.setSerializerFactory(sf);
    out.writeObject(new A());
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object2_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    out.writeObject(new A());
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object3() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
    sf.setAllowNonSerializable(true);
    out.setSerializerFactory(sf);
    out.writeObject(new B(new A()));
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object3_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    out.writeObject(new B(new A()));
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object4() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
    sf.setAllowNonSerializable(true);
    out.setSerializerFactory(sf);
    out.writeObject(new HessianSerialization());
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object4_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    out.writeObject(new HessianSerialization());
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object5() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
    sf.setAllowNonSerializable(true);
    out.setSerializerFactory(sf);
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    map.put("foo", "foo");
    out.writeObject(map);
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object5_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    map.put("foo", "foo");
    out.writeObject(map);
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object6() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
    sf.setAllowNonSerializable(true);
    out.setSerializerFactory(sf);
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    out.writeObject(map);
    out.close();
    return bos.toByteArray();
  }

  private static byte[] object6_() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    AbstractHessianOutput out = new HessianOutput(bos);
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    out.writeObject(map);
    out.close();
    return bos.toByteArray();
  }

  static class A implements Serializable {}
  static class B implements Serializable {
    A a;

    public B(A a) {
      this.a = a;
    }
  }

  public static class NoWriteReplaceSerializerFactory extends SerializerFactory {

    /**
     * {@inheritDoc}
     *
     * @see com.caucho.hessian.io.SerializerFactory#getObjectSerializer(java.lang.Class)
     */
    @Override
    public Serializer getObjectSerializer ( Class<?> cl ) throws HessianProtocolException {
      return super.getObjectSerializer(cl);
    }


    /**
     * {@inheritDoc}
     *
     * @see com.caucho.hessian.io.SerializerFactory#getSerializer(java.lang.Class)
     */
    @Override
    public Serializer getSerializer ( Class cl ) throws HessianProtocolException {
      Serializer serializer = super.getSerializer(cl);

      if ( serializer instanceof WriteReplaceSerializer) {
        return UnsafeSerializer.create(cl);
      }
      return serializer;
    }

  }


}
