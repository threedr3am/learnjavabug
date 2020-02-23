package com.threedr3am.bug.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

/**
 * @author threedr3am
 */
public class HttpUtil {

  private static SSLConnectionSocketFactory sslsf;

  static {
    try {
      SSLContext sslContext = new SSLContextBuilder()
          .loadTrustMaterial(null, new TrustStrategy() {
            // 信任所有
            public boolean isTrusted(X509Certificate[] chain,
                String authType)
                throws CertificateException {
              return true;
            }
          }).build();
      sslsf = new SSLConnectionSocketFactory(
          sslContext);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String get(String url) {
    HttpGet httpGet = new HttpGet(url);
    try {
      HttpClientBuilder httpClientBuilder = HttpClients
          .custom()
//          .setProxy(new HttpHost("127.0.0.1", 8080))
          .disableRedirectHandling()
//          .disableCookieManagement()
          ;
      if (url.startsWith("https://")) {
        httpClientBuilder.setSSLSocketFactory(sslsf);
      }

      CloseableHttpClient httpClient = null;
      CloseableHttpResponse response = null;
      try {
        httpClient = httpClientBuilder.build();
        response = httpClient.execute(httpGet);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
          StringBuilder stringBuilder = new StringBuilder();
          String line;
          while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
          }
          return stringBuilder.toString();
        }
      } finally {
        response.close();
        httpClient.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String post(String url, String payload) throws UnsupportedEncodingException {
    HttpPost httpPost = new HttpPost(url);
//    httpPost.addHeader("Cookie", "rememberMe=" + Base64.getEncoder().encodeToString(data));
    HttpEntity httpEntity = new StringEntity(payload, "application/x-www-form-urlencoded", "utf-8");
    httpPost.setEntity(httpEntity);
    try {
      HttpClientBuilder httpClientBuilder = HttpClients
          .custom()
//          .setProxy(new HttpHost("127.0.0.1", 8080))
          .disableRedirectHandling()
//          .disableCookieManagement()
          ;
      if (url.startsWith("https://")) {
        httpClientBuilder.setSSLSocketFactory(sslsf);
      }

      CloseableHttpClient httpClient = null;
      CloseableHttpResponse response = null;
      try {
        httpClient = httpClientBuilder.build();
        response = httpClient.execute(httpPost);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
          StringBuilder stringBuilder = new StringBuilder();
          String line;
          while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
          }
          return stringBuilder.toString();
        }
      } finally {
        response.close();
        httpClient.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
