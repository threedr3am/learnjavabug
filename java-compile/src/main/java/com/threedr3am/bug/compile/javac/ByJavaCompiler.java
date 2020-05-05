package com.threedr3am.bug.compile.javac;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * @author threedr3am
 */
public class ByJavaCompiler {

    //直接使用JavaCompiler指定java文件编译
    public static void c() {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = javaCompiler.run(null, null, null, "-cp", "/tmp/ccc/CCC.jar", "/tmp/Main.java");
        // 返回0表示编译成功
        if (compilationResult == 0) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }

    public static void main(String[] args) {
        c();
    }
}
