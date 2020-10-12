package com.zhangqiang.sl.framework.view;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MeasureOptions {


    private static final int MODE_SHIFT = 30;
    public static final int MODE_MASK = 0x3 << MODE_SHIFT;

    @Retention(RetentionPolicy.SOURCE)
    @interface MeasureMode {

    }

    /**
     * 就是这么大
     */
    public static final int MODE_UNSPECIFIED = 0;
    /**
     * 只能这么大
     */
    public static final int MODE_EXACTLY = 1 << MODE_SHIFT;
    /**
     * 最多xx大
     */
    public static final int MODE_AD_MOST = 2 << MODE_SHIFT;


    public static int make(int size, int mode) {

        return size & ~MODE_MASK | mode & MODE_MASK;
    }

    public static int getSize(int measureSpec) {
        return measureSpec & ~MODE_MASK;
    }

    public static int getMode(int measureSpec) {
        return measureSpec & MODE_MASK;
    }
}
