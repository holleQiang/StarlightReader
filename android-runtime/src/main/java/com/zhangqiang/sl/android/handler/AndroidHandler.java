package com.zhangqiang.sl.android.handler;

import android.os.Handler;
import android.os.Message;

import com.zhangqiang.sl.framework.handler.SLHandler;
import com.zhangqiang.sl.framework.handler.SLMessage;

public class AndroidHandler extends SLHandler {

    private Handler handler;

    public AndroidHandler(Handler handler) {
        this(null, handler);
    }

    public AndroidHandler(Callback callback, Handler handler) {
        super(callback);
        this.handler = new Handler(handler.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AndroidHandler.this.dispatchMessage((SLMessage) msg.obj);
            }
        };
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public void postDelayed(Runnable runnable, long delayMillions) {
        handler.postDelayed(runnable, delayMillions);
    }

    @Override
    public void removeCallbacks(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void removeCallbacksAndMessages() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onSendMessage(SLMessage message) {
        Message obtain = Message.obtain();
        obtain.what = message.what;
        obtain.obj = message;
        handler.sendMessage(obtain);
    }

    @Override
    protected void onRemoveMessage(SLMessage message) {
        handler.removeMessages(message.what);
    }

    @Override
    protected void onQuit() {
        handler.getLooper().quit();
    }

    public Handler getHandler() {
        return handler;
    }
}
