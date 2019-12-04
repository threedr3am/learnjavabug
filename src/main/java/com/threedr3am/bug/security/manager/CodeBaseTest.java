package com.threedr3am.bug.security.manager;

import java.io.FilePermission;
import java.io.IOException;
import java.security.AccessControlException;

/**
 * @author xuanyh
 */
public class CodeBaseTest {

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    SecurityManager sm = System.getSecurityManager();
    try {
      sm.checkRead("/tmp/aaa");
    } catch (AccessControlException e) {
      System.out.println("没有权限：" + e.getMessage());
    }
    try {
      sm.checkWrite("/tmp/aaa");
    } catch (AccessControlException e) {
      System.out.println("没有权限：" + e.getMessage());
    }
    try {
      sm.checkDelete("/tmp/aaa");
    } catch (AccessControlException e) {
      System.out.println("没有权限：" + e.getMessage());
    }
    try {
      sm.checkPermission(new FilePermission("/tmp/aaa","execute"));
    } catch (AccessControlException e) {
      System.out.println("没有权限：" + e.getMessage());
    }
  }
}
/**
 * grant codeBase "file:/Users/xuanyh/IdeaProjects/learnjavabug/target/classes/*" {
 *   permission java.io.FilePermission "/tmp/aaa","read";
 * };
 */
