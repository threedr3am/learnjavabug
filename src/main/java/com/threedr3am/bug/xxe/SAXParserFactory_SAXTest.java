package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX方式 - SAXParserFactory
 *
 * @author xuanyh
 */
public class SAXParserFactory_SAXTest {

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();
    SAXHandel handel = new SAXHandel();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.FEEDBACK.getBytes());
    saxParser.parse(byteArrayInputStream, handel);
  }

}

class SAXHandel extends DefaultHandler {

  //遍历xml文件开始标签
  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
    System.out.println("sax解析开始");
  }

  //遍历xml文件结束标签
  @Override
  public void endDocument() throws SAXException {
    super.endDocument();
    System.out.println("sax解析结束");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    super.startElement(uri, localName, qName, attributes);
    if (qName.equals("student")) {
      System.out.println("============开始遍历student=============");
      //System.out.println(attributes.getValue("rollno"));
    } else if (!qName.equals("student") && !qName.equals("class")) {
      System.out.print("节点名称:" + qName + "----");
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement(uri, localName, qName);
    System.out.println("============结束遍历student=============");
  }

  @Override
  public void characters(char ch[], int start, int length)
      throws SAXException {
    super.characters(ch, start, length);
    String value = new String(ch, start, length).trim();
    if (!value.equals("")) {
      System.out.println(value);
    }
  }
}