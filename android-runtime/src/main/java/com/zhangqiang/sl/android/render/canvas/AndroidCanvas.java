package com.zhangqiang.sl.android.render.canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextPaint;


import com.zhangqiang.sl.android.AndroidPaint;
import com.zhangqiang.sl.android.image.AndroidImage;
import com.zhangqiang.sl.framework.graphic.SLRect;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.image.SLImage;

public class AndroidCanvas extends AndroidBasicCanvas {

    private Canvas canvas;
    private Paint paint = new TextPaint();

    public AndroidCanvas(Canvas mCanvas) {
        canvas = mCanvas;
    }

    public AndroidCanvas() {
    }

    public AndroidCanvas(SLImage image) {
        this(new Canvas(((AndroidImage) image).getBitmap()));
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    protected void onDrawText(String mText, int start, int end, float x, float y, Paint paint) {
        canvas.drawText(mText, start, end, x, y, paint);
    }

    @Override
    public void clear() {

        RectRecord rectRecord = RectRecord.obtain();
        rectRecord.rect.set(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(rectRecord.rect, paint);
        rectRecord.recycle();
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    @Override
    public void translate(float dx, float dy) {
        canvas.translate(dx, dy);
    }

    @Override
    public int save() {
        return canvas.save();
    }

    @Override
    public void restore() {
        canvas.restore();
    }

    @Override
    public void restoreToCount(int count) {
        canvas.restoreToCount(count);
    }


    @Override
    protected void onDrawImage(Bitmap bitmap, float left, float top) {
        canvas.drawBitmap(bitmap, left, top, null);
    }

    @Override
    protected void onDrawImage(Bitmap bitmap, Rect src, Rect dst) {
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    @Override
    public void drawColor(int color) {
        canvas.drawColor(color);
    }

    @Override
    public void clipRect(int left, int top, int right, int bottom) {
        canvas.clipRect(left, top, right, bottom);
    }

    @Override
    protected void onSetImage(Bitmap bitmap) {
        canvas.setBitmap(bitmap);
    }

    @Override
    protected void onDrawRect(Rect rect, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    public Canvas getCanvas() {
        return canvas;
    }


    private static class RectRecord {

        private final Rect rect = new Rect();
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
