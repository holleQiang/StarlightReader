package com.zhangqiang.sl.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glview.GLView;
import com.zhangqiang.sl.android.render.AndroidFramePoster;
import com.zhangqiang.sl.android.render.canvas.AndroidCanvas;
import com.zhangqiang.sl.android.render.canvas.AndroidGLCanvas;
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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AndroidGLSLView extends GLView implements ISLView{

    private static final int MSG_MOTION_EVENT = 1;
    private SLViewRoot mViewRoot;
    private SLView mTempContentView;
    private SLRootView mRootView;
    private SLHandler mHandler;
    private Runnable mRunnable;

    public AndroidGLSLView(Context context) {
        super(context);
        init(context);
    }

    public AndroidGLSLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        mViewRoot = new SLViewRoot(new AndroidContext(context, true),
                new SLRenderBufferFactory() {
                    @Override
                    public SLRenderBuffer create(int width, int height) {
                        return new GLRenderBuffer();
                    }
                },
                new FramePosterFactory() {
                    @Override
                    protected SLFramePoster onCreateFramePoster() {
                        return new SLFramePoster() {

                            @Override
                            protected void onPostFrameCallback(Runnable runnable) {
                                mRunnable = runnable;
                                requestRender();
                            }

                            @Override
                            protected void onRemoveFrameCallback(Runnable runnable) {

                            }
                        };
                    }
                });
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                quit();
            }
        });
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas) {
        ((GLRenderBuffer) mViewRoot.getRenderBuffer()).setCanvas(canvas);

        if (mRunnable != null) {
            mHandler.post(mRunnable);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouchEvent(event);
        return true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        quit();
        start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        quit();
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

    private class GLRenderBuffer extends SLRenderBuffer {

        AndroidGLCanvas mCanvas = new AndroidGLCanvas();
        private final Object mCanvasLock = new Object();
        private final Object mRenderLock = new Object();


        @Override
        public SLCanvas lockCanvas() {

            requestRender();
            synchronized (mCanvasLock) {

                if (mCanvas.getCanvasGL() != null) {
                    return mCanvas;
                }

                try {
                    mCanvasLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return mCanvas;
        }

        @Override
        protected void handUnlockCanvasAndPost(SLCanvas canvas) {
            synchronized (mCanvasLock) {
                mCanvas.setCanvasGL(null);
            }
            synchronized (mRenderLock) {
                mRenderLock.notifyAll();
            }
        }

        @Override
        protected void onDestroy() {

        }

        public void setCanvas(ICanvasGL canvas) {
            synchronized (mCanvasLock) {

                mCanvas.setCanvasGL(canvas);
                mCanvasLock.notifyAll();
            }
//            synchronized (mRenderLock) {
//                try {
//                    mRenderLock.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
