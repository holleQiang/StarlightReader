package com.zhangqiang.sl.android.render;

import android.view.View;

import com.zhangqiang.sl.framework.render.SLRenderBuffer;
import com.zhangqiang.sl.framework.view.SLRenderBufferFactory;

public class AndroidRenderBufferFactory implements SLRenderBufferFactory {

    private View view;
    private boolean multiThread;

    public AndroidRenderBufferFactory(View view,boolean multiThread) {
        this.view = view;
        this.multiThread = multiThread;
    }

    @Override
    public SLRenderBuffer create(int width, int height) {
        AndroidRenderBuffer renderBuffer = new AndroidRenderBuffer(width, height,multiThread);
        renderBuffer.setCallback(new SLRenderBuffer.Callback() {
            @Override
            public void onRenderBufferChanged() {
                if (view != null) {
                    view.postInvalidate();
                }
            }
        });
        return renderBuffer;
    }
}
