package com.zhangqiang.sl.android.event;

import android.view.MotionEvent;

import com.zhangqiang.sl.framework.gesture.SLMotionEvent;

public class AndroidMotionEvent extends SLMotionEvent {

    private MotionEvent event;
    private static AndroidMotionEvent sPool;
    private AndroidMotionEvent next;

    private AndroidMotionEvent() {
    }

    public static AndroidMotionEvent obtain(MotionEvent event) {
        AndroidMotionEvent result;
        if (sPool == null) {
            result = new AndroidMotionEvent();
        } else {
            result = sPool;
            sPool = result.next;
        }
        result.event = event;
        return result;
    }

    public void recycle() {
        next = sPool;
        sPool = this;
        event = null;
    }

    @Override
    public float getX() {
        return event.getX();
    }

    @Override
    public float getY() {
        return event.getY();
    }

    @Override
    public int getAction() {
        int resultAction;
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                resultAction = ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                resultAction = ACTION_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                resultAction = ACTION_UP;
                break;
            case MotionEvent.ACTION_CANCEL:
                resultAction = ACTION_CANCEL;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                resultAction = ACTION_POINTER_DOWN;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                resultAction = ACTION_POINTER_UP;
                break;
            default:
                resultAction = -1;
        }
        return resultAction | ((actionIndex << ACTION_POINTER_INDEX_SHIFT) & POINTER_INDEX_MASK);
    }

    @Override
    public void offset(float xOffset, float yOffset) {
        event.offsetLocation(xOffset, yOffset);
    }

    @Override
    public int getPointerCount() {
        return event.getPointerCount();
    }

    @Override
    public int getPointerId(int pointerIndex) {
        return event.getPointerId(pointerIndex);
    }

    @Override
    public int findPointerIndex(int pointerId) {
        return event.findPointerIndex(pointerId);
    }

    @Override
    public int getActionIndex() {
        return (getAction() & POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;
    }

    @Override
    public void setAction(int action) {
        switch (action) {
            case ACTION_DOWN:
                event.setAction(MotionEvent.ACTION_DOWN);
                break;
            case ACTION_MOVE:
                event.setAction(MotionEvent.ACTION_MOVE);
                break;
            case ACTION_UP:
                event.setAction(MotionEvent.ACTION_UP);
                break;
            case ACTION_CANCEL:
                event.setAction(MotionEvent.ACTION_CANCEL);
                break;
            case ACTION_POINTER_DOWN:
                event.setAction(MotionEvent.ACTION_POINTER_DOWN);
                break;
            case ACTION_POINTER_UP:
                event.setAction(MotionEvent.ACTION_POINTER_UP);
                break;
        }
    }

    @Override
    public float getX(int pointerIndex) {
        return event.getX(pointerIndex);
    }

    @Override
    public float getY(int pointerIndex) {
        return event.getY(pointerIndex);
    }
}
