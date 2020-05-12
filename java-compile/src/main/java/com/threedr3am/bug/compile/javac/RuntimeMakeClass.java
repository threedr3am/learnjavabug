package com.threedr3am.bug.compile.javac;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * @author threedr3am
 */
public class RuntimeMakeClass {

    //使用了自定义JavaFileObject实现的动态编译
    public void e() throws IOException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector();
        StandardJavaFileManager standardJavaFileManager = javaCompiler
            .getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("utf-8"));
        int id = new Random().nextInt(10000000);
        StringBuilder stringBuilder = new StringBuilder()
            .append("import java.io.BufferedReader;\n")
            .append("import java.io.IOException;\n")
            .append("import java.io.InputStream;\n")
            .append("import java.io.InputStreamReader;\n")
            .append("public class Threedr3am" + id + " {\n")
            .append("   public static String result = \"\";\n")
            .append("   static {\n")
            .append("        try {")
            .append("               StringBuilder stringBuilder = new StringBuilder();\n")
            .append("               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(\"" + "ls" + "\").getInputStream()));\n")
            .append("               String line;\n")
            .append("               while((line = bufferedReader.readLine()) != null) {\n")
            .append("                       stringBuilder.append(line).append(\"\\n\");\n")
            .append("               }\n")
            .append("               result = stringBuilder.toString();\n")
            .append("        } catch (Exception e) {\n")
            .append("              e.printStackTrace();\n")
            .append("        }\n")
            .append("   }\n")
            .append("}");
        String classPath = "/tmp/";
        Files.write(Paths.get(classPath + "Threedr3am" +id + ".java"), stringBuilder.toString().getBytes());
        Iterable fileObject = standardJavaFileManager.getJavaFileObjects(classPath + "Threedr3am" +id + ".java");
        javaCompiler.getTask(null, standardJavaFileManager, diagnostics, null, null, fileObject).call();
        try {
            System.out.println(new URLClassLoader(new URL[]{new URL("file:" + classPath)}).loadClass("Threedr3am" + id).getField("result").get(null));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new RuntimeMakeClass().e();
    }
}
