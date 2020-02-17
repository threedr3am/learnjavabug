package com.threedr3am.bug.common.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by threedr3am on 2018/5/5.
 */
public class FileToByteArrayUtil {
    /**
     * 读取class文件，转换为byte[]对象
     * @param classPath
     * @return
     * @throws IOException
     */
    public static byte[] readCallbackRuntimeClassBytes(String classPath) throws IOException {
        //执行前先编译CallbackRuntime类得到class文件
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(classPath);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        return bytes;
    }
}
