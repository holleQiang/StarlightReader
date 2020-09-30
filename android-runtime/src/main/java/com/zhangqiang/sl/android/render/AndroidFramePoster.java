package com.zhangqiang.sl.android.render;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Choreographer;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.render.SLFramePoster;

import java.util.ArrayList;
import java.util.List;

public class AndroidFramePoster extends SLFramePoster {

    private static final boolean debug = false;
    private static final String TAG = AndroidFramePoster.class.getCanonicalName();
    private final List<Runnable> mRunnableList = new ArrayList<>();

    public AndroidFramePoster() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostFrameCallback(final Runnable runnable) {
        if (debug) {
            SLContext.getLogger().logI(TAG,"=======onPostFrameCallback==========");
        }
        synchronized (mRunnableList) {
            mRunnableList.add(runnable);
            Choreographer.getInstance().postFrameCallback(mFrameCallback);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onRemoveFrameCallback(Runnable runnable) {
        if (debug) {
            SLContext.getLogger().logI(TAG,"=======onRemoveFrameCallback==========");
        }
        synchronized (mRunnableList) {
            mRunnableList.remove(runnable);
            if (mRunnableList.size() <= 0) {
                Choreographer.getInstance().removeFrameCallback(mFrameCallback);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private final Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            if (debug) {
                SLContext.getLogger().logI(TAG,"=======doFrame==========" + Thread.currentThread().getName());
            }
            synchronized (mRunnableList) {

                for (int i = mRunnableList.size() - 1; i >= 0; i--) {
                    Runnable runnable = mRunnableList.get(i);
                    runnable.run();
                    mRunnableList.remove(i);
                }
            }

        }
    };
}
