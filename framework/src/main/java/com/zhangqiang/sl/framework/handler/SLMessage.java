package com.zhangqiang.sl.framework.handler;

public class SLMessage {

    public int what;
    public Object obj;
    public Runnable callback;
    private SLMessage next;
    private static SLMessage mPool;
    private static final Object mPoolLock = new Object();
    private static int mPoolSize;
    private static final int MAX_POOL_SIZE = 50;
    private static final int FLAG_MARK_IN_USE = 1 << 1;
    private int mFlags;
    private SLHandler target;

    public static SLMessage obtain() {

        synchronized (mPoolLock) {
            if (mPool != null) {

                SLMessage msg = mPool;
                mPool = msg.next;
                msg.next = null;
                msg.mFlags = 0;
                return msg;
            }
        }
        return new SLMessage();
    }

    public static SLMessage obtain(int what) {
        SLMessage message = obtain();
        message.what = what;
        return message;
    }

    public void recycle() {

        if (isInUse()) {
            throw new IllegalStateException("message is already in use");
        }

        recycleUnChecked();
    }

    void markInUse() {

        mFlags |= FLAG_MARK_IN_USE;
    }

    boolean isInUse() {
        return (mFlags & FLAG_MARK_IN_USE) == FLAG_MARK_IN_USE;
    }

    public SLHandler getTarget() {
        return target;
    }

    public void setTarget(SLHandler target) {
        this.target = target;
    }

    public void sendToTarget() {
        if (target != null) {
            target.sendMessage(this);
        }
    }

    void recycleUnChecked() {
        mFlags = FLAG_MARK_IN_USE;
        what = 0;
        obj = null;
        callback = null;

        synchronized (mPoolLock) {

            if (mPoolSize < MAX_POOL_SIZE) {
                next = mPool;
                mPool = this;
                mPoolSize++;
            }
        }
    }
}
