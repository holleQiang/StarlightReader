package com.zhangqiang.sl.framework.image;

import com.zhangqiang.sl.framework.graphic.SLRect;
import com.zhangqiang.sl.framework.graphic.SLCanvas;

public abstract class SLDrawable {

    private SLRect mBounds;

    public final void draw(SLCanvas canvas) {
        SLRect bounds = getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        int save = canvas.save();
        canvas.translate(bounds.getLeft(),bounds.getTop());
        canvas.clipRect(0, 0, bounds.getWidth(), bounds.getHeight());
        onDraw(canvas);
        canvas.restoreToCount(save);
    }

    protected abstract void onDraw(SLCanvas canvas);

    public int getIntrinsicWidth() {
        return 0;
    }

    public int getIntrinsicHeight() {
        return 0;
    }

    public void setBounds(int l, int t, int r, int b) {
        SLRect oldBounds = mBounds;
        if (oldBounds == null) {
            oldBounds = mBounds = new SLRect();
        }
        if (oldBounds.getLeft() != l
                || oldBounds.getTop() != t
                || oldBounds.getRight() != r
                || oldBounds.getBottom() != b) {
            mBounds.set(l, t, r, b);
            onBoundsChange(mBounds);
        }
    }

    protected void onBoundsChange(SLRect bounds) {

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
