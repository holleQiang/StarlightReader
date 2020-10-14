package com.zhangqiang.sl.android.graphic;

import android.graphics.Paint;
import android.text.TextPaint;

import com.zhangqiang.sl.framework.graphic.SLPaint;

public class AndroidPaint extends SLPaint {

    private Paint mPaint = new TextPaint();

    public AndroidPaint() {
        init();
    }

    private void init() {
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

    @Override
    public void setStyle(Style style) {
        switch (style) {
            case FILL:
                mPaint.setStyle(Paint.Style.FILL);
                break;
            case STROKE:
                mPaint.setStyle(Paint.Style.STROKE);
                break;
            case FILL_AND_STROKE:
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                break;
        }

    }

    @Override
    public float getStrokeWidth() {
        return mPaint.getStrokeWidth();
    }

    @Override
    public void reset() {
        mPaint.reset();
        init();
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        mPaint.setStrokeWidth(strokeWidth);
    }

    public Paint getPaint() {
        return mPaint;
    }
}
