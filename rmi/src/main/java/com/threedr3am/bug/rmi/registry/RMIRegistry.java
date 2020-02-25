package com.threedr3am.bug.rmi.registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author threedr3am
 */
public class RMIRegistry {

  public static void main(String[] args) throws RemoteException {
    Registry registry = LocateRegistry.createRegistry(1099);
    /**
     * todo 看完客户端lookup打registry，也能看到lookup内部执行了readObject，那么，相应的在registry打断点，writeObject恶意类就可以打客户端
     *
     * writeObject:343, ObjectOutputStream (java.io)
     * dispatch:118, RegistryImpl_Skel (sun.rmi.registry)
     * oldDispatch:468, UnicastServerRef (sun.rmi.server)
     * dispatch:300, UnicastServerRef (sun.rmi.server)
     * run:200, Transport$1 (sun.rmi.transport)
     * run:197, Transport$1 (sun.rmi.transport)
     * doPrivileged:-1, AccessController (java.security)
     * serviceCall:196, Transport (sun.rmi.transport)
     * handleMessages:573, TCPTransport (sun.rmi.transport.tcp)
     * run0:834, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
     * lambda$run$0:688, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
     * run:-1, 568523217 (sun.rmi.transport.tcp.TCPTransport$ConnectionHandler$$Lambda$5)
     * doPrivileged:-1, AccessController (java.security)
     * run:687, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
     * runWorker:1149, ThreadPoolExecutor (java.util.concurrent)
     * run:624, ThreadPoolExecutor$Worker (java.util.concurrent)
     * run:748, Thread (java.lang)
     *
     * todo 因为服务端bind它的stub到registry的时候，也会有序列化数据通讯，所以registry也能被动的打服务端
     *
     * writeObject:348, ObjectOutputStream (java.io)
     * dirty:105, DGCImpl_Stub (sun.rmi.transport)
     * makeDirtyCall:382, DGCClient$EndpointEntry (sun.rmi.transport)
     * registerRefs:324, DGCClient$EndpointEntry (sun.rmi.transport)
     * registerRefs:160, DGCClient (sun.rmi.transport)
     * registerRefs:102, ConnectionInputStream (sun.rmi.transport)
     * releaseInputStream:157, StreamRemoteCall (sun.rmi.transport)
     * dispatch:80, RegistryImpl_Skel (sun.rmi.registry)
     * oldDispatch:468, UnicastServerRef (sun.rmi.server)
     * dispatch:300, UnicastServerRef (sun.rmi.server)
     * run:200, Transport$1 (sun.rmi.transport)
     * run:197, Transport$1 (sun.rmi.transport)
     * doPrivileged:-1, AccessController (java.security)
     * serviceCall:196, Transport (sun.rmi.transport)
     * handleMessages:573, TCPTransport (sun.rmi.transport.tcp)
     * run0:834, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
     * lambda$run$0:688, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
     * run:-1, 568523217 (sun.rmi.transport.tcp.TCPTransport$ConnectionHandler$$Lambda$5)
     * doPrivileged:-1, AccessController (java.security)
     * run:687, TCPTransport$ConnectionHandler (sun.rmi.transport.tcp)
     * runWorker:1149, ThreadPoolExecutor (java.util.concurrent)
     * run:624, ThreadPoolExecutor$Worker (java.util.concurrent)
     * run:748, Thread (java.lang)
     */
    while (true);
  }
}
