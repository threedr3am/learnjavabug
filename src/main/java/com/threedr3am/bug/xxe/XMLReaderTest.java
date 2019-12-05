package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author xuanyh
 */
public class XMLReaderTest {

  public static void main(String[] args) throws SAXException, IOException {
    XMLReader reader = XMLReaderFactory.createXMLReader();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.NO_FEEDBACK_SINGLE_LINE
        .getBytes());

    //todo 修复方式
    reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
    reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

    reader.parse(new InputSource(byteArrayInputStream));
  }
}
