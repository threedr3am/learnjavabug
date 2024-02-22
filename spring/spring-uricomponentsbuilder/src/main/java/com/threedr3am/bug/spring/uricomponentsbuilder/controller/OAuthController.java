package com.threedr3am.bug.spring.uricomponentsbuilder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author threedr3am
 *
 * CVE-2024-22243
 *
 * Spring Framework 是一个开源的Java应用程序框架，UriComponentsBuilder是Spring Web中用于构建和操作URI的工具类。
 * 受影响版本中，由于 UriComponentsBuilder 处理URL时未正确过滤用户信息中的方括号 `[` ，攻击者可构造包含方括号的恶意URL绕过主机名验证。
 * 如果应用程序依赖UriComponentsBuilder.fromUriString()等方法对URL进行解析和校验，则可能导致验证绕过，出现开放重定向或SSRF漏洞。
 *
 * ###  修复方案
 * 1. 将 org.springframework:spring-web 升级至 6.1.4 及以上版本
 * 2. 将 org.springframework:spring-web 升级至 6.0.17 及以上版本
 * 3. 将 org.springframework:spring-web 升级至 5.3.32 及以上版本
 *
 * ### 参考链接
 * 1. https://www.oscs1024.com/hd/MPS-uwzo-gx91
 * 2. https://spring.io/security/cve-2024-22243
 * 3. https://github.com/spring-projects/spring-framework/commit/7ec5c994c147f0e168149498b1c9d4a249d69e87
 * 4. https://nvd.nist.gov/vuln/detail/CVE-2024-22243
 */
@Controller
@RequestMapping("/oauth")
public class OAuthController {

    private static final Set<String> whiteDomains = new HashSet<>(Arrays.asList(new String[]{
            ".fuckpdd.com"
    }));

    /**
     * 一般绕过oauth的host校验，可以开放重定向到恶意站点劫持code
     * 访问：http://127.0.0.1:8080/oauth?redirect_uri=http%3A%2F%2Fwww.fuckpdd.com%5B%40www.evil.com%2Ftou
     *
     *
     * @param redirectUri http://www.fuckpdd.com[@www.evil.com/tou
     * @return
     */
    @GetMapping
    public String oauth(@RequestParam(name = "redirect_uri") String redirectUri, HttpServletResponse response) throws IOException {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(redirectUri).build();
        String schema = uriComponents.getScheme();
        String host = uriComponents.getHost();
        String path = uriComponents.getPath();

        System.out.printf("schema:%s\n", schema);
        System.out.printf("host:%s\n", host);
        System.out.printf("path:%s\n", path);

        boolean pass = false;
        for (String whiteDomain : whiteDomains) {
            if (host.endsWith(whiteDomain)) {
                pass = true;
                break;
            }
        }
        if (!pass) return "error";

        return "redirect:" + redirectUri;
    }
}
