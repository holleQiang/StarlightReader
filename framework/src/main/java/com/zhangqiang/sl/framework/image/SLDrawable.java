package com.zhangqiang.sl.framework.image;

import com.zhangqiang.sl.framework.graphic.SLRect;
import com.zhangqiang.sl.framework.graphic.SLCanvas;

public abstract class SLDrawable {

    private SLRect mBounds;

    public void draw(SLCanvas canvas) {
        SLRect bounds = getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        int save = canvas.save();
        canvas.clipRect(bounds.getLeft(), bounds.getTop(), bounds.getRight(), bounds.getBottom());
        onDraw(canvas);
        canvas.restoreToCount(save);
    }

    protected abstract void onDraw(SLCanvas canvas);

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public void setBounds(int l, int t, int r, int b) {
        getBounds().set(l, t, r, b);
    }

    public SLRect getBounds() {
        if (mBounds == null) {
            mBounds = new SLRect();
        }
        return mBounds;
    }

    public boolean isOpaque() {
        return false;
    }
}
