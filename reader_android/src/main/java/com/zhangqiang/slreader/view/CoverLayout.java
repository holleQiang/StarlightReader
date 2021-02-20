package com.zhangqiang.slreader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.zhangqiang.slreader.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class CoverLayout extends ViewGroup {

    private static final boolean debug = false;
    private static final String TAG = CoverLayout.class.getCanonicalName();
    private int mTouchSlop;
    private int mLastX, mLastY;
    private boolean beingDragged;
    private int mInitX;
    private int mInitY;
    private static final int DRAG_DIRECTION_LTR = 0;
    private static final int DRAG_DIRECTION_RTL = 1;
    private int mDragDirection;
    private boolean mInLayout;
    private boolean mLayoutBlocked;
    private Adapter mAdapter;
    private RecycleBin mRecycleBin;
    private View mTouchView;
    private final List<ScrollItem> mActiveScrollItems = new ArrayList<>();
    public static final int INTENT_PREVIOUS = 0;
    public static final int INTENT_NEXT = 1;
    private int mDragIntent;
    private OnPageChangeListener onPageChangeListener;
    private boolean mDataChanged;
    private Drawable mLeftShadowDrawable;
    private Drawable mRightShadowDrawable;
    private int mActivePointerId;
    private static final int INVALID_POINTER_ID = -1;

    public CoverLayout(Context context) {
        super(context);
        init();
    }

    public CoverLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoverLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mRecycleBin = new RecycleBin();
    }

    @Override
    public void requestLayout() {
        if (!mInLayout || !mLayoutBlocked) {
            super.requestLayout();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mInLayout = true;
        layoutChildren();
        mInLayout = false;
    }

    private void layoutChildren() {
        mLayoutBlocked = true;
        onLayoutChildren();
        mLayoutBlocked = false;
    }

    private void onLayoutChildren() {

        if (mAdapter == null) {
            return;
        }

        invalidate();
        final int childLeft = 0;
        final int childRight = getWidth();

        mRecycleBin.addChildrenToActiveViews(this);

        detachAllViewsFromParent();

        makeAndAddView(null, INTENT_NEXT);

        mRecycleBin.removeActiveViews();
        mDataChanged = false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int currX = (int) event.getX();
                int currY = (int) event.getY();
                mInitX = mLastX = currX;
                mInitY = mLastY = currY;
                beingDragged = false;
                mTouchView = findTouchView();
                mDragIntent = -1;
                mActivePointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                currX = (int) event.getX(pointerIndex);
                currY = (int) event.getY(pointerIndex);
                if (mTouchView != null && !beingDragged) {
                    int deltaX = currX - mLastX;
                    int deltaY = currY - mLastY;
                    int absDeltaX = Math.abs(deltaX);
                    int absDeltaY = Math.abs(deltaY);
                    if (absDeltaX > mTouchSlop && absDeltaX > absDeltaY) {
                        int intent = deltaX > 0 ? INTENT_PREVIOUS : INTENT_NEXT;
                        if (!onInterceptIntent(intent)) {
                            mDragIntent = intent;
                            beingDragged = true;
                            if (deltaX > 0) {
                                mLastX += mTouchSlop;
                            } else {
                                mLastX -= mTouchSlop;
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;
        }
        return beingDragged;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int clipLeft = 0;
        int clipRight = getWidth();
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View tempChild = getChildAt(i);
            if (tempChild == child) {
                break;
            }
            int left = tempChild.getLeft();
            if (left < 0) {
                clipLeft = Math.max(clipLeft, tempChild.getRight());
                clipRight = Math.min(clipRight, getWidth());
            } else if (left > 0) {
                clipLeft = Math.max(clipLeft, 0);
                clipRight = Math.min(clipRight, tempChild.getLeft());
            } else {
                return false;
            }
        }
        if (clipLeft >= clipRight) {
            return false;
        }

        int save = canvas.save();
        canvas.clipRect(clipLeft, 0, clipRight, child.getHeight());
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(save);
        int left = child.getLeft();
        if (left > 0) {
            if (mLeftShadowDrawable != null) {
                Rect bounds = mLeftShadowDrawable.getBounds();
                mLeftShadowDrawable.setBounds(left - bounds.width(), 0, left, getHeight());
                int saveIndex = canvas.save();
                canvas.clipRect(0, 0, left, getHeight());
                mLeftShadowDrawable.draw(canvas);
                canvas.restoreToCount(saveIndex);
            }
        }
        int right = child.getRight();
        if (right < getWidth()) {

            if (mRightShadowDrawable != null) {
                Rect bounds = mRightShadowDrawable.getBounds();
                mRightShadowDrawable.setBounds(right, 0, right + bounds.width(), getHeight());
                int saveIndex = canvas.save();
                canvas.clipRect(right, 0, getWidth(), getHeight());
                mRightShadowDrawable.draw(canvas);
                canvas.restoreToCount(saveIndex);
            }
        }
        return result;
    }

    private boolean onInterceptIntent(int intent) {
        if (debug) {
            LogUtils.logI(TAG, "========onInterceptIntent======");
        }
        View child = getChildAt(0);
        return makeAndAddView(child, intent) == null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                int actionIndex = event.getActionIndex();
                mActivePointerId = event.getPointerId(actionIndex);
                mLastX = (int) event.getX(actionIndex);
                mLastY = (int) event.getY(actionIndex);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                actionIndex = event.getActionIndex();
                int pointerId = event.getPointerId(actionIndex);
                if (pointerId == mActivePointerId) {
                    int newPointerIndex = actionIndex == 0 ? 1 : 0;
                    mActivePointerId = event.getPointerId(newPointerIndex);
                    mLastX = (int) event.getX(newPointerIndex);
                    mLastY = (int) event.getY(newPointerIndex);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                int currX = (int) event.getX(pointerIndex);
                int currY = (int) event.getY(pointerIndex);
                if (mTouchView != null && !beingDragged) {
                    int deltaX = currX - mLastX;
                    int deltaY = currY - mLastY;
                    int absDeltaX = Math.abs(deltaX);
                    int absDeltaY = Math.abs(deltaY);
                    if (absDeltaX > mTouchSlop && absDeltaX > absDeltaY) {
                        int intent = deltaX > 0 ? INTENT_PREVIOUS : INTENT_NEXT;
                        if (!onInterceptIntent(intent)) {
                            mDragIntent = intent;
                            beingDragged = true;
                            if (deltaX > 0) {
                                mLastX += mTouchSlop;
                            } else {
                                mLastX -= mTouchSlop;
                            }
                        }
                    }
                }
                if (beingDragged) {

                    int deltaX = currX - mLastX;
                    if (deltaX != 0) {
                        mDragDirection = deltaX > 0 ? DRAG_DIRECTION_LTR : DRAG_DIRECTION_RTL;
                    }
                    if (mTouchView != null) {
                        mTouchView.offsetLeftAndRight(deltaX);
                    }
                    mLastX = currX;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!beingDragged) {
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    currX = (int) event.getX(pointerIndex);
                    currY = (int) event.getY(pointerIndex);
                    int height = getHeight();
                    int width = getWidth();
                    int centerLeft = width / 3;
                    int centerTop = height / 3;
                    int centerRight = centerLeft * 2;
                    int centerBottom = centerTop * 2;
                    if (currX > centerLeft && currY > centerBottom
                            || currX > centerRight) {
                        //click right
                        if (!onInterceptIntent(INTENT_NEXT)) {
                            if (mTouchView != null) {
                                smoothScrollToLeft(mTouchView);
                                notifyPageChanged();
                            }
                        }
                    } else if (currX < centerLeft || currX < centerRight && currY < centerTop) {
                        //click left
                        if (!onInterceptIntent(INTENT_PREVIOUS)) {
                            if (mTouchView != null) {
                                smoothScrollToRight(mTouchView);
                                notifyPageChanged();
                            }
                        }
                    } else {
                        notifyPageClicked();
                    }
                } else {
                    handleBeingDraggedWhenTouchEnd();
                }
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_CANCEL:
                if (beingDragged) {
                    handleBeingDraggedWhenTouchEnd();
                }
                mActivePointerId = INVALID_POINTER_ID;
                break;
        }
        return true;
    }

    private void handleBeingDraggedWhenTouchEnd() {
        beingDragged = false;
        if (mDragDirection == DRAG_DIRECTION_LTR) {
            if (mDragIntent == INTENT_PREVIOUS) {
                if (mTouchView != null) {
                    smoothScrollToRight(mTouchView);
                    notifyPageChanged();
                }
            } else {
                if (mTouchView != null) {
                    smoothScrollToOrigin(mTouchView);
                }
            }
        } else if (mDragDirection == DRAG_DIRECTION_RTL) {
            if (mDragIntent == INTENT_NEXT) {
                if (mTouchView != null) {
                    smoothScrollToLeft(mTouchView);
                    notifyPageChanged();
                }
            } else {
                if (mTouchView != null) {
                    smoothScrollToOrigin(mTouchView);
                }
            }
        }
    }

    private void notifyPageClicked() {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageCenterClick(getChildAt(0));
        }
    }

    private void notifyPageChanged() {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageChange(getChildAt(0));
        }
    }

    private void smoothScrollToRight(View child) {
        Scroller scroller = new Scroller(getContext());
        int left = child.getLeft();
        scroller.startScroll(left, 0, getWidth() - left, 0, 1000);
        mActiveScrollItems.add(new ScrollItem(child, scroller));
        invalidate();
    }

    private void smoothScrollToLeft(View child) {
        Scroller scroller = new Scroller(getContext());
        int left = child.getLeft();
        scroller.startScroll(left, 0, -getWidth() - left, 0, 1000);
        mActiveScrollItems.add(new ScrollItem(child, scroller));
        invalidate();
    }

    private void smoothScrollToOrigin(View child) {
        Scroller scroller = new Scroller(getContext());
        int left = child.getLeft();
        scroller.startScroll(left, 0, 0 - left, 0, 1000);
        mActiveScrollItems.add(new ScrollItem(child, scroller));
        invalidate();
    }

    private View findTouchView() {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getLeft() == 0) {
                return child;
            }
        }
        return null;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        for (int i = mActiveScrollItems.size() - 1; i >= 0; i--) {
            ScrollItem scrollItem = mActiveScrollItems.get(i);
            Scroller scroller = scrollItem.scroller;
            View view = scrollItem.view;
            if (scroller.computeScrollOffset()) {
                int deltaLeft = scroller.getCurrX() - view.getLeft();
                view.offsetLeftAndRight(deltaLeft);
            } else {
                mActiveScrollItems.remove(i);
                int childCount = getChildCount();
                for (int index = childCount - 1; index >= 0; index--) {
                    View child = getChildAt(index);
                    if (child.getLeft() >= getWidth() || child.getRight() <= 0) {
                        removeViewInLayout(child);
                        mRecycleBin.addScrapView(child);
                    }
                }
            }
        }
        if (mActiveScrollItems.size() > 0) {
            invalidate();
        } else {
            boolean covered = false;
            int childCount = getChildCount();
            for (int index = childCount - 1; index >= 0; index--) {
                View child = getChildAt(index);
                if (!covered && child.getLeft() == 0) {
                    covered = true;
                    continue;
                }
                if (covered) {
                    removeViewInLayout(child);
                    mRecycleBin.addScrapView(child);
                }
            }
        }
    }

    public void setAdapter(Adapter adapter) {
        mRecycleBin.clear();
        removeAllViewsInLayout();
        abortAnimations();
        if (mAdapter != null) {
            mAdapter.unRegisterObserver(mObserver);
            mAdapter = null;
        }
        this.mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerObserver(mObserver);
        }
        requestLayout();
    }

    private final Adapter.Observer mObserver = new Adapter.Observer() {
        @Override
        public void onDataChanged() {
            mDataChanged = true;
            requestLayout();
        }
    };

    private class RecycleBin {

        private ViewNode mActiveView;
        private ViewNode mScrapView;

        void addChildrenToActiveViews(ViewGroup viewGroup) {

            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                child.forceLayout();
                ViewNode node = ViewNode.obtain();
                node.view = child;
                node.next = mActiveView;
                mActiveView = node;
            }
        }

        View getActiveView(View child) {
            if (mActiveView == null) {
                return null;
            }
            if (child == null) {
                ViewNode tempNode = mActiveView;
                mActiveView = tempNode.next;
                View view = tempNode.view;
                tempNode.recycle();
                return view;
            }
            ViewNode prev = null;
            ViewNode tempNode = mActiveView;
            while (tempNode != null) {
                if (tempNode.view == child) {
                    break;
                }
                prev = tempNode;
                tempNode = tempNode.next;
            }
            if (tempNode != null) {
                if (prev != null) {

                    prev.next = tempNode.next;

                } else {
                    mActiveView = tempNode.next;
                }
                View view = tempNode.view;
                tempNode.recycle();
                return view;
            }
            return null;
        }


        void addScrapView(View view) {
            ViewNode node = ViewNode.obtain();
            node.next = mScrapView;
            node.view = view;
            mScrapView = node;
        }

        View getScrapView() {
            ViewNode node = mScrapView;
            if (node != null) {
                mScrapView = node.next;
                View view = node.view;
                node.recycle();
                return view;
            }
            return null;
        }

        void removeActiveViews() {
            while (mActiveView != null) {
                ViewNode node = mActiveView;
                mActiveView = node.next;
                View view = node.view;
                removeDetachedView(view, false);
                node.recycle();
                addScrapView(view);
            }
        }

        void clear() {

            ViewNode temp = mActiveView;
            while (temp != null) {
                ViewNode next = temp.next;
                temp.recycle();
                temp = next;
            }
            temp = mScrapView;
            while (temp != null) {
                ViewNode next = temp.next;
                temp.recycle();
                temp = next;
            }
            mActiveView = null;
            mScrapView = null;
        }
    }

    static class ViewNode {

        View view;
        ViewNode next;
        static ViewNode sPool;

        ViewNode() {
        }

        static ViewNode obtain() {
            if (sPool == null) {
                return new ViewNode();
            }
            ViewNode result = sPool;
            sPool = result.next;
            return result;
        }

        void recycle() {
            next = sPool;
            sPool = this;
            view = null;
            next = null;
        }
    }

    private View makeAndAddView(View prevView, int viewType) {

        if (!mDataChanged) {
            View activeView = mRecycleBin.getActiveView(prevView);
            if (activeView != null) {
                setupView(activeView, true);
                return activeView;
            }
        }

        View view = obtainView(prevView, viewType);
        if (view != null) {
            setupView(view, false);
        }
        return view;
    }

    private void setupView(View view, boolean isAttachedToWindow) {
        boolean needToMeasure = view.isLayoutRequested() || !isAttachedToWindow;
        if (isAttachedToWindow) {
            attachViewToParent(view, 0, view.getLayoutParams());
        } else {
            addViewInLayout(view, 0, view.getLayoutParams(), true);
        }
        if (needToMeasure) {
            int widthMeasureOptions = MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY);
            int heightMeasureOptions = MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY);
            measureChild(view, widthMeasureOptions, heightMeasureOptions);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
    }

    private View obtainView(View prevView, int viewType) {
        View scrapView = mRecycleBin.getScrapView();
        View child = mAdapter.getView(this, prevView, scrapView, viewType);
        if (scrapView != null && scrapView != child) {
            mRecycleBin.addScrapView(scrapView);
        }
        LayoutParams layoutParams = child.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = generateDefaultLayoutParams();
            child.setLayoutParams(layoutParams);
        }
        return child;
    }

    private static class ScrollItem {
        View view;
        Scroller scroller;

        ScrollItem(View view, Scroller scroller) {
            this.view = view;
            this.scroller = scroller;
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        abortAnimations();
    }

    private void abortAnimations() {
        for (int i = mActiveScrollItems.size() - 1; i >= 0; i--) {
            mActiveScrollItems.get(i).scroller.abortAnimation();
        }
        mActiveScrollItems.clear();
    }

    public interface OnPageChangeListener {

        void onPageChange(View view);

        void onPageCenterClick(View view);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }


    public static abstract class Adapter {

        private List<Observer> observers = new ArrayList<>();

        public abstract View getView(ViewGroup parent, View prevView, View convertView, int intent);

        public interface Observer {

            void onDataChanged();
        }

        public void registerObserver(Observer observer) {
            if (observers.contains(observer)) {
                return;
            }
            observers.add(observer);
        }

        public void unRegisterObserver(Observer observer) {
            observers.remove(observer);
        }

        protected void notifyDataChanged() {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onDataChanged();
            }
        }
    }

    public void setLeftShadowDrawable(Drawable leftShadowDrawable) {
        if (mLeftShadowDrawable != leftShadowDrawable) {
            this.mLeftShadowDrawable = leftShadowDrawable;
            invalidate();
        }
    }

    public void setRightShadowDrawable(Drawable rightShadowDrawable) {
        if (mRightShadowDrawable != rightShadowDrawable) {
            this.mRightShadowDrawable = rightShadowDrawable;
            invalidate();
        }
    }
}
