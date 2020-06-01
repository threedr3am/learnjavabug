package com.threedr3am.bug.fastjson.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

/**
 * @author threedr3am
 */
public class Threedr3am {

    public static void main(String[] args) {
        ParserConfig.global.addAccept("com.threedr3am.bug.fastjson.test.AAAA");
        String json = "{\"@type\":\"com.threedr3am.bug.fastjson.test.AAAA\", \"rrr\": {\"@type\": \"com.threedr3am.bug.fastjson.test.BBBB\", \"eval\": \"threedr3am\"}}";
        JSON.parse(json);
    }

}
class AAAA {
    private Runnable rrr;
    public Runnable getRrr() {
        return rrr;
    }
    public void setRrr(Runnable rrr) {
        this.rrr = rrr;
    }
}
class BBBB extends CCCC {
    private String eval;
    public String getEval() {
        return eval;
    }
    public void setEval(String eval) {
        System.out.println(eval + " eval!");
        this.eval = eval;
    }
}
class CCCC implements Runnable {
    @Override
    public void run() { }
}
