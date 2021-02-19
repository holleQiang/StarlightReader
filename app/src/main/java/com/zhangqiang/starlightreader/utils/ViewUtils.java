package com.zhangqiang.starlightreader.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;

public class ViewUtils {

    public static int dpToPx(Context context,float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }

    public static int spToPx(Context context,float sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,context.getResources().getDisplayMetrics());
    }

    public static void safePost(View view,Runnable runnable){

        view.post(new Runnable() {
            @Override
            public void run() {
                if (!ViewCompat.isAttachedToWindow(view)) {
                    return;
                }
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context){
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
