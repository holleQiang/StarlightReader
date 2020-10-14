package com.zhangqiang.sl.framework.image;

import com.zhangqiang.sl.framework.graphic.SLCanvas;

public class SLImageDrawable extends SLDrawable {

    private SLImage image;

    public SLImageDrawable(SLImage image) {
        this.image = image;
    }

    @Override
    protected void onDraw(SLCanvas canvas) {
        canvas.drawImage(image,null, getBounds());
    }

    @Override
    public int getIntrinsicWidth() {
        return image.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return image.getHeight();
    }

    @Override
    public boolean isOpaque() {
        return image != null && !image.hasAlpha();
    }
}
