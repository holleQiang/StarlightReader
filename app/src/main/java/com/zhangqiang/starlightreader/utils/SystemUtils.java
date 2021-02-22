package com.zhangqiang.starlightreader.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

public class SystemUtils {


    /**
     * 获取系统默认屏幕亮度值 屏幕亮度值范围（0-255）
     **/
    public static int getScreenBrightness(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        int defVal = 125;
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, defVal);
    }

    /**
     * 获取系统默认屏幕亮度值 0f-1f
     **/
    public static float getScreenBrightnessPercent(Context context) {
        return (float) getScreenBrightness(context) / 255;
    }
}
