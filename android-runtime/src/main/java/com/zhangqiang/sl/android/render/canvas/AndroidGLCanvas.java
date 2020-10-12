package com.zhangqiang.sl.android.render.canvas;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import com.chillingvan.canvasgl.CanvasGL;
import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.zhangqiang.sl.android.image.AndroidImage;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.graphic.SLRect;
import com.zhangqiang.sl.framework.image.SLImage;

public class AndroidGLCanvas extends AndroidBasicCanvas {

    private ICanvasGL canvasGL;
    private int mSaveFlags;
    private GLPaint mTempPaint = new GLPaint();

    public AndroidGLCanvas() {
    }

    @Override
    protected void onDrawText(String mText, int start, int end, float x, float y, Paint paint) {
        throw new RuntimeException("not support for text");
    }

    @Override
    protected void onDrawImage(Bitmap bitmap, float left, float top) {
        canvasGL.drawBitmap(bitmap, (int) left, (int) top);
    }

    @Override
    protected void onDrawImage(Bitmap bitmap, Rect src, Rect dst) {
        canvasGL.drawBitmap(bitmap, src, dst);
    }

    @Override
    protected void onSetImage(Bitmap bitmap) {
        throw new RuntimeException("not support for onSetImage");
    }

    @Override
    protected void onDrawRect(Rect rect, Paint paint) {
        mTempPaint.setColor(paint.getColor());
        canvasGL.drawRect(rect, mTempPaint);
    }

    @Override
    public void clear() {
        canvasGL.clearBuffer();
    }

    @Override
    public void translate(float dx, float dy) {
        canvasGL.translate(dx, dy);
    }

    @Override
    public int save() {
        canvasGL.save();
        return 0;
    }

    @Override
    public void restore() {
        canvasGL.restore();
    }

    @Override
    public void restoreToCount(int count) {
        canvasGL.restore();
    }

    @Override
    public void drawColor(int color) {

        RectRecord record = RectRecord.obtain();
        record.rect.set(0,0,canvasGL.getWidth(),canvasGL.getHeight());
        mTempPaint.setColor(color);
        canvasGL.drawRect(record.rect,mTempPaint);
        record.recycle();
    }

    @Override
    public void clipRect(int left, int top, int right, int bottom) {

    }

    @Override
    public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, SLPaint paint) {

    }

    public void setCanvasGL(ICanvasGL canvasGL) {
        this.canvasGL = canvasGL;
    }

    public ICanvasGL getCanvasGL() {
        return canvasGL;
    }
}
