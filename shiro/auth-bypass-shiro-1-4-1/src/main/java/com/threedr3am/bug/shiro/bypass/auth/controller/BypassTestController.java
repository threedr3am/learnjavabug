package com.threedr3am.bug.shiro.bypass.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author threedr3am
 */
@RestController
public class BypassTestController {

    /**
     * todo 过滤器配置（参考ShiroConfig）中bypass映射认证过滤器最后一个URI字符没有/，使用spring和shiro对资源的解析不一致进行bypass
     *
     * 例：配置"/bypass", "authc"，请求http://localhost:8080/bypass/
     *
     * shiro <= 1.4.1
     *
     * @return
     */
    @RequestMapping(value = "/bypass", method = RequestMethod.GET)
    public String bypass() {
        return "bypass";
    }
}
