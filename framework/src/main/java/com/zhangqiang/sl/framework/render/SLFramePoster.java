package com.zhangqiang.sl.framework.render;

import java.util.ArrayList;
import java.util.List;

public abstract class SLFramePoster {

    private List<PosterListener> mPosterListener;

    protected abstract void onPostFrameCallback(Runnable runnable);

    protected abstract void onRemoveFrameCallback(Runnable runnable);

    public void postFrameCallback(Runnable runnable) {
        onPostFrameCallback(runnable);
        if (mPosterListener != null) {
            for (int i = mPosterListener.size() - 1; i >= 0; i--) {
                mPosterListener.get(i).onPostCallback();
            }
        }
    }

    public void removeFrameCallback(Runnable runnable) {
        onRemoveFrameCallback(runnable);
        if (mPosterListener != null) {
            for (int i = mPosterListener.size() - 1; i >= 0; i--) {
                mPosterListener.get(i).onRemoveCallback();
            }
        }
    }

    public interface PosterListener{

        void onPostCallback();

        void onRemoveCallback();
    }

    public void addPosterListener(PosterListener listener){
        if (mPosterListener == null) {
            mPosterListener = new ArrayList<>();
        }
        if (mPosterListener.contains(listener)) {
            return;
        }
        mPosterListener.add(listener);
    }

    public void removePosterListener(PosterListener listener){
        if (mPosterListener == null) {
            return;
        }
        mPosterListener.remove(listener);
    }
}
