package com.threedr3am.learn.server.boot;

import org.apache.dubbo.config.annotation.Service;

/**
 * @author xuanyh
 */
@Service(registry = "dubboRegistry", timeout = 3000, version = "1.0", retries = 3, loadbalance = "random", actives = 5)
public class DemoServiceImpl implements DemoService {

  public String hello(A a) {
    return "hello! " + a.getName();
  }
}
