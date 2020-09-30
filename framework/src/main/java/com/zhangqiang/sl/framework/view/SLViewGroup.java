package com.zhangqiang.sl.framework.view;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.gesture.SLMotionEvent;
import com.zhangqiang.sl.framework.graphic.SLCanvas;

import java.util.ArrayList;
import java.util.List;

public abstract class SLViewGroup extends SLView implements SLViewParent {

    private final boolean debug = false;
    private static final String TAG = SLViewGroup.class.getCanonicalName();
    private List<SLView> children;
    private SLView mTouchTarget;

    public SLViewGroup(SLContext context) {
        super(context);
    }

    public void addView(SLView child) {

        if (child.getParent() != null) {
            throw new IllegalArgumentException(child + " has already has a parent" + child.getParent());
        }
        addViewInternal(child, getChildCount());
        requestLayout();
    }

    public void removeView(SLView child) {
        if (children == null) {
            return;
        }
        if (removeViewInternal(child)) {
            requestLayout();
        }
    }

    public void removeAllViews() {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            removeView(getChildAt(i));
        }
    }

    private boolean removeViewInternal(SLView child) {
        if (children.remove(child)) {
            child.setParent(null);
            if (child.isAttachedToWindow()) {
                child.dispatchDetachFromWindow();
            }
            return true;
        }
        return false;
    }

    private void addViewInternal(SLView child, int index) {
        initChildrenIfNeed();
        LayoutParams layoutParams = child.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = generateDefaultLayoutParams();
            child.setLayoutParams(layoutParams);
        }
        children.add(index, child);
        child.setParent(this);
        if (isAttachedToWindow()) {
            child.dispatchAttachToWindow(getAttachInfo());
        }
    }

    public void addViewInLayout(SLView child) {
        if (child == null) {
            throw new IllegalArgumentException("cannot add null child view");
        }
        addViewInLayout(child, getChildCount());
    }

    public void addViewInLayout(SLView child, int index) {
        if (child == null) {
            throw new IllegalArgumentException("cannot add null child view");
        }
        addViewInternal(child, index);
    }

    public void removeViewInLayout(SLView child) {
        if (children == null) {
            return;
        }
        removeViewInternal(child);
    }

    protected void removeAllViewsInLayout() {
        if (children == null) {
            return;
        }
        for (int i = children.size() - 1; i >= 0; i--) {
            SLView view = children.get(i);
            removeViewInLayout(view);
        }
    }

    protected void detachAllViewsFromParent() {
        if (children == null) {
            return;
        }
        for (int i = 0; i < children.size(); i++) {
            children.get(i).setParent(null);
        }
        children.clear();
    }

    protected void detachViewFromParent(SLView child) {
        children.remove(child);
        child.setParent(null);
    }

    protected void removeDetachedView(SLView child) {
        child.dispatchDetachFromWindow();
    }

    protected void attachViewToParent(SLView child, int index, LayoutParams layoutParams) {
        if (child.getParent() != null) {
            throw new IllegalArgumentException("child has already has a parent");
        }
        child.layoutParams = layoutParams;
        children.add(index, child);
        child.setParent(this);
    }

    private void initChildrenIfNeed() {
        if (children == null) {
            children = new ArrayList<>();
        }
    }

    public int getChildCount() {
        return children == null ? 0 : children.size();
    }

    public SLView getChildAt(int index) {
        if (children == null || index < 0 || index > children.size() - 1) {
            return null;
        }
        return children.get(index);
    }

    @Override
    protected void dispatchDraw(SLCanvas canvas) {
        super.dispatchDraw(canvas);
        long currentTimeMillis = System.currentTimeMillis();
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {

            SLView child = getChildAt(index);
            drawChild(canvas, child, this);
        }
        if (debug) {
            SLContext.getLogger().logI(TAG, "=======dispatchDraw========" + (System.currentTimeMillis() - currentTimeMillis) + "&" + getClass().getCanonicalName() + "==childCount:" + getChildCount());
        }
    }

    protected void drawChild(SLCanvas canvas, SLView child, SLViewGroup viewGroup) {
        child.draw(canvas, viewGroup);
    }

    @Override
    protected abstract void onLayout(boolean changed, int left, int top, int right, int bottom);

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int childCount = getChildCount();
        for (int index = childCount - 1; index >= 0; index--) {
            getChildAt(index).dispatchAttachToWindow(getAttachInfo());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        int childCount = getChildCount();
        for (int index = childCount - 1; index >= 0; index--) {
            getChildAt(index).dispatchDetachFromWindow();
        }
    }

    @Override
    public void invalidateChild(SLView child) {

        int flag = child.isOpaque() ? P_FLAG_DIRTY_OPAQUE : P_FLAG_DIRTY;
        mPrivateFlags = mPrivateFlags & ~P_FLAG_DIRTY_MASK | flag;

        SLViewParent tempParent = this;
        do {

            tempParent = tempParent.invalidateChildInParent();
        } while (tempParent != null);
    }

    @Override
    public SLViewParent invalidateChildInParent() {
        return getParent();
    }

    @Override
    public boolean dispatchTouchEvent(SLMotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

        if (action == SLMotionEvent.ACTION_DOWN) {
            mTouchTarget = null;
        }

        boolean intercept;
        if (action == SLMotionEvent.ACTION_DOWN || mTouchTarget != null) {

            intercept = onInterceptTouchEvent(event);
            if (intercept && mTouchTarget != null) {
                event.setAction(SLMotionEvent.ACTION_CANCEL);
                mTouchTarget.dispatchTouchEvent(event);
                event.setAction(action);
                mTouchTarget = null;
            }
        } else {
            intercept = true;
        }
        boolean handed = false;
        if (!intercept) {

            if (action == SLMotionEvent.ACTION_DOWN) {

                SLView touchTarget = findTouchTarget(x, y);
                if (touchTarget != null) {
                    int xOffset = touchTarget.getLeft();
                    int yOffset = touchTarget.getTop();
                    event.offset(xOffset, yOffset);
                    handed = touchTarget.dispatchTouchEvent(event);
                    event.offset(-xOffset, -yOffset);

                    if (handed) {
                        mTouchTarget = touchTarget;
                    }
                }
            }
        }
        if (!handed) {
            if (mTouchTarget == null) {
                handed = super.dispatchTouchEvent(event);
            } else {
                handed = mTouchTarget.dispatchTouchEvent(event);
            }
        }
        return handed;
    }

    protected boolean onInterceptTouchEvent(SLMotionEvent event) {
        return false;
    }

    private SLView findTouchTarget(float touchX, float touchY) {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            SLView child = getChildAt(i);
            if (touchX >= child.getLeft() && touchX < child.getRight()
                    && touchY >= child.getTop() && touchY < child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    public static class LayoutParams {

        public static final int SIZE_WRAP_CONTENT = -1;
        public static final int SIZE_MATCH_PARENT = -2;

        private int width;
        private int height;

        public LayoutParams(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.SIZE_WRAP_CONTENT, LayoutParams.SIZE_WRAP_CONTENT);
    }

    protected static void measureChild(int widthOptions, int heightOptions, SLView child) {
        measureChild(widthOptions, 0, heightOptions, 0, child);
    }

    protected static void measureChild(SLView child, int width, int widthMode, int height, int heightMode) {
        measureChild(MeasureOptions.make(width, widthMode), MeasureOptions.make(height, heightMode), child);
    }

    protected static void measureChild(int widthOptions, int widthUsed, int heightOptions, int heightUsed, SLView child) {
        LayoutParams layoutParams = child.getLayoutParams();
        child.measure(makeChildMeasureOptions(widthOptions, widthUsed, layoutParams.width),
                makeChildMeasureOptions(heightOptions, heightUsed, layoutParams.height));
    }

    private static int makeChildMeasureOptions(int options, int sizeUsed, int layoutSize) {

        int resultMode;
        int resultSize;
        int mode = MeasureOptions.getMode(options);
        int size = MeasureOptions.getSize(options) - sizeUsed;
        if (mode == MeasureOptions.MODE_AD_MOST) {

            if (layoutSize == LayoutParams.SIZE_WRAP_CONTENT) {
                resultSize = size;
                resultMode = MeasureOptions.MODE_AD_MOST;
            } else if (layoutSize == LayoutParams.SIZE_MATCH_PARENT) {
                resultSize = size;
                resultMode = MeasureOptions.MODE_AD_MOST;
            } else {
                resultSize = layoutSize;
                resultMode = MeasureOptions.MODE_EXACTLY;
            }
        } else if (mode == MeasureOptions.MODE_EXACTLY) {

            if (layoutSize == LayoutParams.SIZE_WRAP_CONTENT) {
                resultSize = size;
                resultMode = MeasureOptions.MODE_AD_MOST;
            } else if (layoutSize == LayoutParams.SIZE_MATCH_PARENT) {
                resultSize = size;
                resultMode = MeasureOptions.MODE_EXACTLY;
            } else {
                resultSize = layoutSize;
                resultMode = MeasureOptions.MODE_EXACTLY;
            }
        } else if (mode == MeasureOptions.MODE_UNSPECIFIED) {

            if (layoutSize == LayoutParams.SIZE_WRAP_CONTENT) {
                resultSize = size;
                resultMode = MeasureOptions.MODE_UNSPECIFIED;
            } else if (layoutSize == LayoutParams.SIZE_MATCH_PARENT) {
                resultSize = size;
                resultMode = MeasureOptions.MODE_UNSPECIFIED;
            } else {
                resultSize = layoutSize;
                resultMode = MeasureOptions.MODE_EXACTLY;
            }
        } else {
            throw new IllegalArgumentException("unknown mode:" + mode);
        }
        return MeasureOptions.make(resultSize, resultMode);
    }


}
