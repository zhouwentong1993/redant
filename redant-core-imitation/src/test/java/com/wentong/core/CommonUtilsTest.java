package com.wentong.core;

import org.junit.Assert;
import org.junit.Test;

import static com.wentong.core.utils.CommonUtils.getPropertyByLocationAndName;

public class CommonUtilsTest {

    @Test
    public void testGetPropertyByLocationAndName() {
        String propertyByLocationAndName = getPropertyByLocationAndName("/application.properties", "scan.package");
        Assert.assertEquals(propertyByLocationAndName, "com.wentong");
    }
}
