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
      System.out.println(helloService.sayHello());
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (NotBoundException e) {
      e.printStackTrace();
    }
  }
}
