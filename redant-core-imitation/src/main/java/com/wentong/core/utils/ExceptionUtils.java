package com.wentong.core.utils;

import com.wentong.core.exception.CommonException;

public final class ExceptionUtils {

    public static void warring(String message) {
        throw new CommonException(message);
    }
}
