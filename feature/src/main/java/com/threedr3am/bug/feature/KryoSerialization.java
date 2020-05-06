package com.threedr3am.bug.feature;

import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.UnsafeSerializer;
import com.caucho.hessian.io.WriteReplaceSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
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
public class KryoSerialization implements Serializable {

  public static void main(String[] args) throws IOException {
    printAndMatch(object1());
    printAndMatch(object1_2());
    printAndMatch(object2());
    printAndMatch(object3());
    printAndMatch(object4());
    printAndMatch(object5());
    printAndMatch(object6());
  }

  private static void printAndMatch(byte[] bytes) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      stringBuilder.append(String.format("\\x%x ", bytes[i]));
    }
    System.out.println(stringBuilder.toString());
    System.out.println(stringBuilder.toString().replaceAll(" ", "").contains("\\x1\\x0"));
  }

  private static Kryo makeKryo() {
    return new com.esotericsoftware.kryo.Kryo();
  }

  private static byte[] writeObject(Object o) {
    com.esotericsoftware.kryo.Kryo k = makeKryo();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try ( Output output = new Output(bos) ) {
      k.writeClassAndObject(output, o);
    }
    return bos.toByteArray();
  }

  private static byte[] object1() throws IOException {
    return writeObject("test");
  }

  private static byte[] object1_2() throws IOException {
    return writeObject("threedr3am");
  }

  private static byte[] object2() throws IOException {
    return writeObject(new A());
  }

  private static byte[] object3() throws IOException {
    return writeObject(new B(new A()));
  }

  private static byte[] object4() throws IOException {
    return writeObject(new KryoSerialization());
  }

  private static byte[] object5() throws IOException {
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    map.put("foo", "foo");
    return writeObject(map);
  }

  private static byte[] object6() throws IOException {
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    return writeObject(map);
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
     * @see SerializerFactory#getObjectSerializer(Class)
     */
    @Override
    public Serializer getObjectSerializer ( Class<?> cl ) throws HessianProtocolException {
      return super.getObjectSerializer(cl);
    }


    /**
     * {@inheritDoc}
     *
     * @see SerializerFactory#getSerializer(Class)
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
