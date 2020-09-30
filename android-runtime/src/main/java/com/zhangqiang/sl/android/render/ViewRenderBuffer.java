package com.zhangqiang.sl.android.render;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import com.zhangqiang.sl.android.render.canvas.AndroidCanvas;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.render.SLRenderBuffer;

public class ViewRenderBuffer extends SLRenderBuffer {

    public static final boolean debug = true;
    public static final String TAG = ViewRenderBuffer.class.getCanonicalName();
    private final Object mCanvasLock = new Object();
    private final Object mRenderLock = new Object();
    private AndroidCanvas mCanvas = new AndroidCanvas();
    private View view;
    private boolean mLockCanvasRequested;
    public ViewRenderBuffer(View view) {
        this.view = view;
    }

    @Override
    public SLCanvas lockCanvas() {

        synchronized (mCanvasLock) {
            mLockCanvasRequested = true;
            if (mCanvas.getCanvas() == null) {

                try {
                    if (debug) {
                        logI("=====子线程请求canvas，被锁========");
                    }
                    mCanvasLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        logI("=====子线程请求到canvas========");
        return mCanvas;
    }

    @Override
    protected void handUnlockCanvasAndPost(SLCanvas canvas) {
        synchronized (mCanvasLock) {
            mCanvas.setCanvas(null);
            mLockCanvasRequested = false;
        }
        synchronized (mRenderLock) {
            if (debug) {
                logI("=====子线程绘制完毕，释放锁========");
            }
            mRenderLock.notifyAll();
        }
    }

    @Override
    protected void onDestroy() {

    }

    public void setCanvas(Canvas canvas) {
        synchronized (mCanvasLock) {
            if (!mLockCanvasRequested) {
                return;
            }
            mCanvas.setCanvas(canvas);
            mCanvasLock.notifyAll();
        }
        synchronized (mRenderLock) {
            try {
                if (debug) {
                    logI("=====主线程被锁，等待绘制========");
                }
                mRenderLock.wait();
                if (debug) {
                    logI("=====主线程被释放========");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void logI(String log){
        Log.i(TAG,log);
    }

}