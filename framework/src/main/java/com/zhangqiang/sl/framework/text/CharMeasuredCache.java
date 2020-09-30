package com.zhangqiang.sl.framework.text;

import java.util.HashMap;
import java.util.Map;

public class CharMeasuredCache {

    private static final Map<Integer, Float> measuredCache = new HashMap<>();

    public static float get(char c, float textSize) {
//        return 50;
//        int textSizeInt = (int) (textSize * 100);
//        int key = ((int) c) & 0xFFFF << 16 | textSizeInt & 0xFFFF;
//        Float integer = measuredCache.get(key);
//        if (integer != null) {
//            return integer;
//        }
        return -1;
    }

    public static void put(char c, float textSize, float width) {
        int textSizeInt = (int) (textSize * 100);
        int key = ((int) c) & 0xFFFF << 16 | textSizeInt & 0xFFFF;
        measuredCache.put(key, width);
    }
}
