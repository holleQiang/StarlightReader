package com.zhangqiang.sl.framework.graphic;

public class SLRect {

    private int left, top, right, bottom;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public void set(int l, int t, int r, int b) {
        setLeft(l);
        setTop(t);
        setRight(r);
        setBottom(b);
    }

    public void set(SLRect rect) {
        set(rect.left, rect.top, rect.right, rect.bottom);
    }

    public boolean isEmpty() {
        return getWidth() <= 0 || getHeight() <= 0;
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }
}
