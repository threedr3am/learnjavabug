package com.threedr3am.bug.xxe;

import java.io.ByteArrayInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author xuanyh
 */
public class UnmarshallerTest {

  public static void main(String[] args) throws JAXBException {
    Class tClass = A.class;
    JAXBContext context = JAXBContext.newInstance(tClass);
    Unmarshaller um = context.createUnmarshaller();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Payloads.NO_FEEDBACK_SINGLE_LINE
        .getBytes());
    Object o = um.unmarshal(byteArrayInputStream);
    tClass.cast(o);
  }

  static class A {
    public String root;

    public String getRoot() {
      return root;
    }

    public void setRoot(String root) {
      this.root = root;
    }
  }
}
