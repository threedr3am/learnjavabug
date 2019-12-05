package com.threedr3am.bug.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * 解析http协议，输出http请求体
 *
 * @author xuanyh
 */
public class HTTPServer {

  private static final int PORT = 8080;

  public static void main(String[] args) throws IOException {
    HttpServerProvider provider = HttpServerProvider.provider();
    HttpServer httpserver = provider.createHttpServer(new InetSocketAddress(PORT), 100);
    //监听端口8080,

    httpserver.createContext("/", new RestGetHandler());
    httpserver.setExecutor(null);
    httpserver.start();
    System.out.println("server started");
  }

  static class RestGetHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
      String requestMethod = he.getRequestMethod();
      System.out.println(requestMethod + " " + he.getRequestURI().getPath() + (
          StringUtils.isEmpty(he.getRequestURI().getRawQuery()) ? ""
              : "?" + he.getRequestURI().getRawQuery()) + " " + he.getProtocol());
      if (requestMethod.equalsIgnoreCase("GET")) {
        Headers responseHeaders = he.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");

        he.sendResponseHeaders(200, 0);
        // parse request
        OutputStream responseBody = he.getResponseBody();
        Headers requestHeaders = he.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        Iterator<String> iter = keySet.iterator();

        while (iter.hasNext()) {
          String key = iter.next();
          List values = requestHeaders.get(key);
          String s = key + ": " + values.toString();
          System.out.println(s);
        }
        System.out.println();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(he.getRequestBody()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        for (;(line = bufferedReader.readLine()) != null;) {
          stringBuilder.append(line);
        }
        System.out.println(stringBuilder.toString());

        // send response
        String response = "";
        responseBody.write(response.getBytes());
        responseBody.close();
      }
    }
  }

}
