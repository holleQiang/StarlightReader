package com.zhangqiang.sl.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhangqiang.sl.android.event.AndroidMotionEvent;
import com.zhangqiang.sl.android.render.AndroidFramePoster;
import com.zhangqiang.sl.android.render.canvas.AndroidCanvas;
import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.gesture.SLMotionEvent;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.handler.SLHandler;
import com.zhangqiang.sl.framework.handler.SLMessage;
import com.zhangqiang.sl.framework.render.SLFramePoster;
import com.zhangqiang.sl.framework.render.SLRenderBuffer;
import com.zhangqiang.sl.framework.view.FramePosterFactory;
import com.zhangqiang.sl.framework.view.SLRenderBufferFactory;
import com.zhangqiang.sl.framework.view.SLRootView;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewRoot;

public class AndroidSurfaceSLView extends SurfaceView implements ISLView {

    private static final int MSG_MOTION_EVENT = 1;
    private SLViewRoot mViewRoot;
    private SLView mTempContentView;
    private SLRootView mRootView;
    private SLHandler mHandler;

    public AndroidSurfaceSLView(Context context) {
        super(context);
        init(context);
    }

    public AndroidSurfaceSLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AndroidSurfaceSLView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mViewRoot = new SLViewRoot(new AndroidContext(context, true),
                new SLRenderBufferFactory() {
                    @Override
                    public SLRenderBuffer create(int width, int height) {
                        return new SurfaceRenderBuffer();
                    }
                },
                new FramePosterFactory() {
                    @Override
                    protected SLFramePoster onCreateFramePoster() {
                        return new AndroidFramePoster();
                    }
                });
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                quit();
                start();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                quit();
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouchEvent(event);
        return true;
    }


    public void handleTouchEvent(MotionEvent event) {

        AndroidMotionEvent motionEvent = AndroidMotionEvent.obtain(event);
        mViewRoot.dispatchTouchEvent(motionEvent);
        SLMessage message = mHandler.obtainMessage(MSG_MOTION_EVENT);
        message.obj = motionEvent;
        message.sendToTarget();
    }

    @Override
    public void setContentView(SLView view) {
        if (mRootView == null) {
            mTempContentView = view;
        } else {
            mRootView.setContentView(view);
        }
    }

    @Override
    public SLContext getSLContext() {
        return mViewRoot.getContext();
    }

    private void start() {

        SLContext context = mViewRoot.getContext();
        mRootView = new SLRootView(context);
        if (mTempContentView != null) {
            mRootView.setContentView(mTempContentView);
            mTempContentView = null;
        }
        mViewRoot.setView(mRootView, getWidth(), getHeight());

        mHandler = context.createHandler(new SLHandler.Callback() {
            @Override
            public boolean handMessage(SLMessage message) {
                switch (message.what) {
                    case MSG_MOTION_EVENT:
                        AndroidMotionEvent motionEvent = (AndroidMotionEvent) message.obj;
                        mViewRoot.dispatchTouchEvent(motionEvent);
                        motionEvent.recycle();
                        break;
                }
                return false;
            }
        });
    }

    private void quit() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages();
            mHandler = null;
        }
        if (mViewRoot != null) {
            mViewRoot.release();
        }
    }

    private class SurfaceRenderBuffer extends SLRenderBuffer {

        AndroidCanvas canvas = new AndroidCanvas();

        @Override
        public SLCanvas lockCanvas() {
            Canvas canvas;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                 canvas = getHolder().lockHardwareCanvas();
//            }else {
//                canvas = getHolder().lockCanvas();
//            }
            canvas = getHolder().lockCanvas();
            this.canvas.setCanvas(canvas);
            return this.canvas;
        }

        @Override
        protected void handUnlockCanvasAndPost(SLCanvas canvas) {
            getHolder().unlockCanvasAndPost(((AndroidCanvas) canvas).getCanvas());
        }

        @Override
        protected void onDestroy() {

        }
    }
}
