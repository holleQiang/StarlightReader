package com.zhangqiang.sl.android;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhangqiang.sl.android.render.AndroidFramePoster;
import com.zhangqiang.sl.android.render.AndroidRenderBuffer;
import com.zhangqiang.sl.android.render.AndroidRenderBufferFactory;
import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.gesture.SLMotionEvent;
import com.zhangqiang.sl.framework.handler.SLHandler;
import com.zhangqiang.sl.framework.handler.SLMessage;
import com.zhangqiang.sl.framework.render.SLFramePoster;
import com.zhangqiang.sl.framework.view.FramePosterFactory;
import com.zhangqiang.sl.framework.view.SLRootView;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewRoot;

public class AndroidSLView extends View implements ISLView{

    private static final int MSG_MOTION_EVENT = 1;
    private SLViewRoot mViewRoot;
    private SLView mTempContentView;
    private SLRootView mRootView;
    private SLHandler mHandler;

    public AndroidSLView(Context context) {
        super(context);
        init(context);
    }


    public AndroidSLView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AndroidSLView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mViewRoot = new SLViewRoot(new AndroidContext(context, true),
                new AndroidRenderBufferFactory(this, true),
                new FramePosterFactory() {
                    @Override
                    protected SLFramePoster onCreateFramePoster() {
                        return new AndroidFramePoster();
                    }
                });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        quit();
        start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AndroidRenderBuffer renderBuffer = (AndroidRenderBuffer) mViewRoot.getRenderBuffer();
        if (renderBuffer != null) {
            renderBuffer.flush(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        quit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouchEvent(event);
        return true;
    }


    public void handleTouchEvent(MotionEvent event) {

        SLMotionEvent motionEvent = SLMotionEvent.obtain();
        motionEvent.setX(event.getX());
        motionEvent.setY(event.getY());
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            motionEvent.setAction(SLMotionEvent.ACTION_DOWN);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            motionEvent.setAction(SLMotionEvent.ACTION_MOVE);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            motionEvent.setAction(SLMotionEvent.ACTION_UP);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            motionEvent.setAction(SLMotionEvent.ACTION_CANCEL);
        }
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
                        SLMotionEvent motionEvent = (SLMotionEvent) message.obj;
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

}
