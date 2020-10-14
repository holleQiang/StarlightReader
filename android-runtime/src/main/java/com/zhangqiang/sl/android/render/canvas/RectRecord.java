package com.zhangqiang.sl.android.render.canvas;

import android.graphics.Rect;

public class RectRecord {

    public final Rect rect = new Rect();
    private RectRecord next;
    private static RectRecord sPool;

    private RectRecord() {
    }

    public static RectRecord obtain() {
        if (sPool == null) {
            return new RectRecord();
        }
        RectRecord result = sPool;
        sPool = sPool.next;
        result.next = null;
        return result;
    }

    public void recycle() {
        next = sPool;
        sPool = this;
    }
}
