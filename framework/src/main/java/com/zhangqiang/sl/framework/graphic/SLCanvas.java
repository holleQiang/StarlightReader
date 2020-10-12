package com.zhangqiang.sl.framework.graphic;

import com.zhangqiang.sl.framework.image.SLImage;

import java.awt.Paint;

public abstract class SLCanvas {

    public abstract void drawText(String mText, int start, int end, float x, float y, SLPaint mPaint);

    public abstract void clear();

    public abstract void translate(float dx, float dy);

    public abstract int save();

    public abstract void restore();

    public abstract void restoreToCount(int count);

    public abstract void drawImage(SLImage image, float left, float top);

    public abstract void drawImage(SLImage image, SLRect src, SLRect dst);

    public abstract void drawColor(int color);

    public abstract void clipRect(int left, int top, int right, int bottom);

    public abstract void setImage(SLImage image);

    public abstract void drawRect(SLRect rect, SLPaint paint);

    public abstract void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, SLPaint paint);
}