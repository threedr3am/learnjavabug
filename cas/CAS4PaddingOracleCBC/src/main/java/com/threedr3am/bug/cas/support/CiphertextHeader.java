package com.threedr3am.bug.cas.support;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author threedr3am
 */
public class CiphertextHeader {
    private final byte[] nonce;
    private String keyName;
    private int length;

    public CiphertextHeader(byte[] nonce) {
        this(nonce, (String)null);
    }

    public CiphertextHeader(byte[] nonce, String keyName) {
        this.nonce = nonce;
        this.length = 8 + nonce.length;
        if (keyName != null) {
            this.length += 4 + keyName.getBytes().length;
            this.keyName = keyName;
        }

    }

    public int getLength() {
        return this.length;
    }

    public byte[] getNonce() {
        return this.nonce;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public byte[] encode() {
        ByteBuffer bb = ByteBuffer.allocate(this.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(this.length);
        bb.putInt(this.nonce.length);
        bb.put(this.nonce);
        if (this.keyName != null) {
            byte[] b = this.keyName.getBytes();
            bb.putInt(b.length);
            bb.put(b);
        }

        return bb.array();
    }

    public static CiphertextHeader decode(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        int length = bb.getInt();
        byte[] nonce = new byte[bb.getInt()];
        bb.get(nonce);
        String keyName = null;
        if (length > nonce.length + 8) {
            byte[] b = new byte[bb.getInt()];
            bb.get(b);
            keyName = new String(b);
        }

        return new CiphertextHeader(nonce, keyName);
    }

    public static CiphertextHeader decode(InputStream input) {
        int length = ByteUtil.readInt(input);
        byte[] nonce = new byte[ByteUtil.readInt(input)];

        try {
            input.read(nonce);
        } catch (IOException var7) {
            throw new RuntimeException("Error reading from stream", var7);
        }

        String keyName = null;
        if (length > nonce.length + 8) {
            byte[] b = new byte[ByteUtil.readInt(input)];

            try {
                input.read(b);
            } catch (IOException var6) {
                throw new RuntimeException("Error reading from stream", var6);
            }

            keyName = new String(b);
        }

        return new CiphertextHeader(nonce, keyName);
    }
}

