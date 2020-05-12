package com.threedr3am.bug.compile.javac;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * @author threedr3am
 */
public class CustomJavaFileObject {

    //使用了自定义JavaFileObject实现的动态编译
    public static void e() throws URISyntaxException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        ClassLoader classLoader = CustomJavaFileObject.class.getClassLoader();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector();
        StandardJavaFileManager standardJavaFileManager = javaCompiler
            .getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("utf-8"));
//        FileManagerImpl fileManager = new FileManagerImpl(standardJavaFileManager);
//        StringBuilder stringBuilder = new StringBuilder()
//            .append("class Main {")
//            .append("   public static void main(String[] args) {")
//            .append("       System.out.println(\"hello FFF!\");")
//            .append("   }")
//            .append("}");
        StringBuilder stringBuilder = new StringBuilder()
            .append("class Main {")
            .append("   static {")
            .append("       System.out.println(\"hello FFF!\");")
            .append("   }")
            .append("}");
        JavaFileObject javaFileObject = new JavaObjectFromString("tmp.Main", stringBuilder.toString());
        Iterable<? extends JavaFileObject> javaFileObjects = Arrays
            .asList(javaFileObject);

        // 编译任务
        CompilationTask task = javaCompiler.getTask(null, standardJavaFileManager, diagnostics, null, null, javaFileObjects);
        Boolean result = task.call();
        System.out.println(result);
        List list = diagnostics.getDiagnostics();
        for (Object object : list) {
            Diagnostic d = (Diagnostic) object;
            System.out.println(d.getMessage(Locale.ENGLISH));
        }

        try {
            new URLClassLoader(new URL[]{javaFileObject.toUri().toURL()}).loadClass("Main");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        e();
    }

    static class JavaObjectFromString extends SimpleJavaFileObject {

        private String content;

        public JavaObjectFromString(String className, String content) throws URISyntaxException {
            super(new URI(className), Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return content;
        }
    }
}
