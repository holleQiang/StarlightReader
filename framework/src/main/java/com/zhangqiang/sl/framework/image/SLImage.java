package com.zhangqiang.sl.framework.image;

public abstract class SLImage {

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void recycle();

    public abstract boolean hasAlpha();

    public abstract void eraseColor(int color);
}
