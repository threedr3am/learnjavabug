package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 * SchemaFactory方式
 *
 * @author xuanyh
 */
public class SchemaFactoryTest {

  public static void main(String[] args)
      throws SAXException {
    SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

    //todo 修复方式
//    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.NO_FEEDBACK_SINGLE_LINE
        .getBytes());
    StreamSource source = new StreamSource(byteArrayInputStream);
    Schema schema = factory.newSchema(source);
  }
}
