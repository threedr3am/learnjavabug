package com.threedr3am.bug.shiro.bypass.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * CVE-2020-1957
 *
 * todo 当存在某个Controller使用了动态Controller时，例：存在接口/bypass和/bypass/{id}，就能通过访问 http://localhost:8080/bypass.xxxxx 或 http://localhost:8080/aaaaa/..;/bypass 绕过接口/bypass的认证控制
 * todo When there is a dynamic Controller, the Controller USES the example: there are api interface /bypass and /bypass/{id}, you can visit http://localhost:8080/bypass.xxxxx or http://localhost:8080/aaaaa/..;bypass to bypass authentication
 *
 * todo 漏洞点在于使用了getRequestURI
 * todo The vulnerability point is in use 'getRequestURI()'
 *
 * /aaaaa/..;/bypass
 * /bypass.xxxxx
 *
 * @author threedr3am
 */
@RestController
public class BypassTestController {

    /**
     *
     * 例：配置"/bypass", "authc"，请求http://localhost:8080/bypass.xxxxx
     *
     * Example: configuration "/bypass", "authc", request to http://localhost:8080/bypass.xxxxx bypass
     *
     * shiro < 1.5.2
     *
     * @return
     */
    @RequestMapping(value = "/bypass", method = RequestMethod.GET)
    public String bypass() {
        return "bypass1";
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/bypass/{id}", method = RequestMethod.GET)
    public String bypass2(String id) {
        return "bypass2";
    }
}
