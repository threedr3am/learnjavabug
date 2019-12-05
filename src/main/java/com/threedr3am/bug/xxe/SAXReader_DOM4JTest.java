package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

/**
 * DOM4J方式 - SAXReader
 *
 * @author xuanyh
 */
public class SAXReader_DOM4JTest {

  public static void main(String[] args) throws DocumentException, SAXException {
    SAXReader saxReader = new SAXReader();

    //todo 修复方式
//    saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//    saxReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
//    saxReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
//    saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        Payloads.FEEDBACK.getBytes());
    Document document = saxReader.read(byteArrayInputStream);
    Element element = document.getRootElement();
    System.out.println(element.getText());
  }
}
