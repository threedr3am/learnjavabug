package com.threedr3am.bug.asm;

import com.google.common.reflect.ClassPath;
import com.sun.org.apache.bcel.internal.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.mozilla.javascript.GeneratedClassLoader;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.commons.JSRInlinerAdapter;

/**
 * 学习asm字节码操作，fuzz基础
 *
 * @author threedr3am
 */
public class Test {

  public static void main(String[] args) {
//    //TODO 遍历当前线程类加载器加载的所有类，进行asm操作
//    try {
//      for (ClassPath.ClassInfo classInfo : ClassPath.from(Thread.currentThread().getContextClassLoader()).getAllClasses()) {
//        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classInfo.getResourceName())) {
//          ClassReader cr = new ClassReader(in);
//          try {
//            MethodCallDiscoveryClassVisitor visitor = new MethodCallDiscoveryClassVisitor(Opcodes.ASM6);
//            cr.accept(visitor, ClassReader.EXPAND_FRAMES);
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
//        }
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    //TODO 遍历类com.threedr3am.bug.asm.Test，进行asm操作
    try {
      try (InputStream in = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream("com.threedr3am.bug.asm.Test".replace(".", "/") + ".class")) {
        ClassReader cr = new ClassReader(in);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        try {
          MyClassVisitorAdapter visitor = new MyClassVisitorAdapter(
              Opcodes.ASM6, cw);
          cr.accept(visitor, ClassReader.EXPAND_FRAMES);
          GeneratorClassLoader classLoader = new GeneratorClassLoader();
          classLoader.defineClassFromClassFile("com.threedr3am.bug.asm.Test", cw.toByteArray());
          Class c = classLoader.loadClass("com.threedr3am.bug.asm.Test");
          Object o = c.newInstance();
          Method method = c.getDeclaredMethod("test", new Class[]{String.class});
          method.invoke(o, "输入参数");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void test(String str) {
    System.out.println("call Test.test(" + str + ")");
  }

  public static class GeneratorClassLoader extends ClassLoader {

    public Class defineClassFromClassFile(String className, byte[] classFile)
        throws ClassFormatError {
      return defineClass(className, classFile, 0, classFile.length);
    }
  }

  /**
   * ClassVisitor 基于观察者模式的class asm操作
   *
   * visit -> visitSource -> visitInnerClass -> visitInnerClass -> visitMethod -> visitMethod ->
   * visitMethod -> visitEnd
   */
  private static class MyClassVisitorAdapter extends ClassVisitor {

    public MyClassVisitorAdapter(int api, ClassVisitor cv) {
      super(api, cv);
    }

    @Override
    public void visitSource(String source, String debug) {
      System.out.printf("visitSource -> ");
      super.visitSource(source, debug);
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
      System.out.printf("visitModule -> ");
      return super.visitModule(name, access, version);
    }

    @Override
    public void visitNestHostExperimental(String nestHost) {
      System.out.printf("visitNestHostExperimental -> ");
      super.visitNestHostExperimental(nestHost);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
      System.out.printf("visitOuterClass -> ");
      super.visitOuterClass(owner, name, descriptor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
      System.out.printf("visitAnnotation -> ");
      return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor,
        boolean visible) {
      System.out.printf("visitTypeAnnotation -> ");
      return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
      System.out.printf("visitAttribute -> ");
      super.visitAttribute(attribute);
    }

    @Override
    public void visitNestMemberExperimental(String nestMember) {
      System.out.printf("visitNestMemberExperimental -> ");
      super.visitNestMemberExperimental(nestMember);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
      System.out.printf("visitInnerClass -> ");
      super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature,
        Object value) {
      System.out.printf("visitField -> ");
      return super.visitField(access, name, descriptor, signature, value);
    }

    private String name = null;

    /**
     * @param version 字节码版本
     * @param name 类全限定名
     * @param superName 继承的基类
     * @param interfaces 实现的接口
     */
    @Override
    public void visit(int version, int access, String name, String signature,
        String superName, String[] interfaces) {
      System.out.printf("visit -> ");
      super.visit(version, access, name, signature, superName, interfaces);
//      System.out.println("---------------------------------------------");
//      System.out.println("version: " + version);
//      System.out.println("access: " + access);
//      System.out.println("name: " + name);
//      System.out.println("signature: " + signature);
//      System.out.println("superName: " + superName);
//      System.out.println("interfaces: " + Arrays.toString(interfaces));
      if (this.name != null) {
        throw new IllegalStateException("ClassVisitor already visited a class!");
      }
      this.name = name;
    }

    /**
     * @param name 方法名
     * @param desc 方法参数和返回值
     * @param signature 方法签名
     * @param exceptions 方法抛出的异常数组
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
        String signature, String[] exceptions) {
      System.out.printf("visitMethod -> ");
      MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
//      System.out.println();
//      System.out.println("access: " + access);
//      System.out.println("name: " + name);
//      System.out.println("desc: " + desc);
//      System.out.println("signature: " + signature);
//      System.out.println("exceptions: " + Arrays.toString(exceptions));

      //适配器封装MethodVisitor，进行asm操作方法字节码
      return new MethodVisitorAdapter(api, mv, this.name, name, desc);
    }

    @Override
    public void visitEnd() {
      System.out.printf("visitEnd\n");
      super.visitEnd();
    }
  }

  /**
   * MethodVisitor 基于观察者模式的method asm操作
   */
  private static class MethodVisitorAdapter extends MethodVisitor {

    private String owner = null;
    private MethodVisitor mv = null;
    private String name = null;

    public MethodVisitorAdapter(final int api, final MethodVisitor mv,
        final String owner, String name, String desc) {
      super(api, mv);
      this.owner = owner;
      this.mv = mv;
      this.name = name;
    }

    @Override
    public void visitParameter(String name, int access) {
      super.visitParameter(name, access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
      return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
      return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor,
        boolean visible) {
      return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
      super.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor,
        boolean visible) {
      return super.visitParameterAnnotation(parameter, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
      super.visitAttribute(attribute);
    }

    /**
     * 进入方法代码执行时会先执行该方法
     */
    @Override
    public void visitCode() {
      super.visitCode();
      if (name.equals("test")) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("插入代码调用，输出字符串");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
            "(Ljava/lang/String;)V", false);
      }
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
      super.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
      super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
      super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
      super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
      super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
      super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
      super.visitMethodInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
        Object... bootstrapMethodArguments) {
      super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle,
          bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
      super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
      super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object value) {
      super.visitLdcInsn(value);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
      super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
      super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
      super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
      super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor,
        boolean visible) {
      return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
      super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath,
        String descriptor, boolean visible) {
      return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start,
        Label end, int index) {
      super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath,
        Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
      return super
          .visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
      super.visitLineNumber(line, start);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
      super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    public void visitEnd() {
      super.visitEnd();
    }

    /**
     * TODO this.name base call method（调用方法） TODO this.owner base call class（调用类）
     *
     * @param owner target class（被调用类）
     * @param name target method（被调用方法）
     * @param desc 方法参数和返回值描述
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      super.visitMethodInsn(opcode, owner, name, desc, itf);
//      System.out.println("++++++++++++++++++++++++++++++++++++++++");
//      System.out.println("this.owner: " + this.owner);
//      System.out.println("this.name: " + this.name);
//      System.out.println("opcode: " + opcode);
//      System.out.println("owner: " + owner);
//      System.out.println("name: " + name);
//      System.out.println("desc: " + desc);
//      System.out.println("itf: " + itf);
    }
  }

}
