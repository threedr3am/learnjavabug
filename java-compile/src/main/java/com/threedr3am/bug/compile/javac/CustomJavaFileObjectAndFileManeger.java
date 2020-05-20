package com.threedr3am.bug.compile.javac;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * @author threedr3am
 */
public class CustomJavaFileObjectAndFileManeger {

    //使用了自定义JavaFileObject实现的动态编译
    public static void e() throws URISyntaxException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        ClassLoader classLoader = CustomJavaFileObjectAndFileManeger.class.getClassLoader();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector();
        StandardJavaFileManager standardJavaFileManager = javaCompiler
            .getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("utf-8"));
//        FileManagerImpl fileManager = new FileManagerImpl(standardJavaFileManager);
        StringBuilder stringBuilder = new StringBuilder()
            .append("class Main {")
            .append("   public static void main(String[] args) {")
            .append("       System.out.println(\"hello FFF!\");")
            .append("   }")
            .append("}");
        Iterable<? extends JavaFileObject> javaFileObjects = Arrays
            .asList(new JavaObjectFromString("Main", stringBuilder.toString()));

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

    static class FileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {

        private final Map<URI, JavaFileObject> fileObjects
            = new HashMap<URI, JavaFileObject>();

        public FileManagerImpl(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public FileObject getFileForInput(Location location, String packageName,
            String relativeName) throws IOException {
            FileObject o = fileObjects.get(uri(location, packageName, relativeName));
            if (o != null) {
                return o;
            }
            return super.getFileForInput(location, packageName, relativeName);
        }

        public void putFileForInput(StandardLocation location, String packageName,
            String relativeName, JavaFileObject file) {
            fileObjects.put(uri(location, packageName, relativeName), file);
        }

        private URI uri(Location location, String packageName, String relativeName) {

            return null;
        }
    }
}
