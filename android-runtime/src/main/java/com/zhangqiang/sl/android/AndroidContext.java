package com.zhangqiang.sl.android;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;

import com.zhangqiang.sl.android.graphic.AndroidPaint;
import com.zhangqiang.sl.android.graphic.AndroidPath;
import com.zhangqiang.sl.android.handler.AndroidHandler;
import com.zhangqiang.sl.android.image.AndroidImage;
import com.zhangqiang.sl.android.log.AndroidLogger;
import com.zhangqiang.sl.android.render.canvas.AndroidCanvas;
import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.graphic.SLPath;
import com.zhangqiang.sl.framework.handler.SLHandler;
import com.zhangqiang.sl.framework.image.SLImage;

public class AndroidContext extends SLContext {

    private Context context;
    private volatile HandlerThread mHandlerThread;
    private boolean useMultiThread;

    static {
        setLogger(new AndroidLogger());
    }

    public AndroidContext(Context context, boolean useMultiThread) {
        this.context = context.getApplicationContext();
        this.useMultiThread = useMultiThread;
    }

    @Override
    public SLPaint newPaint() {
        return new AndroidPaint();
    }

    @Override
    public SLPath newPath() {
        return new AndroidPath();
    }

    @Override
    public int getScaledTouchSlop() {
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public float getDensity() {
        return context.getResources().getDisplayMetrics().density;
    }

    @Override
    public float getScrollFriction() {
        return ViewConfiguration.getScrollFriction();
    }

    @Override
    public long getCurrentAnimationTimeMillis() {
        return AnimationUtils.currentAnimationTimeMillis();
    }

    @Override
    protected SLHandler onCreateHandler(SLHandler.Callback callback) {
        if (useMultiThread) {
            if (mHandlerThread == null) {
                synchronized (AndroidContext.this) {
                    if (mHandlerThread == null) {
                        mHandlerThread = new HandlerThread("render_thread");
                        mHandlerThread.start();
                    }
                }
            }
            return new AndroidHandler(callback,new Handler(mHandlerThread.getLooper()));
        }else {
            return new AndroidHandler(callback,new Handler(Looper.getMainLooper()));
        }
    }

    @Override
    public SLImage createImage(int width, int height) {
        return new AndroidImage(width,height);
    }

    @Override
    public SLCanvas createCanvas(SLImage image) {
        return new AndroidCanvas(image);
    }

    @Override
    public boolean isRenderThread() {
        return Looper.myLooper() == (useMultiThread ? mHandlerThread.getLooper() : Looper.getMainLooper());
    }
}
