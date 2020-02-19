package com.threedr3am.learn.serialize;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author xuanyh
 */
public class MyHessian2Input extends Hessian2Input {

  /**
   * Creates a new Hessian input stream, initialized with an underlying input stream.
   *
   * @param is the underlying input stream.
   */
  public MyHessian2Input(InputStream is) {
    super(is);
  }

  @Override
  public Object readObject(Class cl) throws IOException {
    return super.readObject(cl);
  }

  @Override
  public Object readObject(Class expectedClass, Class<?>... expectedTypes) throws IOException {
    return super.readObject(expectedClass, expectedTypes);
  }

  @Override
  public Object readObject() throws IOException {
    return super.readObject();
  }

  @Override
  public Object readObject(List<Class<?>> expectedTypes) throws IOException {
    return super.readObject(expectedTypes);
  }

  void checkClassDef() {
    if (_classDefs.isEmpty())
      return;
    for (Object c : _classDefs) {
      Field[] fields = c.getClass().getDeclaredFields();
      System.out.println();
    }
  }
}
