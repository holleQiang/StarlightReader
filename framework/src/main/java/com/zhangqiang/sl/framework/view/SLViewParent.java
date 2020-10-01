package com.zhangqiang.sl.framework.view;

public interface SLViewParent {

    void requestLayout();

    void invalidateChild(SLView child);

    SLViewParent invalidateChildInParent();

    boolean isLayoutRequested();
}
