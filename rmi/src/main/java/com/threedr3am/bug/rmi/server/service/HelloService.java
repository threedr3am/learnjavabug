package com.threedr3am.bug.rmi.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author threedr3am
 */
public interface HelloService extends Remote {

  String sayHello() throws RemoteException;
}
