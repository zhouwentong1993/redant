package com.wentong.core.controller;

import com.wentong.core.annotations.Autowired;
import com.wentong.core.annotations.Bean;
import com.wentong.core.service.HelloService;

@Bean
public class HelloController {

    @Autowired
    private HelloService helloService;

}
