package com.wentong.core.controller;

import com.wentong.core.annotations.Autowired;
import com.wentong.core.annotations.Bean;
import com.wentong.core.service.HelloService;

import java.lang.reflect.Field;

@Bean
public class HelloController {

    @Autowired(name = "test")
    private HelloService helloService;

    public static void main(String[] args) {
        Field[] declaredFields = HelloController.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getType());
        }
    }

}
