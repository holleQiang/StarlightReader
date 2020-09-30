package com.zhangqiang.sl.framework.layout;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.view.MeasureOptions;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

public class SLFrameLayout extends SLViewGroup {

    public SLFrameLayout(SLContext context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            SLView child = getChildAt(i);
            child.layout(left, top, left + child.getWidth(), top + child.getHeight());
        }
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        int maxWidth = 0, maxHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            SLView child = getChildAt(i);
            measureChild(widthOptions, heightOptions, child);
            maxWidth = Math.max(child.getMeasuredWidth(), maxWidth);
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
        }
        setMeasuredResult(resolveSizeAndState(maxWidth, widthOptions), resolveSizeAndState(maxHeight, heightOptions));
    }
}
