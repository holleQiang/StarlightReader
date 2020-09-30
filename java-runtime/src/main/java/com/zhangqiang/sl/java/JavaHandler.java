package com.zhangqiang.sl.java;

import com.zhangqiang.sl.framework.handler.SLHandler;
import com.zhangqiang.sl.framework.handler.SLMessage;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class JavaHandler extends SLHandler {

    private static final BlockingDeque<SLMessage> messageQueue = new LinkedBlockingDeque<>();

    static {
        Thread mThread = new Thread(){
            @Override
            public void run() {
                super.run();
                for(;;){
                    try {
                        SLMessage slMessage = messageQueue.take();
                        slMessage.getTarget().dispatchMessage(slMessage);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
    }

    @Override
    public void post(Runnable runnable) {
        SLMessage obtain = SLMessage.obtain();
        obtain.callback = runnable;
        sendMessage(obtain);
    }

    @Override
    public void postDelayed(Runnable runnable, long delayMillions) {

    }

    @Override
    public void removeCallbacks(Runnable runnable) {
        for (SLMessage slMessage : messageQueue) {
            if (slMessage.callback == runnable) {
                messageQueue.remove(slMessage);
            }
        }
    }

    @Override
    public void removeCallbacksAndMessages() {
        messageQueue.clear();
    }

    @Override
    protected void onSendMessage(SLMessage message) {
        messageQueue.offerLast(message);
    }

    @Override
    protected void onRemoveMessage(SLMessage message) {
        messageQueue.remove(message);
    }

    @Override
    protected void onQuit() {
    }
}
