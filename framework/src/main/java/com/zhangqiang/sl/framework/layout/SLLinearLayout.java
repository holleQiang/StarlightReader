package com.zhangqiang.sl.framework.layout;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.view.Gravity;
import com.zhangqiang.sl.framework.view.MeasureOptions;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

public class SLLinearLayout extends SLViewGroup {

    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;
    private int mOrientation = ORIENTATION_VERTICAL;
    private int mTotalLength;

    public SLLinearLayout(SLContext context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int l;
        int t;
        int r;
        int b;
        if (mOrientation == ORIENTATION_VERTICAL) {
            t = getPaddingTop();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                SLView child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                int gravity = layoutParams.gravity;
                int measuredWidth = child.getMeasuredWidth();
                if ((gravity & Gravity.START) != 0) {
                    l = getPaddingLeft();
                    r = l + measuredWidth;
                }else if((gravity & Gravity.END) != 0){
                    r = getWidth() - getPaddingRight();
                    l = r - measuredWidth;
                }else if((gravity & Gravity.CENTER_HORIZONTAL) != 0){
                    l = (getWidth() - getPaddingLeft() - getPaddingRight() - measuredWidth)/2 + getPaddingLeft();
                    r = l + measuredWidth;
                }else {
                    l = getPaddingLeft();
                    r = l + measuredWidth;
                }
                b = t + child.getMeasuredHeight();
                child.layout(l, t, r, b);
                t = b;
            }
        } else if (mOrientation == ORIENTATION_HORIZONTAL) {

            l = getPaddingLeft();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                SLView child = getChildAt(i);
                int measuredHeight = child.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                int gravity = layoutParams.gravity;
                if((gravity & Gravity.TOP) != 0){
                    t = getPaddingTop();
                    b = t + measuredHeight;
                }else if((gravity & Gravity.BOTTOM) != 0){

                    t = getHeight() - getPaddingBottom() - measuredHeight;
                    b = t + measuredHeight;
                }else if((gravity & Gravity.CENTER_VERTICAL) != 0){
                    t = (getHeight() - getPaddingTop() - getPaddingBottom() - measuredHeight)/2 + getPaddingTop();
                    b = t + measuredHeight;
                }else {
                    t = getPaddingTop();
                    b = t + measuredHeight;
                }
                r = l + child.getMeasuredWidth();
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
        int heightMode = MeasureOptions.getMode(heightOptions);
        int heightSize = MeasureOptions.getSize(heightOptions);

        int width = 0;
        int childCount = getChildCount();
        mTotalLength = 0;
        float totalWeight = 0;
        boolean skipMeasure = false;
        for (int i = 0; i < childCount; i++) {
            SLView child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            float weight = layoutParams.weight;
            if (weight < 0) {
                throw new IllegalArgumentException("illegal weight:" + weight);
            }
            totalWeight += weight;

            boolean useExcessSpace = layoutParams.height == 0 && layoutParams.weight > 0;
            if (heightMode == MeasureOptions.MODE_EXACTLY && useExcessSpace) {
                skipMeasure = true;
            } else {

                if (useExcessSpace) {
                    layoutParams.height = SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT;
                }

                int heightUsed = mTotalLength;
                measureChild(widthOptions, 0, heightOptions, heightUsed, child);
                if (useExcessSpace) {
                    layoutParams.height = 0;
                }
                mTotalLength += child.getMeasuredHeight();
                width = Math.max(width, child.getMeasuredWidth());
            }
        }
        mTotalLength = mTotalLength + getPaddingTop() + getPaddingBottom();
        int heightResolveSize = resolveSizeAndState(mTotalLength,heightOptions);
        int remainExcess = heightSize - mTotalLength;
        if (skipMeasure || totalWeight > 0 && remainExcess != 0) {

            mTotalLength = 0;

            for (int i = 0; i < childCount; i++) {
                SLView child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                float weight = layoutParams.weight;

                if (weight > 0) {

                    int share = (int) (remainExcess * weight / totalWeight);
                    int childHeight = 0;
                    if (layoutParams.height == 0 && heightMode == MeasureOptions.MODE_EXACTLY) {
                        childHeight = share;
                    } else {
                        childHeight = child.getMeasuredHeight() + share;
                    }

                    int widthMeasureOptions = makeChildMeasureOptions(widthOptions, getPaddingLeft() + getPaddingTop(), layoutParams.width);
                    int heightMeasureOptions = MeasureOptions.make(Math.max(0, childHeight), MeasureOptions.MODE_EXACTLY);
                    child.measure(widthMeasureOptions, heightMeasureOptions);
                    width = Math.max(width, child.getMeasuredWidth());
                }

                mTotalLength += child.getMeasuredHeight();
            }
            mTotalLength += getPaddingTop() + getPaddingBottom();
        }
        width += getPaddingLeft() + getPaddingRight();
        setMeasuredResult(resolveSizeAndState(width, widthOptions), heightResolveSize);
    }

    private void measureChildWhenHorizontal(int widthOptions, int heightOptions) {

        int widthMode = MeasureOptions.getMode(widthOptions);
        int widthSize = MeasureOptions.getSize(widthOptions);

        int height = 0;
        int childCount = getChildCount();
        mTotalLength = 0;
        float totalWeight = 0;
        boolean skipMeasure = false;
        for (int i = 0; i < childCount; i++) {
            SLView child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            float weight = layoutParams.weight;
            if (weight < 0) {
                throw new IllegalArgumentException("illegal weight:" + weight);
            }
            totalWeight += weight;

            boolean useExcessSpace = layoutParams.width == 0 && layoutParams.weight > 0;
            if (widthMode == MeasureOptions.MODE_EXACTLY && useExcessSpace) {
                skipMeasure = true;
            } else {

                if (useExcessSpace) {
                    layoutParams.width = SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT;
                }

                int widthUsed = mTotalLength;
                measureChild(widthOptions, widthUsed, heightOptions, 0, child);
                if (useExcessSpace) {
                    layoutParams.width = 0;
                }
                mTotalLength += child.getMeasuredWidth();
                height = Math.max(height, child.getMeasuredHeight());
            }
        }
        mTotalLength = mTotalLength + getPaddingLeft() + getPaddingRight();
        int widthResolveSize = resolveSizeAndState(mTotalLength,widthOptions);
        int remainExcess = widthSize - mTotalLength;
        if (skipMeasure || totalWeight > 0 && remainExcess != 0) {

            mTotalLength = 0;

            for (int i = 0; i < childCount; i++) {
                SLView child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                float weight = layoutParams.weight;

                if (weight > 0) {

                    int share = (int) (remainExcess * weight / totalWeight);
                    int childWidth = 0;
                    if (layoutParams.width == 0 && widthMode == MeasureOptions.MODE_EXACTLY) {
                        childWidth = share;
                    } else {
                        childWidth = child.getMeasuredWidth() + share;
                    }

                    int widthMeasureOptions = MeasureOptions.make(Math.max(0, childWidth), MeasureOptions.MODE_EXACTLY);
                    int heightMeasureOptions = makeChildMeasureOptions(heightOptions, getPaddingTop() + getPaddingBottom(), layoutParams.height);
                    child.measure(widthMeasureOptions, heightMeasureOptions);
                    height = Math.max(height, child.getMeasuredHeight());
                }

                mTotalLength += child.getMeasuredWidth();
            }
            mTotalLength += getPaddingTop() + getPaddingBottom();
        }
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredResult(widthResolveSize, resolveSizeAndState(height, heightOptions));
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT, SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT);
    }

    public static class LayoutParams extends SLViewGroup.LayoutParams {

        public float weight;
        public int gravity;

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
