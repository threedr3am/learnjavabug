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
 * CVE-2024-22259
 *
 * 使用UricomponentsBuilder解析外部提供的URL(例如通过查询参数)并对解析URL的主机执行验证检查的应用程序可能容易受到公开重定向攻击，
 * 如果在通过验证检查后使用该URL，则可能容易受到SSRF攻击。
 * 这与CVE-2024-22243相同，这是另一种输入不同的情况。
 *
 * ###  修复方案
 * 1. 将 org.springframework:spring-web 升级至 6.1.5 及以上版本
 * 2. 将 org.springframework:spring-web 升级至 6.0.18 及以上版本
 * 3. 将 org.springframework:spring-web 升级至 5.3.33 及以上版本
 *
 * ### 参考链接
 * https://spring.io/security/cve-2024-22259
 * https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2024-22259
 *
 */
@Controller
@RequestMapping("/oauth")
public class OAuthController {

    private static final Set<String> whiteDomains = new HashSet<>(Arrays.asList(new String[]{
            ".fuckpdd.com"
    }));

    /**
     * 一般绕过oauth的host校验，可以开放重定向到恶意站点劫持code
     * 访问：http://127.0.0.1:8080/oauth?redirect_uri=http%3A%2F%2F%40www.fuckpdd.com%5B%40www.evil.com%2Ftou
     *
     *
     * @param redirectUri [CVE-2024-22259] -> http://@www.fuckpdd.com[@www.evil.com/tou
     *                    [CVE-2024-22243] -> http://www.fuckpdd.com[@www.evil.com/tou
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
