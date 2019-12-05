package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * DOM方式 - DocumentBuilderFactory
 *
 * @author xuanyh
 */
public class DocumentBuilderFactory_DOMTest {

  public static void main(String[] args)
      throws IOException, ParserConfigurationException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    //todo 存在xxe漏洞
    DocumentBuilder builder = dbf.newDocumentBuilder();

    String FEATURE = null;
    FEATURE = "http://javax.xml.XMLConstants/feature/secure-processing";
    dbf.setFeature(FEATURE, true);
    FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    dbf.setFeature(FEATURE, true);
    FEATURE = "http://xml.org/sax/features/external-parameter-entities";
    dbf.setFeature(FEATURE, false);
    FEATURE = "http://xml.org/sax/features/external-general-entities";
    dbf.setFeature(FEATURE, false);
    FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    dbf.setFeature(FEATURE, false);
    dbf.setXIncludeAware(false);
    dbf.setExpandEntityReferences(false);

    //todo 修复需要把这行代码放在此处 DocumentBuilder builder = dbf.newDocumentBuilder();
//    DocumentBuilder builder = dbf.newDocumentBuilder();

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.FEEDBACK.getBytes());
    Document d = builder.parse(byteArrayInputStream);
    System.out.println(d.getDocumentElement().getTextContent());
  }
}
