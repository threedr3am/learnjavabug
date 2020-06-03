package com.threedr3am.bug.cas.support;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author threedr3am
 */
public final class ByteUtil {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final Charset ASCII_CHARSET = Charset.forName("ASCII");

    private ByteUtil() {
    }

    public static int toInt(byte[] data) {
        return data[0] << 24 | (data[1] & 255) << 16 | (data[2] & 255) << 8 | data[3] & 255;
    }

    public static int readInt(InputStream in) {
        try {
            return in.read() << 24 | (in.read() & 255) << 16 | (in.read() & 255) << 8 | in.read() & 255;
        } catch (IOException var2) {
            throw new RuntimeException("Error reading from stream.", var2);
        }
    }

    public static long toLong(byte[] data) {
        return (long)data[0] << 56 | ((long)data[1] & 255L) << 48 | ((long)data[2] & 255L) << 40 | ((long)data[3] & 255L) << 32 | ((long)data[4] & 255L) << 24 | ((long)data[5] & 255L) << 16 | ((long)data[6] & 255L) << 8 | (long)data[7] & 255L;
    }

    public static long readLong(InputStream in) {
        try {
            return (long)in.read() << 56 | ((long)in.read() & 255L) << 48 | ((long)in.read() & 255L) << 40 | ((long)in.read() & 255L) << 32 | ((long)in.read() & 255L) << 24 | ((long)in.read() & 255L) << 16 | ((long)in.read() & 255L) << 8 | (long)in.read() & 255L;
        } catch (IOException var2) {
            throw new RuntimeException("Error reading from stream.", var2);
        }
    }

    public static byte[] toBytes(int value) {
        byte[] bytes = new byte[4];
        toBytes(value, bytes, 0);
        return bytes;
    }

    public static void toBytes(int value, byte[] output, int offset) {
        int shift = 24;

        for(int i = 0; i < 4; ++i) {
            output[offset + i] = (byte)(value >> shift);
            shift -= 8;
        }

    }

    public static byte[] toBytes(long value) {
        byte[] bytes = new byte[8];
        toBytes(value, bytes, 0);
        return bytes;
    }

    public static void toBytes(long value, byte[] output, int offset) {
        int shift = 56;

        for(int i = 0; i < 8; ++i) {
            output[offset + i] = (byte)((int)(value >> shift));
            shift -= 8;
        }

    }

    public static String toString(byte[] bytes) {
        return new String(bytes, DEFAULT_CHARSET);
    }

    public static String toString(ByteBuffer buffer) {
        return toCharBuffer(buffer).toString();
    }

    public static CharBuffer toCharBuffer(ByteBuffer buffer) {
        return DEFAULT_CHARSET.decode(buffer);
    }

    public static ByteBuffer toByteBuffer(String s) {
        return DEFAULT_CHARSET.encode(CharBuffer.wrap(s));
    }

    public static byte[] toBytes(String s) {
        return s.getBytes(DEFAULT_CHARSET);
    }

    public static byte[] toArray(ByteBuffer buffer) {
        if (buffer.limit() == buffer.capacity()) {
            return buffer.array();
        } else {
            byte[] array = new byte[buffer.limit()];
            buffer.position(0);
            buffer.get(array);
            return array;
        }
    }
}

