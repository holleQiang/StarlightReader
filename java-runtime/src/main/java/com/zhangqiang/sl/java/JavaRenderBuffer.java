package com.zhangqiang.sl.java;

import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.render.SLRenderBuffer;

public class JavaRenderBuffer extends SLRenderBuffer {
    @Override
    public SLCanvas lockCanvas() {
        return null;
    }

    @Override
    protected void handUnlockCanvasAndPost(SLCanvas canvas) {

    }

    @Override
    protected void onDestroy() {

    }
}
