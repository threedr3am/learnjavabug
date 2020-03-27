package com.threedr3am.bug.shiro.bypass.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * CVE-2020-1957
 *
 * todo 通过访问 http://localhost:8080/bypass.xxxxx 或 http://localhost:8080/aaaaa/..;/bypass 绕过接口/bypass的认证控制
 *
 *  * todo 漏洞点在于使用了getRequestURI
 *  * todo The vulnerability point is in use 'getRequestURI()'
 *
 * todo    /aaaaa/..;/bypass  ->  bypass ->  ("/bypass", "authc")、("/bypass.*", "authc")、("/bypass/**", "authc")     (shiro <= 1.5.1)
 * todo    /bypass.xxxxx      ->  bypass ->  ("/bypass", "authc")、("/bypass/**", "authc")                             (shiro all version)
 *
 * 有人说，实际开发不会这样配置，都是通过最后加一个("/**", "authc")全范围匹配，然而，实际上会存在某些放过的接口，例如/aaaaa/**，就能利用它进行bypass
 * todo    /aaaaa/..;/bypass  利用存在（"/aaaaa/**", "anon"）去bypass ("/**", "authc")
 *
 * @author threedr3am
 */
@RestController
public class BypassTestController {

    /**
     * @return
     */
    @RequestMapping(value = "/bypass", method = RequestMethod.GET)
    public String bypass() {
        return "bypass1";
    }
}
