package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author xuanyh
 */
public class TransformerFactoryTest {

  public static void main(String[] args) throws TransformerException {
    TransformerFactory tf = TransformerFactory.newInstance();

    //todo 修复方式
    tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.NO_FEEDBACK_SINGLE_LINE
        .getBytes());
    StreamSource source = new StreamSource(byteArrayInputStream);
    DOMResult domResult = new DOMResult();
    tf.newTransformer().transform(source, domResult);
  }

}
