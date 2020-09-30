package com.zhangqiang.sl.framework.gesture;

public final class SLMotionEvent {

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_MOVE = 1;
    public static final int ACTION_UP = 2;
    public static final int ACTION_CANCEL = 3;

    private static final Object poolSync = new Object();
    private static SLMotionEvent pool;
    private SLMotionEvent next;
    private float x, y;
    private int action;

    private SLMotionEvent() {
    }

    public static SLMotionEvent obtain() {
        synchronized (poolSync) {

            if (pool == null) {
                return new SLMotionEvent();
            }
            final SLMotionEvent event = pool;
            pool = pool.next;
            event.next = null;
            return event;
        }
    }

    public void recycle() {
        synchronized (poolSync) {
            x = y = 0;
            next = pool;
            pool = this;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getAction() {
        return action;
    }

    public void offset(float xOffset,float yOffset){
        x += xOffset;
        y += yOffset;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
