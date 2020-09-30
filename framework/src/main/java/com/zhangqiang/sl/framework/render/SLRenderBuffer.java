package com.zhangqiang.sl.framework.render;

import com.zhangqiang.sl.framework.graphic.SLCanvas;

public abstract class SLRenderBuffer {

    private Callback callback;

    public abstract SLCanvas lockCanvas();

    public final void unlockCanvasAndPost(SLCanvas canvas) {
        handUnlockCanvasAndPost(canvas);
        if (callback != null) {
            callback.onRenderBufferChanged();
        }
    }

    protected abstract void handUnlockCanvasAndPost(SLCanvas canvas);

    public void destroy() {
        onDestroy();
    }

    protected abstract void onDestroy();

    public interface Callback {

        void onRenderBufferChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
