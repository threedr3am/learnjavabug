package com.xyh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by xuanyonghao on 2018/5/5.
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
        FileInputStream fileInputStream = new FileInputStream(new File(classPath));
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        return bytes;
    }
}
