package com.zhangqiang.sl.framework.render;

import com.zhangqiang.sl.framework.handler.SLHandler;

public class DefaultFramePoster extends SLFramePoster {

    private SLHandler handler;

    public DefaultFramePoster(SLHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void onPostFrameCallback(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    protected void onRemoveFrameCallback(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }

}
