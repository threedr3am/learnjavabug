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
    } catch (AlreadyBoundException | RemoteException e) {
      e.printStackTrace();
    }
  }
}
