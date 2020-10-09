package com.zhangqiang.sl.framework.layout;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.view.MeasureOptions;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

public class SLLinearLayout extends SLViewGroup {

    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;
    private int mOrientation = ORIENTATION_VERTICAL;

    public SLLinearLayout(SLContext context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int l = 0;
        int t = 0;
        int r;
        int b;
        if (mOrientation == ORIENTATION_VERTICAL) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                SLView child = getChildAt(i);
                r = l + child.getMeasuredWidth();
                b = t + child.getMeasuredHeight();
                child.layout(l, t, r, b);
                t = b;
            }
        } else if (mOrientation == ORIENTATION_HORIZONTAL) {

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                SLView child = getChildAt(i);
                r = l + child.getMeasuredWidth();
                b = t + child.getMeasuredHeight();
                child.layout(l, t, r, b);
                l = r;
            }
        }
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        if (mOrientation == ORIENTATION_VERTICAL) {

            measureChildWhenVertical(widthOptions, heightOptions);
        } else if (mOrientation == ORIENTATION_HORIZONTAL) {

            measureChildWhenHorizontal(widthOptions, heightOptions);
        }

    }


    private void measureChildWhenVertical(int widthOptions, int heightOptions) {
        int width = 0;
        int height = 0;
        int childCount = getChildCount();
        int noWeightHeight = 0;
        float totalWeight = 0;
        for (int i = 0; i < childCount; i++) {
            SLView child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams.weight <= 0) {
                measureChild(widthOptions, 0, heightOptions, noWeightHeight, child);
                noWeightHeight += child.getMeasuredHeight();
                width = Math.max(width, child.getMeasuredWidth());
            } else {
                totalWeight += layoutParams.weight;
            }
        }
        int heightSize = MeasureOptions.getSize(heightOptions);
        int heightMode = MeasureOptions.getMode(heightOptions);
        if (heightSize > 0 && heightMode == MeasureOptions.MODE_EXACTLY && totalWeight > 0) {
            float unitWeightHeight = (float)(heightSize - noWeightHeight) / totalWeight;
            for (int i = 0; i < childCount; i++) {
                SLView child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                float weight = layoutParams.weight;
                if (weight > 0) {
                    int cHeight = (int) (unitWeightHeight * weight);
                    measureChild(widthOptions, 0, MeasureOptions.make(cHeight, MeasureOptions.MODE_EXACTLY), 0, child);
                    width = Math.max(width, child.getMeasuredWidth());
                }
            }
            height = heightSize;
        } else {
            height = noWeightHeight;
        }
        setMeasuredResult(resolveSizeAndState(width, widthOptions), resolveSizeAndState(height, heightOptions));
    }

    private void measureChildWhenHorizontal(int widthOptions, int heightOptions) {

        int width;
        int height = 0;
        int childCount = getChildCount();
        int noWeightWidth = 0;
        float totalWeight = 0;
        for (int i = 0; i < childCount; i++) {
            SLView child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams.weight <= 0) {
                measureChild(widthOptions, noWeightWidth, heightOptions, 0, child);
                noWeightWidth += child.getMeasuredWidth();
                height = Math.max(height, child.getMeasuredHeight());
            } else {
                totalWeight += layoutParams.weight;
            }
        }
        int widthSize = MeasureOptions.getSize(widthOptions);
        int widthMode = MeasureOptions.getMode(heightOptions);
        if (widthSize > 0 && widthMode == MeasureOptions.MODE_EXACTLY && totalWeight > 0) {
            float unitWeightWidth = (widthSize - noWeightWidth) / totalWeight;
            for (int i = 0; i < childCount; i++) {
                SLView child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                float weight = layoutParams.weight;
                if (weight > 0) {
                    int cWidth = (int) (unitWeightWidth * weight);
                    measureChild(MeasureOptions.make(cWidth,MeasureOptions.MODE_EXACTLY), 0, heightOptions, 0, child);
                    height = Math.max(height, child.getMeasuredHeight());
                }
            }
            width = widthSize;
        }else {
            width = noWeightWidth;
        }
        setMeasuredResult(resolveSizeAndState(width, widthOptions), resolveSizeAndState(height, heightOptions));
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT, SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT);
    }

    public static class LayoutParams extends SLViewGroup.LayoutParams {

        private float weight;

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        if (mOrientation != orientation) {
            this.mOrientation = orientation;
            requestLayout();
        }
    }
}
