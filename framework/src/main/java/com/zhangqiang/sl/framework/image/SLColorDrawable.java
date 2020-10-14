package com.zhangqiang.sl.framework.image;

import com.zhangqiang.sl.framework.graphic.SLCanvas;

public class SLColorDrawable extends SLDrawable {

    private int color;

    public SLColorDrawable(int color) {
        this.color = color;
    }

    @Override
    protected void onDraw(SLCanvas canvas) {
        canvas.drawColor(color);
    }

    @Override
    public boolean isOpaque() {
        return (color >>> 24) == 0xff;
    }

}
