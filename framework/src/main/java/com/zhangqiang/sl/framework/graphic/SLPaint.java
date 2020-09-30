package com.zhangqiang.sl.framework.graphic;

public abstract class SLPaint {

    public abstract float measureText(String text, int start, int end);

    public abstract void setTextSize(float textSize);

    public abstract float getTextSize();

    public abstract float getTextHeight();

    public abstract float ascent();

    public abstract void setColor(int color);

    public abstract int getColor();
}
