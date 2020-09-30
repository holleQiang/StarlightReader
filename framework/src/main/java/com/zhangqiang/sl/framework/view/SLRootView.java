package com.zhangqiang.sl.framework.view;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.layout.SLLinearLayout;

public class SLRootView extends SLLinearLayout {

    private int width, height;
    private SLView mContentView;

    public SLRootView(SLContext context) {
        super(context);
    }

    public void setContentView(SLView contentView) {
        if (mContentView != null) {
            removeView(mContentView);
            mContentView = null;
        }
        this.mContentView = contentView;
        if (contentView != null) {
            addView(contentView);
        }
    }
}
