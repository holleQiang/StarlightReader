package com.zhangqiang.sl.framework.graphic;

public abstract class SLPath {

   public abstract void addRoundRect(SLRectF rect, float[] radii, Direction dir);

    public abstract void reset();

    public enum  Direction{

        CW,
        CCW
    }
}
