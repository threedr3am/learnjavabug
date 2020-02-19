package com.threedr3am.learn.server.boot.serialize;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author threedr3am
 */
public class MyHessian2Input extends Hessian2Input {

  private static final Set<String> blackList = new HashSet<>();

  static {
//    blackList.add("com.threedr3am.learn.server.boot.A");
  }

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
    checkClassDef();
    return super.readObject(cl);
  }

  @Override
  public Object readObject(Class expectedClass, Class<?>... expectedTypes) throws IOException {
    checkClassDef();
    return super.readObject(expectedClass, expectedTypes);
  }

  @Override
  public Object readObject() throws IOException {
    checkClassDef();
    return super.readObject();
  }

  @Override
  public Object readObject(List<Class<?>> expectedTypes) throws IOException {
    checkClassDef();
    return super.readObject(expectedTypes);
  }

  void checkClassDef() {
    if (_classDefs == null || _classDefs.isEmpty())
      return;
    for (Object c : _classDefs) {
      Field[] fields = c.getClass().getDeclaredFields();
      if (fields.length == 2) {
        fields[0].setAccessible(true);
        try {
          String type = (String) fields[0].get(c);
          if (blackList.contains(type))
            _classDefs = new ArrayList();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
