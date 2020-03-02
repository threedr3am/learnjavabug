package com.threedr3am.bug.feature;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Java字节码特征
 *
 * @author threedr3am
 */
public class JavaClassByteCode {

  public static void main(String[] args) throws IOException {
    testClass(Object.class.getResourceAsStream("String.class"));
    testClass(JavaClassByteCode.class.getResourceAsStream("JavaClassByteCode.class"));
  }

  private static void testClass(InputStream inputStream) throws IOException {
    byte[] bytes = new byte[inputStream.available()];
    inputStream.read(bytes);
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      stringBuilder.append(String.format("\\x%x ", bytes[i]));
    }
    printAndMatch(stringBuilder.toString());
  }

  private static void printAndMatch(String bytes) {
    System.out.println(bytes);
    System.out.println(bytes.replaceAll(" ", "").contains("\\xca\\xfe\\xba\\xbe"));
  }
}
