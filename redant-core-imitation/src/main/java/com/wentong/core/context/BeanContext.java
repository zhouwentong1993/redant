package com.wentong.core.context;

public interface BeanContext {

    Object getBean(String beanName);

    <T> T getBean(String beanName, Class<T> clazz);

}
