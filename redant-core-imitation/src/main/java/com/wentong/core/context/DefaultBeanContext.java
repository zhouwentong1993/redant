package com.wentong.core.context;

import cn.hutool.core.lang.ClassScaner;
import com.wentong.core.annotations.Autowired;
import com.wentong.core.annotations.Bean;
import com.wentong.core.controller.HelloController;
import com.wentong.core.init.InitFunc;
import com.wentong.core.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DefaultBeanContext implements BeanContext, InitFunc {

    private DefaultBeanContext(){}

    private static DefaultBeanContext instance = new DefaultBeanContext();

    public static DefaultBeanContext getInstance() {
        return instance;
    }

    private Map<String, Object> objectContainer = new LinkedHashMap<>();


    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanContext.class);

    @Override
    public Object getBean(String beanName) {
        return objectContainer.get(beanName);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        Object o = objectContainer.get(beanName);
        if (o.getClass() != clazz) {
            return null;
        } else {
            return (T) o;
        }
    }

    @Override
    public void init() {
        LOGGER.info("init class start ……");
        scanAllBeans();
        LOGGER.info("init class end ……");
    }


    /**
     * 扫描所有带有 @Bean 注解的类
     */
    private void scanAllBeans() {
        Set<Class<?>> classes = ClassScaner.scanPackageByAnnotation(CommonUtils.getPropertyByName("scan.package"), Bean.class);
        for (Class<?> aClass : classes) {
            Bean annotation = aClass.getAnnotation(Bean.class);
            // 全限定名
            String className = StringUtils.isBlank(annotation.name()) ? aClass.getName() : annotation.name();
            if (objectContainer.containsKey(className)) {
                LOGGER.warn("init class：{} duplicate", className);
                throw new IllegalStateException("重复的 bean: " + aClass.getName());
            } else {
                try {
                    LOGGER.info("class:{} init succ", className);
                    objectContainer.put(className, aClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalStateException("无法初始化类：" + className);
                }
            }
        }
    }

    private void injectField() {
        for (Object object : objectContainer.values()) {

        }
    }

    private void injectViaSetter(Object bean) {
        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                Method writeMethod = descriptor.getWriteMethod();
                if (writeMethod != null && writeMethod.getAnnotation(Autowired.class) != null) {
                    Autowired annotation = writeMethod.getAnnotation(Autowired.class);
                    String name = annotation.name();
                    // 配置了 name，走 name 的配置
                    if (StringUtils.isNotBlank(name)) {
                        Object value = objectContainer.get(name);
                        if (value == null) {
                            throw new IllegalStateException("无法注入:" + name + "类，不存在");
                        }
                        writeMethod.setAccessible(true);
                        writeMethod.invoke(bean, value);
                    } else { // 如果没有配置，走 type
                        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
                        if (parameterTypes.length != 1) {
                            throw new IllegalStateException("setter 方法" + writeMethod.getName() + "格式不对");
                        }
                        Class<?> parameterType = parameterTypes[0];
                        findSuitableTypeValue(bean, writeMethod, parameterType);

                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void findSuitableTypeValue(Object bean, Method writeMethod, Class<?> parameterType) throws IllegalAccessException, InvocationTargetException {
        for (Object value : objectContainer.values()) {
            if (value.getClass().isAssignableFrom(parameterType)) {
                writeMethod.setAccessible(true);
                writeMethod.invoke(bean, value);
                return;
            }
        }
        throw new IllegalArgumentException("setter 方法" + writeMethod.getName() + "找不到类型：" + parameterType);
    }

    public static void main(String[] args) {
        DefaultBeanContext objectDefaultBeanContext = new DefaultBeanContext();
        objectDefaultBeanContext.scanAllBeans();
        Object helloService = objectDefaultBeanContext.getBean("test");
        HelloController helloController = objectDefaultBeanContext.getBean("com.wentong.core.controller.HelloController", HelloController.class);
    }

}
