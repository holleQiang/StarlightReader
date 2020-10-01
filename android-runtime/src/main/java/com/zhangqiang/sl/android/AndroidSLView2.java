package com.zhangqiang.sl.android;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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

import java.util.ArrayList;
import java.util.List;

public class AndroidSLView2 extends View implements ISLView {

    private SLViewRoot mViewRoot;
    private SLView mTempContentView;
    private SLRootView mRootView;
    private final List<Runnable> mRunnableList = new ArrayList<>();

    public AndroidSLView2(Context context) {
        super(context);
        init(context);
    }

    public AndroidSLView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AndroidSLView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mViewRoot = new SLViewRoot(new AndroidContext(context, false), new SLRenderBufferFactory() {
            @Override
            public SLRenderBuffer create(int width, int height) {
                return new ViewRenderBuffer();
            }
        }, new FramePosterFactory() {
            @Override
            protected SLFramePoster onCreateFramePoster() {

                return new SLFramePoster() {

                    @Override
                    protected void onPostFrameCallback(Runnable runnable) {
                        mRunnableList.add(runnable);
                        postInvalidate();
                    }

                    @Override
                    protected void onRemoveFrameCallback(Runnable runnable) {
                        mRunnableList.remove(runnable);
                    }
                };
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

        ((ViewRenderBuffer) mViewRoot.getRenderBuffer()).setCanvas(canvas);
        int size = mRunnableList.size();
        if (size > 0) {
            for (int i = size - 1; i >= 0; i--) {
                mRunnableList.get(i).run();
                mRunnableList.remove(i);
            }
        }else {
            mViewRoot.scheduleTraversal();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        quit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
        mViewRoot.dispatchTouchEvent(motionEvent);
        motionEvent.recycle();
        return true;
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
    }

    private void quit() {

        if (mViewRoot != null) {
            mViewRoot.release();
        }
    }

    public static class ViewRenderBuffer extends SLRenderBuffer {

        public static final boolean debug = true;
        public static final String TAG = ViewRenderBuffer.class.getCanonicalName();
        private AndroidCanvas mCanvas = new AndroidCanvas();

        @Override
        public SLCanvas lockCanvas() {
            return mCanvas;
        }

        @Override
        protected void handUnlockCanvasAndPost(SLCanvas canvas) {
            mCanvas.setCanvas(null);
        }

        @Override
        protected void onDestroy() {

        }

        public void setCanvas(Canvas canvas) {
            mCanvas.setCanvas(canvas);
        }

        static void logI(String log) {
            Log.i(TAG, log);
        }

    }
}
