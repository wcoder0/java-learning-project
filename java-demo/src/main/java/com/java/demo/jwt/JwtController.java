package com.java.demo.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

/**
 * 同时启动8888和8889,都能拿到session的值
 */
@RestController
@RequestMapping("/jwt")
public class JwtController {

    @GetMapping("/get")
    public Object getSession(HttpServletRequest httpServletRequest) {
        Object session = httpServletRequest.getSession().getAttribute("session");

        return session;
    }

    @GetMapping("/set")
    public void setSession(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().setAttribute("session", "data");
    }

}
