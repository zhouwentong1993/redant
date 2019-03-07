package com.wentong.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CommonTest {

    @Test
    public void testHashMapComputeIfAbsent() {
        Map<String,Object> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        map.computeIfAbsent("a", String::toUpperCase);
        map.computeIfAbsent("c", String::toUpperCase);
        System.out.println(map);

    }

}
