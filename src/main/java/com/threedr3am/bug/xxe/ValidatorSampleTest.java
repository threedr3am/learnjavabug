package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * @author xuanyh
 */
public class ValidatorSampleTest {

  public static void main(String[] args)
      throws SAXException, IOException {
    SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
    Schema schema = factory.newSchema();
    Validator validator = schema.newValidator();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.NO_FEEDBACK_SINGLE_LINE
        .getBytes());

    //todo 修复方式
//    validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//    validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

    StreamSource source = new StreamSource(byteArrayInputStream);
    validator.validate(source);

  }
}
