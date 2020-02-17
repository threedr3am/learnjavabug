package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * JDOM方式 - SAXBuilder
 *
 * @author xuanyh
 */
public class SAXBuilder_JDOMTest {

  public static void main(String[] args) throws JDOMException, IOException {
    //todo 存在xxe漏洞
    SAXBuilder saxBuilder = new SAXBuilder();

    //todo 修复方式1
//    SAXBuilder saxBuilder = new SAXBuilder(true);

    //todo 修复方式2
//    SAXBuilder saxBuilder = new SAXBuilder();
//    saxBuilder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//    saxBuilder.setFeature("http://xml.org/sax/features/external-general-entities", false);
//    saxBuilder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
//    saxBuilder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.FEEDBACK.getBytes());
    Document document = saxBuilder.build(byteArrayInputStream);
    Element element = document.getRootElement();
    List<Content> contents = element.getContent();
    for (Content content : contents) {
      System.out.println(content.getValue());
    }
  }
}
