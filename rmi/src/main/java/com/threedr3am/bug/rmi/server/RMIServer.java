package com.threedr3am.bug.rmi.server;

import com.threedr3am.bug.rmi.server.service.HelloService;
import com.threedr3am.bug.rmi.server.service.HelloServiceImpl;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author threedr3am
 */
public class RMIServer {

  public static void main(String[] args) {
    try {
      HelloService helloService = new HelloServiceImpl();
      Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
      registry.bind("hello", helloService);
      /**
       * todo all in java serialization，客户端能打服务端，服务端利用返回值也能打客户端，服务端不但能被动打客户端，也能主动bind打registry
       *
       * 打registry（可以选择bind打，也能debug打）：
       *
       * rebind:151, RegistryImpl_Stub (sun.rmi.registry)
       * main:19, RMIServer (com.threedr3am.bug.rmi.server)
       *
       * bind:63, RegistryImpl_Stub (sun.rmi.registry)
       * main:19, RMIServer (com.threedr3am.bug.rmi.server)
       *
       * 打客户端：
       *
       * writeObject:343, ObjectOutputStream (java.io)
       * dispatch:101, DGCImpl_Skel (sun.rmi.transport)
       * oldDispatch:468, UnicastServerRef (sun.rmi.server)
       * dispatch:300, UnicastServerRef (sun.rmi.server)
       * run:200, Transport$1 (sun.rmi.transport)
       * run:197, Transport$1 (sun.rmi.transport)
       * doPrivileged:-1, AccessController (java.security)
       * serviceCall:196, Transport (sun.rmi.transport)
       * handleMessages:573, TCPTransport (sun.rmi.transport.tcp)
       * run0:834, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
       * lambda$run$0:688, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
       * run:-1, 592642572 (sun.rmi.transport.tcp.TCPTransport$ConnectionHandler$$Lambda$3)
       * doPrivileged:-1, AccessController (java.security)
       * run:687, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
       * runWorker:1149, ThreadPoolExecutor (java.util.concurrent)
       * run:624, ThreadPoolExecutor$Worker (java.util.concurrent)
       * run:748, Thread (java.lang)
       *
       */
    } catch (RemoteException | AlreadyBoundException e) {
      e.printStackTrace();
    }
  }
}
