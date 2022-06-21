package me.threedr3am.bug.shiro.bypass.auth.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo 这个洞利用价值不大，基本使用shiro做认证的系统，都会利用/** authc兜底
 * CVE-2020-11989
 *
 * todo-1. 通过访问 http://localhost:8080/bypass/bypass/aaa%252Faaa (两次编码的"aaa/aaa") 绕过接口/bypass的认证控制
 *  * 漏洞点在于tomcat只会对url进行一次解码，而shiro进行了两次解码
 *  * 两次解码后，路径变成 http://localhost:8080/bypass/bypass/aaa/aaa 绕过了权限 "/bypass/*" 的match
 *
 * todo-2. 通过访问 http://localhost:8080/;/bypass/bypass/111 绕过接口/bypass的认证控制
 *  * 漏洞点在于shiro会对;分号进行截断，访问的 /;/bypass/bypass/111 变成了 / ，自然就绕过了权限 "/bypass/*" 的match
 *  * server:
 *      context-path: /bypass
 *
 * @author threedr3am
 */
@RestController
public class BypassTestController {

    /**
     * @return
     */
    @RequestMapping(value = "/bypass/{id}", method = RequestMethod.GET)
    public String bypass(@PathVariable(name = "id") String id) {
        return "bypass1 -> " + id;
    }
}
