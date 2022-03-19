package com.java.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()  //HTTP Basic认证方式
                .and()
                .authorizeRequests()  // 授权配置
                .anyRequest()  // 所有请求
                .permitAll(); // 都需要认证
    }

}
