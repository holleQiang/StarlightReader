package com.zhangqiang.sl.android.graphic;

import android.graphics.Path;

import com.zhangqiang.sl.android.render.canvas.RectFRecord;
import com.zhangqiang.sl.framework.graphic.SLPath;
import com.zhangqiang.sl.framework.graphic.SLRectF;

public class AndroidPath extends SLPath {

    private Path delegate = new Path();

    public Path getPath() {
        return delegate;
    }

    @Override
    public void addRoundRect(SLRectF rect, float[] radii, Direction dir) {
        RectFRecord rectFRecord = RectFRecord.obtain();
        rectFRecord.rect.set(rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom());
        Path.Direction andDirection = Path.Direction.CW;
        if (dir == Direction.CCW) {
            andDirection = Path.Direction.CCW;
        }
        delegate.addRoundRect(rectFRecord.rect, radii, andDirection);
        rectFRecord.recycle();
    }

    @Override
    public void reset() {
        delegate.reset();
    }
}
