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
    while (true);
  }
}
