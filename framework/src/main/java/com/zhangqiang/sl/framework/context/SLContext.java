package com.zhangqiang.sl.framework.context;

import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.graphic.SLPath;
import com.zhangqiang.sl.framework.handler.SLHandler;
import com.zhangqiang.sl.framework.image.SLImage;
import com.zhangqiang.sl.framework.log.DefaultLogger;
import com.zhangqiang.sl.framework.log.SLLogger;
import com.zhangqiang.sl.framework.render.SLFramePoster;

public abstract class SLContext {

    private static SLLogger mLogger;
    private static final SLLogger mDefaultLogger = new DefaultLogger();

    public abstract SLPaint newPaint();

    public abstract SLPath newPath();

    public abstract int getScaledTouchSlop();

    public abstract float getDensity();

    public abstract float getScrollFriction();

    public abstract long getCurrentAnimationTimeMillis();

    public static SLLogger getLogger() {
        if (mLogger == null) {
            return mDefaultLogger;
        }
        return mLogger;
    }

    public static void setLogger(SLLogger logger) {
        SLContext.mLogger = logger;
    }

    public SLHandler createHandler() {
        return createHandler(null);
    }

    public SLHandler createHandler(SLHandler.Callback callback) {
        return onCreateHandler(callback);
    }

    protected abstract SLHandler onCreateHandler(SLHandler.Callback callback);

    public abstract SLImage createImage(int width, int height);

    public abstract SLCanvas createCanvas(SLImage image);

    public abstract boolean isRenderThread();
}
