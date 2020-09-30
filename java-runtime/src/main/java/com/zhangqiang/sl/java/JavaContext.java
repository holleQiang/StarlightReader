package com.zhangqiang.sl.java;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.handler.SLHandler;
import com.zhangqiang.sl.framework.image.SLImage;
import com.zhangqiang.sl.framework.render.SLFramePoster;

public class JavaContext extends SLContext {
    @Override
    public SLPaint newPaint() {
        return new JavaPaint();
    }

    @Override
    public int getScaledTouchSlop() {
        return 20;
    }

    @Override
    public float getDensity() {
        return 0;
    }

    @Override
    public float getScrollFriction() {
        return 0;
    }

    @Override
    public long getCurrentAnimationTimeMillis() {
        return 0;
    }

    @Override
    protected SLHandler onCreateHandler(SLHandler.Callback callback) {
        return null;
    }

    @Override
    protected SLFramePoster onCreateFramePoster() {
        return null;
    }

    @Override
    public SLImage createImage(int width, int height) {
        return null;
    }

    @Override
    public SLCanvas createCanvas(SLImage image) {
        return null;
    }
}
