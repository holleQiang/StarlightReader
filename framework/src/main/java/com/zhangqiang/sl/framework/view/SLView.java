package com.zhangqiang.sl.framework.view;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.gesture.SLMotionEvent;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.image.SLDrawable;
import com.zhangqiang.sl.framework.image.SLImage;

import java.util.HashMap;
import java.util.Map;

public class SLView {

    private static final boolean debug = false;
    public static final String TAG = SLView.class.getCanonicalName();
    private SLContext mContext;
    private SLViewParent mParent;
    private int mLeft, mTop;
    private int mWidth, mHeight;
    private int mMeasuredWidth, mMeasuredHeight;
    private SLAttachInfo mAttachInfo;
    private ActionQueue mRunQueue;
    private OnClickListener mOnClickListener;
    private int mTouchSlop;

    static final int P_FLAG_DRAWING_CACHE_VALID = 0x00000001;
    static final int P_FLAG_LAYOUT_REQUIRED = 0x00000002;
    static final int P_FLAG_DIRTY = 0x00000004;
    static final int P_FLAG_DIRTY_OPAQUE = 0x00000008;
    static final int P_FLAG_DIRTY_MASK = 0x0000000c;
    static final int P_FLAG_FORCE_LAYOUT = 0x00000010;
    static final int P_FLAG_SET_MEASURE_RESULT = 0x00000020;
    int mPrivateFlags;

    static final int FLAG_DRAWING_CACHE_ENABLE = 0x00000001;
    static final int FLAG_CLICKABLE = 0x00000002;
    static final int FLAG_LONG_CLICKABLE = 0x00000004;
    int mViewFlags;

    private OnLongClickListener mOnLongClickListener;
    SLViewGroup.LayoutParams layoutParams;
    private SLDrawable mBackground;
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;
    private SLImage mDrawingCache;
    private Map<Long, Long> mMeasuredCache;
    private long mOldMeasOptions;

    public SLView(SLContext context) {
        this.mContext = context;
        mTouchSlop = context.getScaledTouchSlop();
    }

    public final void measure(int widthOptions, int heightOptions) {

        if (mMeasuredCache == null) {
            mMeasuredCache = new HashMap<>();
        }
        long key = (long) widthOptions << 32 | heightOptions;
        int oldWidthOptions = (int) (mOldMeasOptions >> 32);
        int oldHeightOptions = (int) mOldMeasOptions;
        boolean forceLayout = (mPrivateFlags & P_FLAG_FORCE_LAYOUT) == P_FLAG_FORCE_LAYOUT;
        boolean optionsChanged = oldWidthOptions != widthOptions || oldHeightOptions != heightOptions;
        boolean optionsExactly = MeasureOptions.getMode(widthOptions) == MeasureOptions.MODE_EXACTLY
                && MeasureOptions.getMode(heightOptions) == MeasureOptions.MODE_EXACTLY;
        boolean matchOptionSize = getMeasuredWidth() == MeasureOptions.getSize(widthOptions) && getMeasuredHeight() == MeasureOptions.getSize(heightOptions);
        boolean needLayout = optionsChanged && (!optionsExactly || !matchOptionSize);
        if (forceLayout || needLayout){

            Long value = forceLayout ? null : mMeasuredCache.get(key);
            if (value == null) {

                onMeasure(widthOptions, heightOptions);
            } else {
                setMeasuredResult((int) (value >> 32), (int) (long) value);
            }

            if ((mPrivateFlags & P_FLAG_SET_MEASURE_RESULT) != P_FLAG_SET_MEASURE_RESULT) {
                throw new RuntimeException("you must call setMeasuredResult within method onMeasure");
            }
            mPrivateFlags |= P_FLAG_LAYOUT_REQUIRED;
        }
        mOldMeasOptions = (long) widthOptions << 32 | heightOptions;
        mMeasuredCache.put(key, (long) mMeasuredWidth << 32 | mMeasuredHeight);
    }

    protected void onMeasure(int widthOptions, int heightOptions) {
        setMeasuredResult(getDefaultMeasuredSize(widthOptions), getDefaultMeasuredSize(heightOptions));
    }

    private static int getDefaultMeasuredSize(int options) {
        int mode = MeasureOptions.getMode(options);
        if (mode == MeasureOptions.MODE_EXACTLY || mode == MeasureOptions.MODE_AD_MOST) {
            return MeasureOptions.getSize(options);
        } else {
            return 0;
        }
    }

    protected final void setMeasuredResult(int width, int height) {
        mMeasuredWidth = width;
        mMeasuredHeight = height;
        mPrivateFlags |= P_FLAG_SET_MEASURE_RESULT;
    }

    public final void layout(int left, int top, int right, int bottom) {

        boolean changed = mLeft != left || mTop != top || mLeft + mWidth != right || mTop + mHeight != bottom;
        if (changed) {
            mLeft = left;
            mTop = top;
            mWidth = right - left;
            mHeight = bottom - top;
        }
        if (changed || (mPrivateFlags & P_FLAG_LAYOUT_REQUIRED) == P_FLAG_LAYOUT_REQUIRED) {
            onLayout(changed, left, top, right, bottom);
            mPrivateFlags &= ~P_FLAG_LAYOUT_REQUIRED;
        }
        mPrivateFlags &= ~P_FLAG_FORCE_LAYOUT;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }

    final void draw(SLCanvas canvas) {
        long currentTimeMillis = System.currentTimeMillis();
        boolean isOpaque = (mPrivateFlags & P_FLAG_DIRTY_MASK) == P_FLAG_DIRTY_OPAQUE;
        mPrivateFlags = mPrivateFlags & ~P_FLAG_DIRTY_MASK;
        computeScroll();
        if (!isOpaque) {
            drawBackground(canvas);
            onDraw(canvas);
        }
        dispatchDraw(canvas);
        if (debug) {
            SLContext.getLogger().logI(TAG, "view draw cost===" + (System.currentTimeMillis() - currentTimeMillis) + getClass().getCanonicalName());
        }
    }

    void draw(SLCanvas canvas, SLViewGroup parent) {


        int left = getLeft();
        int top = getTop();
        int save = canvas.save();
        canvas.translate(left, top);
        canvas.clipRect(0, 0, getWidth(), getHeight());

        if (isDrawingCacheEnable()) {

            buildDrawingCache(canvas);
        } else {
            draw(canvas);
        }
        canvas.restoreToCount(save);
    }

    private void buildDrawingCache(SLCanvas canvas) {

        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            destroyDrawingCache();
            return;
        }
        computeScroll();

        if (mDrawingCache == null
                || mDrawingCache.getWidth() != width && mDrawingCache.getHeight() != height
                || (mPrivateFlags & P_FLAG_DRAWING_CACHE_VALID) != P_FLAG_DRAWING_CACHE_VALID) {
            destroyDrawingCache();
            mDrawingCache = getContext().createImage(width, height);

            SLCanvas cacheCanvas = mAttachInfo.getCanvas();
            if (cacheCanvas == null) {
                cacheCanvas = getContext().createCanvas(mDrawingCache);
            }
            cacheCanvas.setImage(mDrawingCache);
            mAttachInfo.setCanvas(null);
            mDrawingCache.eraseColor(0);
            draw(cacheCanvas);
            cacheCanvas.setImage(null);
            mAttachInfo.setCanvas(cacheCanvas);
            mPrivateFlags |= P_FLAG_DRAWING_CACHE_VALID;
        }
        canvas.drawImage(mDrawingCache, 0, 0);
    }

    protected void computeScroll() {

    }

    private void drawBackground(SLCanvas canvas) {
        if (mBackground != null) {
            mBackground.setBounds(0, 0, getWidth(), getHeight());
            mBackground.draw(canvas);
        }
    }

    protected void dispatchDraw(SLCanvas canvas) {

    }

    protected void onDraw(SLCanvas canvas) {

    }

    protected void setParent(SLViewParent parent) {
        this.mParent = parent;
    }

    public SLViewParent getParent() {
        return mParent;
    }

    public final void dispatchAttachToWindow(SLAttachInfo attachInfo) {
        if (attachInfo == null) {
            throw new NullPointerException();
        }
        mAttachInfo = attachInfo;
        if (mRunQueue != null) {
            mRunQueue.executeActions(attachInfo);
        }
        onAttachedToWindow();
    }

    protected void onAttachedToWindow() {

    }

    SLAttachInfo getAttachInfo() {
        return mAttachInfo;
    }

    public final void dispatchDetachFromWindow() {
        destroyDrawingCache();
        mAttachInfo = null;
        onDetachedFromWindow();
    }

    protected void onDetachedFromWindow() {

    }

    public boolean isAttachedToWindow() {
        return mAttachInfo != null;
    }

    public void post(Runnable runnable) {

        if (isAttachedToWindow()) {

            mAttachInfo.getHandler().post(runnable);
        } else {
            getRunQueue().post(runnable);
        }
    }

    public void postDelay(Runnable runnable, int delayMillions) {
        if (isAttachedToWindow()) {

            mAttachInfo.getHandler().postDelayed(runnable, delayMillions);
        } else {
            getRunQueue().postDelay(runnable, delayMillions);
        }
    }

    public void requestLayout() {
        destroyDrawingCache();
        if (mMeasuredCache != null) {
            mMeasuredCache.clear();
        }
        mPrivateFlags |= P_FLAG_FORCE_LAYOUT;
        SLViewParent parent = getParent();
        if (parent != null && !parent.isLayoutRequested()) {
            parent.requestLayout();
        }
    }

    public SLContext getContext() {
        return mContext;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void invalidate() {
        invalidate(true);
    }

    private void invalidate(boolean invalidateCache) {
        if (invalidateCache) {
            mPrivateFlags |= P_FLAG_DRAWING_CACHE_VALID;
        }
        mPrivateFlags |= P_FLAG_DIRTY;
        SLViewParent parent = getParent();
        if (parent != null) {
            parent.invalidateChild(this);
        }
    }

    public int getLeft() {
        return mLeft;
    }

    public int getTop() {
        return mTop;
    }

    public int getRight() {
        return getLeft() + getWidth();
    }

    public int getBottom() {
        return getTop() + getHeight();
    }

    public int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    private ActionQueue getRunQueue() {
        if (mRunQueue == null) {
            mRunQueue = new ActionQueue();
        }
        return mRunQueue;
    }

    public boolean dispatchTouchEvent(SLMotionEvent event) {
        return onTouchEvent(event);
    }

    protected boolean onTouchEvent(SLMotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        boolean clickable = isClickable() | isLongClickable();

        switch (action) {
            case SLMotionEvent.ACTION_DOWN:
                if (clickable) {
                    setPressed(true);
                }
                break;
            case SLMotionEvent.ACTION_MOVE:
                if (!pointInView(x, y, mTouchSlop)) {
                    removeClickCallback();
                }
                break;
            case SLMotionEvent.ACTION_UP:

                if (clickable) {
                    performClick();
                }

                setPressed(false);
                break;
            case SLMotionEvent.ACTION_CANCEL:
                setPressed(false);
                break;
        }
        return clickable;
    }

    private void setPressed(boolean pressed) {
        if (pressed) {
            if (mClickCallback == null) {
                mClickCallback = new PerformClickCallback();
            }
        } else {
            removeClickCallback();
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        if (!isClickable()) {
            setClickable(true);
        }
        this.mOnClickListener = onClickListener;
    }

    public void setClickable(boolean clickable) {
        setFlags(clickable ? FLAG_CLICKABLE : 0, FLAG_CLICKABLE);
    }

    public boolean isClickable() {
        return (mViewFlags & FLAG_CLICKABLE) == FLAG_CLICKABLE;
    }

    private void setFlags(int flag, int mask) {
        mViewFlags = (mViewFlags & ~mask) | (flag & mask);
    }

    public void setLongClickable(boolean longClickable) {
        setFlags(longClickable ? FLAG_LONG_CLICKABLE : 0, FLAG_LONG_CLICKABLE);
    }

    public boolean isLongClickable() {
        return (mViewFlags & FLAG_LONG_CLICKABLE) == FLAG_LONG_CLICKABLE;
    }

    private Runnable mClickCallback;

    private boolean pointInView(float x, float y, int touchSlop) {
        return x >= getLeft() && x < getRight() && y >= getTop() && y < getBottom();
    }

    class PerformClickCallback implements Runnable {

        @Override
        public void run() {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(SLView.this);
            }
        }
    }

    private void removeClickCallback() {
        if (mClickCallback != null) {
            mClickCallback = null;
        }
    }

    private void performClick() {
        if (mClickCallback != null) {
            mClickCallback.run();
        }
    }

    public SLViewGroup.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void setLayoutParams(SLViewGroup.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
        requestLayout();
    }

    public static int resolveSizeAndState(int size, int options) {
        int optionsSize = MeasureOptions.getSize(options);
        int optionsMode = MeasureOptions.getMode(options);
        if (optionsMode == MeasureOptions.MODE_AD_MOST) {
            return Math.min(size, optionsSize);
        } else if (optionsMode == MeasureOptions.MODE_EXACTLY) {
            return optionsSize;
        } else {
            return size;
        }
    }

    public void offsetLeftAndRight(int offset) {
        if (offset == 0) {
            return;
        }
        mLeft += offset;
        invalidate(false);
    }

    public void offsetTopAndBottom(int offset) {
        if (offset == 0) {
            return;
        }
        mTop += offset;
        invalidate(false);
    }

    public SLDrawable getBackground() {
        return mBackground;
    }

    public void setBackground(SLDrawable background) {
        if (mBackground == background) {
            return;
        }
        boolean requestLayout = mBackground == null
                || mBackground.getIntrinsicWidth() != background.getIntrinsicWidth()
                || mBackground.getIntrinsicHeight() != background.getIntrinsicHeight();

        this.mBackground = background;

        if (requestLayout) {
            requestLayout();
        }
        invalidate();
    }

    public void setDrawingCacheEnable(boolean enable) {
        if (!enable && isDrawingCacheEnable()) {
            destroyDrawingCache();
        }
        mViewFlags |= FLAG_DRAWING_CACHE_ENABLE;
    }

    public void destroyDrawingCache() {
        if (mDrawingCache != null) {
            mDrawingCache.recycle();
            mDrawingCache = null;
        }
    }

    public boolean isDrawingCacheEnable() {
        return (mViewFlags & FLAG_DRAWING_CACHE_ENABLE) == FLAG_DRAWING_CACHE_ENABLE;
    }

    public boolean isOpaque() {
        boolean isOpaque = false;
        if (mBackground != null) {
            isOpaque = mBackground.isOpaque();
        }
        return isOpaque;
    }

    public boolean isLayoutRequested() {
        return (mPrivateFlags & P_FLAG_FORCE_LAYOUT) == P_FLAG_FORCE_LAYOUT;
    }

    public void forceLayout() {
        mPrivateFlags |= P_FLAG_FORCE_LAYOUT;
    }

    public void setPadding(int paddingLeft,int paddingTop,int paddingRight,int paddingBottom){
        boolean changed = mPaddingLeft != paddingLeft
                || mPaddingTop != paddingTop
                || mPaddingRight != paddingRight
                || mPaddingBottom != paddingBottom;
        if (changed) {
            mPaddingLeft = paddingLeft;
            mPaddingTop = paddingTop;
            mPaddingRight = paddingRight;
            mPaddingBottom = paddingBottom;
            requestLayout();
        }
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }
}
