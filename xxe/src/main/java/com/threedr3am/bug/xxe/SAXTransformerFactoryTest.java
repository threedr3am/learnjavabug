package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * SAXTransformerFactory方式
 * 这种方式会报错，回显不了，只能在请求中带出去
 *
 * @author xuanyh
 */
public class SAXTransformerFactoryTest {

  public static void main(String[] args) throws TransformerConfigurationException {
    SAXTransformerFactory sf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

    //todo 修复方式
//    sf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//    sf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.NO_FEEDBACK_SINGLE_LINE
        .getBytes());
    StreamSource source = new StreamSource(byteArrayInputStream);
    sf.newTransformerHandler(source);
  }

}
