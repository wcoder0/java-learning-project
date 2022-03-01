package com.java.daily.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
//@RequestMapping
public class PageController {

   private Logger logger = LoggerFactory.getLogger(this.getClass());

   @RequestMapping("/home")
   public String index() {
      logger.info("定向登陆页");
      return "index";
   }

   @RequestMapping("/login")
   public String toLogin() {
      logger.info("定向主页");
      return "toLogin";
   }

   @RequestMapping("/logout")
   public String logout() {
     /* logger.info("退出系统");
      Subject subject = SecurityUtils.getSubject();
      subject.logout(); // shiro底层删除session的会话信息*/
      return "redirect:login";
   }
}
