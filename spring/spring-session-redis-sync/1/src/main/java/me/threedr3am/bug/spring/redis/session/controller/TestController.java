package me.threedr3am.bug.spring.redis.session.controller;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author threedr3am
 */
@RestController
public class TestController {

    @GetMapping("/cache")
    public String cacheData(@RequestParam(name = "data", required = false) String data, HttpSession httpSession) {
        if (data == null) {
            return String.valueOf(httpSession.getAttribute("data"));
        } else {
            httpSession.setAttribute("data", data);
            return data;
        }
    }
}
