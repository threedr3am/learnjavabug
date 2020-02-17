package com.threedr3am.bug.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by threedr3am on 2018/5/5.
 */
public class SerializeUtil {
    /**
     * 序列化
     *
     */
    public static byte[] serialize(Object o) throws Exception {
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
    public static <T>T deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        T o = (T) objectInputStream.readObject();
        objectInputStream.close();
        return o;
    }
}
