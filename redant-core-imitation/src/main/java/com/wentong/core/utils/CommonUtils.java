package com.wentong.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

import static com.wentong.core.constant.CommonConstants.COMMON_PROPERTIES_LOCATION;

public final class CommonUtils {

    private CommonUtils() {
    }

    public static String getPropertyByName(String name) {
        return getPropertyByLocationAndName(COMMON_PROPERTIES_LOCATION, name);
    }


    /**
     * 根据目录和 properties 中 key 寻找 value
     *
     * @param location 配置文件位置
     * @param name     key 名
     */
    public static String getPropertyByLocationAndName(String location, String name) {
        try {
            InputStream resourceAsStream = CommonUtils.class.getResourceAsStream(location);
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            String property = properties.getProperty(name);
            if (StringUtils.isBlank(property)) {
                throw new IllegalArgumentException("无法在" + location + "找到 key:" + name);
            } else {
                return property;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("无法在" + location + "找到 key:" + name);
        }
    }

}
