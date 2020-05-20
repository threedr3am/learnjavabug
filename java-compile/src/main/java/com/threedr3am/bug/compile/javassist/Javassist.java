package com.threedr3am.bug.compile.javassist;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * @author threedr3am
 */
public class Javassist {

    //测试实例化class
    public static void testNewInstance()
        throws NotFoundException, IllegalAccessException, InstantiationException, CannotCompileException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.threedr3am.bug.compile.javassist.A");
        Class c = ctClass.toClass();
        Object o = c.newInstance();
        System.out.println(Arrays.stream(c.getMethods()).map(method -> method.getName()).collect(
            Collectors.joining(" ")));
        System.out.println(o.getClass().getName());
    }

    //测试添加父类
    public static void testAddSuperClass()
        throws NotFoundException, IllegalAccessException, InstantiationException, CannotCompileException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.threedr3am.bug.compile.javassist.A");
        CtClass ctClass2 = classPool.get("com.threedr3am.bug.compile.javassist.B");
        ctClass.setSuperclass(ctClass2);
        Class c = ctClass.toClass();
        Object o = c.newInstance();
        System.out.println(Arrays.stream(c.getMethods()).map(method -> method.getName()).collect(
            Collectors.joining(" ")));
        System.out.println(o.getClass().getName());
    }

    //测试创建class
    public static void testMakeClass()
        throws NotFoundException, IllegalAccessException, InstantiationException, CannotCompileException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("com.threedr3am.bug.compile.javassist.C");
        Class c = ctClass.toClass();
        Object o = c.newInstance();
        System.out.println(Arrays.stream(c.getMethods()).map(method -> method.getName()).collect(
            Collectors.joining(" ")));
        System.out.println(o.getClass().getName());
    }

    //测试添加方法
    public static void testAddMethod()
        throws NotFoundException, IllegalAccessException, InstantiationException, CannotCompileException, NoSuchMethodException, InvocationTargetException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("com.threedr3am.bug.compile.javassist.C");
        StringBuilder stringBuilder = new StringBuilder()
            .append("   public void main() {")
            .append("       System.out.println(\"hello FFF!\");")
            .append("   }");
        ctClass.addMethod(CtNewMethod.make(stringBuilder.toString(), ctClass));
        Class c = ctClass.toClass();
        Object o = c.newInstance();
        c.getDeclaredMethod("main").invoke(o);
        System.out.println(Arrays.stream(c.getMethods()).map(method -> method.getName()).collect(
            Collectors.joining(" ")));
    }

    //测试修改方法
    public static void testModifyMethod()
        throws NotFoundException, IllegalAccessException, InstantiationException, CannotCompileException, NoSuchMethodException, InvocationTargetException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.threedr3am.bug.compile.javassist.A");
        StringBuilder stringBuilder = new StringBuilder()
            .append("{System.out.println(\"hello FFF --- \" + $1);}");
        ctClass.getMethod("a", "(Ljava/lang/String;)V").insertAfter(stringBuilder.toString());
        Class c = ctClass.toClass();
        Object o = c.newInstance();
        c.getDeclaredMethod("a", String.class).invoke(o, "threedr3am");
        System.out.println(Arrays.stream(c.getMethods()).map(method -> method.getName()).collect(
            Collectors.joining(" ")));
    }

    //测试添加字段
    public static void testAddField()
        throws NotFoundException, IllegalAccessException, InstantiationException, CannotCompileException, NoSuchFieldException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("com.threedr3am.bug.compile.javassist.D");
        StringBuilder stringBuilder = new StringBuilder()
            .append("   public String name = \"threedr3am\";");
        ctClass.addField(CtField.make(stringBuilder.toString(), ctClass));
        Class c = ctClass.toClass();
        Object o = c.newInstance();
        System.out.println(c.getDeclaredField("name").get(o));;
        System.out.println(Arrays.stream(c.getDeclaredFields()).map(field -> field.getName()).collect(
            Collectors.joining(" ")));
    }

    //测试修改字段，todo 修改字段名称时，貌似有个问题是字段不能有显式的初始化值，要不然在实例化执行初始化代码时会找不到原来的字段，因为字段修改只修改了字段，没修改方法内的引用
    public static void testModifyField()
        throws NotFoundException, IllegalAccessException, InstantiationException, CannotCompileException, NoSuchFieldException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.threedr3am.bug.compile.javassist.A");
        ctClass.getField("name").setName("cName");
        Class c = ctClass.toClass();
        Object o = c.newInstance();
        Field field = c.getDeclaredField("cName");
        field.setAccessible(true);
        System.out.println(field.get(o));
        System.out.println(Arrays.stream(c.getFields()).map(f -> f.getName()).collect(
            Collectors.joining(" ")));
    }

    public static void main(String[] args)
        throws NotFoundException, InstantiationException, IllegalAccessException, CannotCompileException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
//        testNewInstance();
//        testAddSuperClass();
//        testMakeClass();
//        testAddMethod();
//        testModifyMethod();
//        testAddField();
        testModifyField();
    }
}

class A {

    private String name;

    public A() {
    }

    public void a(String name) {

    }

    public void printName() {
        System.out.println(name);
    }
}

class B {

    public void b() {

    }
}
