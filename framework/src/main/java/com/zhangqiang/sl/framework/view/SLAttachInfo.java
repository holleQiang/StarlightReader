package com.zhangqiang.sl.framework.view;

import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.handler.SLHandler;

public final class SLAttachInfo {

    private SLViewRoot mViewRoot;
    private SLHandler mHandler;
    /**
     * canvas for drawing cache
     */
    private SLCanvas mCanvas;

    public SLAttachInfo(SLViewRoot mViewRoot) {
        this.mViewRoot = mViewRoot;
    }

    protected SLViewRoot getViewRoot() {
        return mViewRoot;
    }

    public SLHandler getHandler() {
        return mHandler;
    }

    public void setHandler(SLHandler handler) {
        this.mHandler = handler;
    }

    public SLCanvas getCanvas() {
        return mCanvas;
    }

    public void setCanvas(SLCanvas canvas) {
        this.mCanvas = canvas;
    }
}
