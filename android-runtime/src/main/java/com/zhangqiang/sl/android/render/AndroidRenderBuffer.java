package com.zhangqiang.sl.android.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.zhangqiang.sl.android.render.canvas.AndroidCanvas;
import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.render.SLRenderBuffer;

import java.nio.ByteBuffer;

public class AndroidRenderBuffer extends SLRenderBuffer {

    public static final String TAG = AndroidRenderBuffer.class.getCanonicalName();
    private final Bitmap mRenderBitmap;
    private final SLCanvas mCanvas;
    private final Bitmap mFlushBitmap;
    private final ByteBuffer mBitmapBuffer;
    private boolean mMultiThread;

    public AndroidRenderBuffer(int width, int height,boolean multiThread) {
        mMultiThread = multiThread;
        mRenderBitmap = BitmapPool.getBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new AndroidCanvas(new Canvas(mRenderBitmap));
        if (mMultiThread) {
            mFlushBitmap = mRenderBitmap.copy(Bitmap.Config.ARGB_8888, true);
            mBitmapBuffer = ByteBuffer.allocate(mRenderBitmap.getByteCount());
        }else {
            mFlushBitmap = null;
            mBitmapBuffer = null;
        }
    }

    @Override
    public SLCanvas lockCanvas() {
        return mCanvas;
    }

    @Override
    protected void handUnlockCanvasAndPost(SLCanvas canvas) {
        if (!mMultiThread) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        mBitmapBuffer.clear();
        mRenderBitmap.copyPixelsToBuffer(mBitmapBuffer);
        SLContext.getLogger().logI(TAG, "==unlockCanvasAndPost=====1======" + (System.currentTimeMillis() - currentTimeMillis));
        currentTimeMillis = System.currentTimeMillis();
        mBitmapBuffer.position(0);
        mFlushBitmap.copyPixelsFromBuffer(mBitmapBuffer);
        SLContext.getLogger().logI(TAG, "===unlockCanvasAndPost====2======" + (System.currentTimeMillis() - currentTimeMillis));
    }

    @Override
    protected void onDestroy() {
        if (mBitmapBuffer != null) {
            mBitmapBuffer.clear();
        }
        if (mFlushBitmap != null) {
            mFlushBitmap.recycle();
        }
        mRenderBitmap.recycle();
    }

    public void flush(Canvas canvas) {
        if (mMultiThread) {
            canvas.drawBitmap(mFlushBitmap, 0, 0, null);
        }else {
            canvas.drawBitmap(mRenderBitmap,0,0,null);
        }
    }

    public Bitmap getBitmap() {
        if (mMultiThread) {
            return mFlushBitmap;
        }else {
            return mFlushBitmap;
        }
    }
}
