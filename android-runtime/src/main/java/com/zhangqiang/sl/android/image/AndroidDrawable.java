package com.zhangqiang.sl.android.image;

import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.zhangqiang.sl.android.render.canvas.AndroidCanvas;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLRect;
import com.zhangqiang.sl.framework.image.SLDrawable;

public class AndroidDrawable extends SLDrawable {

    private Drawable delegate;

    public AndroidDrawable(Drawable delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onDraw(SLCanvas canvas) {
        delegate.draw(((AndroidCanvas) canvas).getCanvas());
    }

    @Override
    public int getIntrinsicHeight() {
        return delegate.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return delegate.getIntrinsicWidth();
    }

    @Override
    protected void onBoundsChange(SLRect bounds) {
        super.onBoundsChange(bounds);
        delegate.setBounds(0,0,bounds.getWidth(),bounds.getHeight());
    }

    @Override
    public boolean isOpaque() {
        return delegate.getOpacity() == PixelFormat.OPAQUE;
    }


}
