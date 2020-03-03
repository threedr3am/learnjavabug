package com.threedr3am.bug.common.server;

import com.google.common.io.Files;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import java.io.BufferedReader;
import java.io.File;
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

  public static String filePath;
  public static int PORT = 8080;
  public static String contentType;

  public static void main(String[] args) throws IOException {
    run(args);
  }

  public static void run(String[] args) {
    int port = PORT;
    String context = "/";
    String clazz = "Calc.class";
    if (args != null && args.length > 0) {
      port = Integer.parseInt(args[0]);
      context = args[1];
      clazz = args[2];
    }
    HttpServerProvider provider = HttpServerProvider.provider();
    HttpServer httpserver = null;
    try {
      httpserver = provider.createHttpServer(new InetSocketAddress(port), 100);
    } catch (IOException e) {
      e.printStackTrace();
    }
    //监听端口8080,

    httpserver.createContext(context, new RestGetHandler(clazz));
    httpserver.setExecutor(null);
    httpserver.start();
    System.out.println("server started");
  }

  static class RestGetHandler implements HttpHandler {

    private String clazz;

    public RestGetHandler(String clazz) {
      this.clazz = clazz;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
      String requestMethod = he.getRequestMethod();
      System.out.println(requestMethod + " " + he.getRequestURI().getPath() + (
          StringUtils.isEmpty(he.getRequestURI().getRawQuery()) ? ""
              : "?" + he.getRequestURI().getRawQuery()) + " " + he.getProtocol());
      if (requestMethod.equalsIgnoreCase("GET")) {
        Headers responseHeaders = he.getResponseHeaders();
        responseHeaders.set("Content-Type", contentType == null ? "application/json" : contentType);

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

        byte[] bytes = Files.toByteArray(new File(filePath == null ? HTTPServer.class.getClassLoader().getResource(clazz).getPath() : filePath));
        // send response
        responseBody.write(bytes);
        responseBody.close();
      }
    }
  }

}
