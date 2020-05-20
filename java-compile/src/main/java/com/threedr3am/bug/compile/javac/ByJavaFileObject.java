package com.threedr3am.bug.compile.javac;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * @author threedr3am
 */
public class ByJavaFileObject {

    //使用了JavaFileObject指定java文件进行编译
    public static void d() {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        ClassLoader classLoader = ByJavaFileObject.class.getClassLoader();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector();
        StandardJavaFileManager standardJavaFileManager = javaCompiler
            .getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("utf-8"));
//        FileManagerImpl fileManager = new FileManagerImpl(standardJavaFileManager);
        Iterable<? extends JavaFileObject> javaFileObjects = standardJavaFileManager.getJavaFileObjects("/tmp/ccc/CCC.java", "/tmp/Main.java");

        // 编译任务
        CompilationTask task = javaCompiler.getTask(null, standardJavaFileManager, diagnostics, null, null, javaFileObjects);
        Boolean result = task.call();
        System.out.println(result);
        List list = diagnostics.getDiagnostics();
        for (Object object : list) {
            Diagnostic d = (Diagnostic) object;
            System.out.println(d.getMessage(Locale.ENGLISH));
        }
    }

    public static void main(String[] args) {
        d();
    }
}
