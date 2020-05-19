package com.threedr3am.bug.tomcat.sync.session.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * code from ysoserial
 */
public class Converter {
    public static byte[] toBytes(Object[] objs) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (Object obj : objs) {
            treatObject(dos, obj);
        }
        dos.close();
        return baos.toByteArray();
    }

    public static void treatObject(DataOutputStream dos, Object obj)
        throws IOException {
        if (obj instanceof Byte) {
            dos.writeByte((Byte) obj);
        } else if (obj instanceof Short) {
            dos.writeShort((Short) obj);
        } else if (obj instanceof Integer) {
            dos.writeInt((Integer) obj);
        } else if (obj instanceof Long) {
            dos.writeLong((Long) obj);
        } else if (obj instanceof String) {
            dos.writeUTF((String) obj);
        } else {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(ba);
            oos.writeObject(obj);
            oos.close();
            dos.write(ba.toByteArray(), 4, ba.size() - 4); // 4 = skip the header
        }
    }
}
