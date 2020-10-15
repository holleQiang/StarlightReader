package com.zhangqiang.sl.framework.gesture;

public abstract class SLMotionEvent {

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_MOVE = 1;
    public static final int ACTION_UP = 2;
    public static final int ACTION_CANCEL = 3;
    public static final int ACTION_POINTER_DOWN = 4;
    public static final int ACTION_POINTER_UP = 5;
    public static final int ACTION_MASK = 0xFF;
    public static final int POINTER_INDEX_MASK = 0xFF00;
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;


    public abstract float getX();

    public abstract float getY();

    public abstract int getAction();

    public abstract void offset(float xOffset,float yOffset);

    public int getActionMasked(){
        return getAction() & ACTION_MASK;
    }

    public abstract int getPointerCount();

    public abstract  int getPointerId(int pointerIndex);

    public abstract int findPointerIndex(int pointerId);

    public abstract int getActionIndex();

    public abstract void setAction(int action);

    public abstract float getX(int pointerIndex);

    public abstract float getY(int pointerIndex);
}
