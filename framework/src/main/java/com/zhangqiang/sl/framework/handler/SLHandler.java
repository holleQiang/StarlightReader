package com.zhangqiang.sl.framework.handler;

public abstract class SLHandler {

    private Callback mCallback;

    public SLHandler(Callback callback) {
        this.mCallback = callback;
    }

    public SLHandler() {
    }

    public abstract void post(Runnable runnable);

    public abstract void postDelayed(Runnable runnable, long delayMillions);

    public abstract void removeCallbacks(Runnable runnable);

    public abstract void removeCallbacksAndMessages();

    public void sendMessage(SLMessage message) {
        message.setTarget(this);
        onSendMessage(message);
        message.markInUse();
    }

    protected abstract void onSendMessage(SLMessage message);

    public void removeMessage(SLMessage message) {
        onRemoveMessage(message);
    }

    protected abstract void onRemoveMessage(SLMessage message);


    public void dispatchMessage(SLMessage message) {
        if (message.callback != null) {
            message.callback.run();
        } else {
            boolean handed = false;
            if (mCallback != null) {
                handed = mCallback.handMessage(message);
            }
            if (!handed) {
                handMessage(message);
            }
        }
        message.recycleUnChecked();
    }

    public void handMessage(SLMessage message) {

    }

    public SLMessage obtainMessage(int what) {
        SLMessage message = SLMessage.obtain(what);
        message.setTarget(this);
        return message;
    }

    public void quit() {
        onQuit();
    }

    protected abstract void onQuit();

    public interface Callback {
        boolean handMessage(SLMessage message);
    }


}
