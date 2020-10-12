package com.zhangqiang.sl.reader.layout;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.animation.SLScroller;
import com.zhangqiang.sl.framework.gesture.SLMotionEvent;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.view.MeasureOptions;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CoverLayout extends SLViewGroup {

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
    private CoverLayoutAdapter mAdapter;
    private RecycleBin mRecycleBin;
    private SLView mTouchView;
    private final List<ScrollItem> mActiveScrollItems = new ArrayList<>();
    public static final int INTENT_PREVIOUS = 0;
    public static final int INTENT_NEXT = 1;
    private int mDragIntent;
    private OnPageChangeListener onPageChangeListener;
    private boolean mDataChanged;

    public CoverLayout(SLContext context) {
        super(context);
        mTouchSlop = context.getScaledTouchSlop();
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
    protected boolean onInterceptTouchEvent(SLMotionEvent event) {
        int currX = (int) event.getX();
        int currY = (int) event.getY();
        switch (event.getAction()) {
            case SLMotionEvent.ACTION_DOWN:
                mInitX = mLastX = currX;
                mInitY = mLastY = currY;
                beingDragged = false;
                mTouchView = findTouchView();
                mDragIntent = -1;
                break;
            case SLMotionEvent.ACTION_MOVE:
                processDragIdentify(currX, currY);
                break;
            case SLMotionEvent.ACTION_UP:
                break;
        }
        return beingDragged;
    }

    @Override
    protected void drawChild(SLCanvas canvas, SLView child, SLViewGroup viewGroup) {

        int clipLeft = 0;
        int clipRight = getWidth();
        int childCount = viewGroup.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            SLView tempChild = getChildAt(i);
            if (tempChild == child) {
                break;
            }
            int left = tempChild.getLeft();
            if (left < 0) {
                clipLeft = tempChild.getRight();
                clipRight = getWidth();
            } else if (left > 0) {
                clipLeft = 0;
                clipRight = tempChild.getLeft();
            } else {
                return;
            }
        }

        int save = canvas.save();
        canvas.clipRect(clipLeft, 0, clipRight, child.getHeight());
        super.drawChild(canvas, child, viewGroup);
        canvas.restoreToCount(save);

    }

    private void processDragIdentify(int currX, int currY) {
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
    }

    private boolean onInterceptIntent(int intent) {
        if (debug) {
            SLContext.getLogger().logI(TAG, "========onInterceptIntent======");
        }
        SLView child = getChildAt(0);
        return makeAndAddView(child, intent) == null;
    }

    @Override
    protected boolean onTouchEvent(SLMotionEvent event) {
        int currX = (int) event.getX();
        int currY = (int) event.getY();
        switch (event.getAction()) {
            case SLMotionEvent.ACTION_MOVE:

                processDragIdentify(currX, currY);

                if (beingDragged) {
                    int deltaX = currX - mLastX;
                    if (deltaX != 0) {
                        mDragDirection = deltaX > 0 ? DRAG_DIRECTION_LTR : DRAG_DIRECTION_RTL;
                    }
                    if (mTouchView != null) {
                        mTouchView.offsetLeftAndRight(deltaX);
                    }
                    mLastX = currX;
                    mLastY = currY;
                }
                break;
            case SLMotionEvent.ACTION_UP:
                if (!beingDragged) {
                    if (currX > getWidth() / 3 * 2) {
                        //click right
                        if (!onInterceptIntent(INTENT_NEXT)) {
                            if (mTouchView != null) {
                                smoothScrollToLeft(mTouchView);
                                notifyPageChanged();
                            }
                        }
                    } else if (currX < getWidth() / 3) {
                        //click left
                        if (!onInterceptIntent(INTENT_PREVIOUS)) {
                            if (mTouchView != null) {
                                smoothScrollToRight(mTouchView);
                                notifyPageChanged();
                            }
                        }
                    }else {
                        notifyPageClicked();
                    }
                } else {
                    handleBeingDraggedWhenTouchEnd();
                }
                break;
            case SLMotionEvent.ACTION_CANCEL:
                if (beingDragged) {
                    handleBeingDraggedWhenTouchEnd();
                }
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

    private void smoothScrollToRight(SLView child) {
        SLScroller scroller = new SLScroller(getContext());
        int left = child.getLeft();
        scroller.startScroll(left, 0, getWidth() - left, 0, 1000);
        mActiveScrollItems.add(new ScrollItem(child, scroller));
        invalidate();
    }

    private void smoothScrollToLeft(SLView child) {
        SLScroller scroller = new SLScroller(getContext());
        int left = child.getLeft();
        scroller.startScroll(left, 0, -getWidth() - left, 0, 1000);
        mActiveScrollItems.add(new ScrollItem(child, scroller));
        invalidate();
    }

    private void smoothScrollToOrigin(SLView child) {
        SLScroller scroller = new SLScroller(getContext());
        int left = child.getLeft();
        scroller.startScroll(left, 0, 0 - left, 0, 1000);
        mActiveScrollItems.add(new ScrollItem(child, scroller));
        invalidate();
    }

    private SLView findTouchView() {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            SLView child = getChildAt(i);
            if (child.getLeft() == 0) {
                return child;
            }
        }
        return null;
    }

    @Override
    protected void computeScroll() {
        super.computeScroll();
        for (int i = mActiveScrollItems.size() - 1; i >= 0; i--) {
            ScrollItem scrollItem = mActiveScrollItems.get(i);
            SLScroller scroller = scrollItem.scroller;
            SLView view = scrollItem.view;
            if (scroller.computeScrollOffset()) {
                int deltaLeft = scroller.getCurrX() - view.getLeft();
                view.offsetLeftAndRight(deltaLeft);
            } else {
                mActiveScrollItems.remove(i);
                int childCount = getChildCount();
                for (int index = childCount - 1; index >= 0; index--) {
                    SLView child = getChildAt(index);
                    if (child.getLeft() >= getWidth() || child.getRight() <= 0) {
                        removeViewInLayout(child);
                        mRecycleBin.addScrapView(child);
                    }
                }
            }
        }
        if (mActiveScrollItems.size() > 0) {
            invalidate();
        }
    }

    public void setAdapter(CoverLayoutAdapter adapter) {
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

    private final CoverLayoutAdapter.Observer mObserver = new CoverLayoutAdapter.Observer() {
        @Override
        public void onDataChanged() {
            mDataChanged = true;
            requestLayout();
        }
    };

    private class RecycleBin {

        private ViewNode mActiveView;
        private ViewNode mScrapView;

        void addChildrenToActiveViews(SLViewGroup viewGroup) {

            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                SLView child = viewGroup.getChildAt(i);
                child.forceLayout();
                ViewNode node = ViewNode.obtain();
                node.view = child;
                node.next = mActiveView;
                mActiveView = node;
            }
        }

        public SLView getActiveView(SLView child) {
            if (mActiveView == null) {
                return null;
            }
            if (child == null) {
                ViewNode tempNode = mActiveView;
                mActiveView = tempNode.next;
                SLView view = tempNode.view;
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
                SLView view = tempNode.view;
                tempNode.recycle();
                return view;
            }
            return null;
        }


        void addScrapView(SLView view) {
            ViewNode node = ViewNode.obtain();
            node.next = mScrapView;
            node.view = view;
            mScrapView = node;
        }

        SLView getScrapView() {
            ViewNode node = mScrapView;
            if (node != null) {
                mScrapView = node.next;
                SLView view = node.view;
                node.recycle();
                return view;
            }
            return null;
        }

        void removeActiveViews() {
            while (mActiveView != null) {
                ViewNode node = mActiveView;
                mActiveView = node.next;
                SLView view = node.view;
                removeDetachedView(view);
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

        SLView view;
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

    private SLView makeAndAddView(SLView prevView, int viewType) {

        if (!mDataChanged) {
            SLView activeView = mRecycleBin.getActiveView(prevView);
            if (activeView != null) {
                setupView(activeView);
                return activeView;
            }
        }

        SLView view = obtainView(prevView, viewType);
        if (view != null) {
            setupView(view);
        }
        return view;
    }

    private void setupView(SLView view) {
        boolean needToMeasure = view.isLayoutRequested() || !view.isAttachedToWindow();
        if (view.isAttachedToWindow()) {
            attachViewToParent(view, 0, view.getLayoutParams());
        } else {
            addViewInLayout(view, 0);
        }
        if (needToMeasure) {
            int widthMeasureOptions = MeasureOptions.make(getWidth(), MeasureOptions.MODE_EXACTLY);
            int heightMeasureOptions = MeasureOptions.make(getHeight(), MeasureOptions.MODE_EXACTLY);
            measureChild(widthMeasureOptions, heightMeasureOptions,view);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
    }

    private SLView obtainView(SLView prevView, int viewType) {
        SLView scrapView = mRecycleBin.getScrapView();
        SLView child = mAdapter.getView(this, prevView, scrapView, viewType);
        if (scrapView != null && scrapView != child) {
            mRecycleBin.addScrapView(scrapView);
        }
        return child;
    }

    private static class ScrollItem {
        SLView view;
        SLScroller scroller;

        ScrollItem(SLView view, SLScroller scroller) {
            this.view = view;
            this.scroller = scroller;
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.SIZE_MATCH_PARENT,LayoutParams.SIZE_MATCH_PARENT);
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

    public interface OnPageChangeListener{

        void onPageChange(SLView view);

        void onPageCenterClick(SLView view);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
