package com.zhangqiang.sl.android;

import android.graphics.Paint;
import android.text.TextPaint;

import com.zhangqiang.sl.framework.graphic.SLPaint;

public class AndroidPaint extends SLPaint {

    private Paint mPaint = new TextPaint();

    public AndroidPaint() {
        mPaint.setAntiAlias(true);
    }

    @Override
    public float measureText(String text, int start, int end) {
        return mPaint.measureText(text, start, end);
    }

    @Override
    public void setTextSize(float textSize) {
        mPaint.setTextSize(textSize);
    }

    @Override
    public float getTextSize() {
        return mPaint.getTextSize();
    }

    @Override
    public float getTextHeight() {
        return mPaint.descent() - mPaint.ascent();
    }

    @Override
    public float ascent() {
        return mPaint.ascent();
    }

    @Override
    public void setColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    public int getColor() {
        return mPaint.getColor();
    }

    public Paint getPaint() {
        return mPaint;
    }
}
