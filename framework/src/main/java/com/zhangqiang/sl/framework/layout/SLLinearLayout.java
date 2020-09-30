package com.zhangqiang.sl.framework.layout;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.view.MeasureOptions;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

public class SLLinearLayout extends SLViewGroup {

    public SLLinearLayout(SLContext context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int l = 0;
        int t = 0;
        int r;
        int b;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            SLView child = getChildAt(i);
            r = l + child.getMeasuredWidth();
            b = t + child.getMeasuredHeight();
            child.layout(l, t, r, b);
            t = b;
        }
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        int width = 0;
        int height = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            SLView child = getChildAt(i);
            measureChild(widthOptions, 0,heightOptions,height, child);
            width = Math.max(child.getMeasuredWidth(), width);
            height += child.getMeasuredHeight();
        }
        setMeasuredResult(resolveSizeAndState(width, widthOptions), resolveSizeAndState(height, heightOptions));
    }
}
