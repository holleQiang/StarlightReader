package com.zhangqiang.sl.android.render.canvas;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import com.zhangqiang.sl.android.AndroidPaint;
import com.zhangqiang.sl.android.image.AndroidImage;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.graphic.SLRect;
import com.zhangqiang.sl.framework.image.SLImage;

public abstract class AndroidBasicCanvas extends SLCanvas {

    @Override
    public final void drawText(String mText, int start, int end, float x, float y, SLPaint mPaint) {
        onDrawText(mText, start, end, x, y, ((AndroidPaint) mPaint).getPaint());
    }

    protected abstract void onDrawText(String mText, int start, int end, float x, float y, Paint paint);

    @Override
    public final void drawImage(SLImage image, float left, float top) {
        onDrawImage(((AndroidImage) image).getBitmap(), left, top);
    }

    protected abstract void onDrawImage(Bitmap bitmap, float left, float top);

    @Override
    public final void drawImage(SLImage image, SLRect src, SLRect dst) {

        RectRecord record = null;
        Rect srcRect;
        if (src == null) {
            srcRect = null;
        } else {
            record = RectRecord.obtain();
            record.rect.set(src.getLeft(), src.getTop(), src.getRight(), src.getBottom());
            srcRect = record.rect;
        }

        RectRecord dstRecord = null;
        Rect dstRect;
        if (dst == null) {
            dstRect = null;
        } else {
            dstRecord = RectRecord.obtain();
            dstRecord.rect.set(dst.getLeft(), dst.getTop(), dst.getRight(), dst.getBottom());
            dstRect = dstRecord.rect;
        }

        onDrawImage(((AndroidImage) image).getBitmap(), srcRect, dstRect);

        if (record != null) {
            record.recycle();
        }
        if (dstRecord != null) {
            dstRecord.recycle();
        }
    }

    protected abstract void onDrawImage(Bitmap bitmap, Rect src, Rect dst);


    @Override
    public final void setImage(SLImage image) {
        if (image == null) {
            onSetImage(null);
            return;
        }
        onSetImage(((AndroidImage) image).getBitmap());
    }

    protected abstract void onSetImage(Bitmap bitmap);

    @Override
    public void drawRect(SLRect rect, SLPaint paint) {
        RectRecord record = RectRecord.obtain();
        record.rect.set(rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom());
        onDrawRect(record.rect, ((AndroidPaint) paint).getPaint());
        record.recycle();
    }

    protected abstract void onDrawRect(Rect rect, Paint paint);

    static class RectRecord {

        final Rect rect = new Rect();
        private RectRecord next;
        private static RectRecord sPool;

        private RectRecord() {
        }

        static RectRecord obtain() {
            if (sPool == null) {
                return new RectRecord();
            }
            RectRecord result = sPool;
            sPool = sPool.next;
            result.next = null;
            return result;
        }

        void recycle() {
            next = sPool;
            sPool = this;
        }
    }
}
