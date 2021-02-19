package com.zhangqiang.sl.framework.view;


import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.gesture.SLMotionEvent;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.handler.SLHandler;
import com.zhangqiang.sl.framework.render.SLFramePoster;
import com.zhangqiang.sl.framework.render.SLRenderBuffer;

public class SLViewRoot implements SLViewParent {

    private static boolean debug = true;
    private static final String TAG = SLViewRoot.class.getCanonicalName();

    private SLView mRootView;
    private SLAttachInfo mAttachInfo;
    private boolean mScheduleTraversal;
    private boolean mLayoutRequested;
    private SLHandler mHandler;
    private SLRenderBuffer mRenderBuffer;
    private SLFramePoster mFramePoster;
    private int frameWidth, frameHeight;
    private SLContext mContext;
    private SLRenderBufferFactory mRenderBufferFactory;
    private FramePosterFactory mFramePosterFactory;

    public SLViewRoot(SLContext context, SLRenderBufferFactory renderBufferFactory, FramePosterFactory framePosterFactory) {
        this.mContext = context;
        mRenderBufferFactory = renderBufferFactory;
        mFramePosterFactory = framePosterFactory;
    }

    public void setView(SLView rootView, int frameWidth, int frameHeight) {
        if (rootView == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            if (mRootView == null) {
                mRootView = rootView;
                mRootView.setParent(this);

                this.frameWidth = frameWidth;
                this.frameHeight = frameHeight;

                mRenderBuffer = mRenderBufferFactory.create(frameWidth, frameHeight);

                mHandler = mContext.createHandler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        scheduleTraversal();
                    }
                });
            }
        }
    }

    public void scheduleTraversal() {
        synchronized (this) {
            if (mScheduleTraversal) {
                return;
            }
            mScheduleTraversal = true;
            if (mFramePoster == null) {
                mFramePoster = mFramePosterFactory.create();
            }
            mFramePoster.postFrameCallback(mTraversalRunnable);
        }
    }

    private void unScheduleTraversal() {
        if (mScheduleTraversal) {
            mScheduleTraversal = false;
            if (mFramePoster != null) {
                mFramePoster.removeFrameCallback(mTraversalRunnable);
            }
        }
    }

    public void doTraversal() {

        mScheduleTraversal = false;

        long currentTimeMillis = System.currentTimeMillis();
        if (debug) {
            logI("开始遍历");
        }

        if (mAttachInfo == null) {
            mAttachInfo = new SLAttachInfo(this);
            mAttachInfo.setHandler(mHandler);
            mRootView.dispatchAttachToWindow(mAttachInfo);
            mLayoutRequested = true;
        }

        if (mLayoutRequested) {
            mLayoutRequested = false;
            doTraversalMeasure();
            doTraversalLayout();
        }
        doTraversalDraw();

        if (debug) {
            logI("遍历完成:" + (System.currentTimeMillis() - currentTimeMillis));
        }
    }

    @Override
    public void requestLayout() {
        if (!getContext().isRenderThread()) {
            throw new RuntimeException("cannot run this method beyond render thread");
        }
        mLayoutRequested = true;
        scheduleTraversal();
    }

    @Override
    public void invalidateChild(SLView child) {
        invalidateChildInParent();
    }

    @Override
    public SLViewParent invalidateChildInParent() {
        if (debug) {
            logI("====invalidateChild======mScheduleTraversal=" + mScheduleTraversal);
        }
        if (!getContext().isRenderThread()) {
            throw new RuntimeException("cannot run this method beyond render thread");
        }
        scheduleTraversal();
        return null;
    }

    @Override
    public boolean isLayoutRequested() {
        return mLayoutRequested;
    }

    private void doTraversalMeasure() {
        if (debug) {
            logI("doTraversalMeasure");
        }
        mRootView.measure(MeasureOptions.make(frameWidth, MeasureOptions.MODE_EXACTLY),
                MeasureOptions.make(frameHeight, MeasureOptions.MODE_EXACTLY));
    }

    private void doTraversalLayout() {
        if (debug) {
            logI("doTraversalLayout");
        }
        int measuredWidth = mRootView.getMeasuredWidth();
        int measuredHeight = mRootView.getMeasuredHeight();
        mRootView.layout(0, 0, measuredWidth, measuredHeight);
    }

    private void doTraversalDraw() {
        long currentTimeMillis = System.currentTimeMillis();
        if (debug) {
            logI("doTraversalDraw----start");
        }
        SLCanvas canvas = mRenderBuffer.lockCanvas();
        if (debug) {
            logI("doTraversalDraw----lockCanvas:" + (System.currentTimeMillis() - currentTimeMillis));
        }
        currentTimeMillis = System.currentTimeMillis();
        mRootView.draw(canvas);
        if (debug) {
            logI("doTraversalDraw----rooView draw:" + (System.currentTimeMillis() - currentTimeMillis));
        }
        currentTimeMillis = System.currentTimeMillis();
        mRenderBuffer.unlockCanvasAndPost(canvas);
        if (debug) {
            logI("doTraversalDraw----unlockCanvasAndPost" + (System.currentTimeMillis() - currentTimeMillis));
        }
    }

    public void release() {
        synchronized (this) {
            if (mRootView != null) {
                mRootView.setParent(null);
                mRootView.dispatchDetachFromWindow();
                mRootView = null;
            }
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages();
                mHandler = null;
            }
            unScheduleTraversal();
            if (mRenderBuffer != null) {
                mRenderBuffer.destroy();
                mRenderBuffer = null;
            }
            mAttachInfo = null;
        }
    }

    private final Runnable mTraversalRunnable = new Runnable() {
        @Override
        public void run() {

            performTraversal();
        }
    };

    private void performTraversal() {

        if (!mScheduleTraversal) {
            return;
        }

        doTraversal();

    }

    public void dispatchTouchEvent(SLMotionEvent motionEvent) {
        mRootView.dispatchTouchEvent(motionEvent);
    }

    public SLView getRootView() {
        return mRootView;
    }

    private void logI(String log) {
        SLContext.getLogger().logI(TAG, log);
    }

    public SLRenderBuffer getRenderBuffer() {
        return mRenderBuffer;
    }

    public SLContext getContext() {
        return mContext;
    }

}
