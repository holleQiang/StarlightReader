package com.zhangqiang.sl.android.render.canvas;

import android.graphics.RectF;

public class RectFRecord {

    public final RectF rect = new RectF();
    private RectFRecord next;
    private static RectFRecord sPool;

    private RectFRecord() {
    }

    public static RectFRecord obtain() {
        if (sPool == null) {
            return new RectFRecord();
        }
        RectFRecord result = sPool;
        sPool = sPool.next;
        result.next = null;
        return result;
    }

    public void recycle() {
        next = sPool;
        sPool = this;
    }
}
