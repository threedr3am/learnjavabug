package com.threedr3am.learn.client.boot;

import com.threedr3am.learn.server.boot.A;
import com.threedr3am.learn.server.boot.DemoService;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @author xuanyh
 */
@Service
public class Test {

  @Reference(version = "1.0")
  private DemoService demoService;

  @PostConstruct
  private void init() throws SQLException {
    A a = new A();
    a.setName("xuanyh");
    new Thread(() -> {
      while (true) {
        System.out.println(demoService.hello(a));
        try {
          Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }
}
