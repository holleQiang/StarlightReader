package com.zhangqiang.slreader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhangqiang.slreader.parser.Book;
import com.zhangqiang.slreader.parser.Element;
import com.zhangqiang.slreader.parser.Paragraph;
import com.zhangqiang.slreader.position.TextWordPosition;
import com.zhangqiang.slreader.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageView extends ViewGroup {

    private static final boolean debug = true;
    private static final String TAG = PageView.class.getCanonicalName();
    private Book mBook;
    private final TextWordPosition mStartPosition = new TextWordPosition();
    private final TextWordPosition mEndPosition = new TextWordPosition();
    private TextWordPosition mPosition;
    private boolean mReverse;
    private RecycleBin mRecycleBin;
    private final List<LineInfo> mLineInfoList = new ArrayList<>();
    private PageViewAdapter mAdapter;
    private int mParagraphSpace;

    public PageView(Context context) {
        super(context);
        mRecycleBin = new RecycleBin();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        performLayout();
    }

    private void performLayout() {

        mStartPosition.reset();
        mEndPosition.reset();
        for (int i = 0; i < mLineInfoList.size(); i++) {
            mLineInfoList.get(i).recycle();
        }
        mLineInfoList.clear();
        mRecycleBin.addChildrenToScrapViews(this);

        if (mBook == null || mPosition == null || mAdapter == null) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        LogUtils.logI(TAG, "====start layout children=======" + mStartPosition + "=scrap view count=" + mRecycleBin.getScrapViewCount());

        if (mReverse) {
            mEndPosition.set(mPosition);
            layoutChildFromEnd();
        } else {
            mStartPosition.set(mPosition);
            layoutChildFromStart();
        }
        LogUtils.logI(TAG, "====layout children complete=======" + mEndPosition
                + "==cost=" + (System.currentTimeMillis() - currentTimeMillis)
                + "=scrap view count=" + mRecycleBin.getScrapViewCount());
    }

    private void layoutChildFromEnd() {

        final int maxWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int maxHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int currentHeight = 0;
        int lastParagraphIndex = -1;
        int paragraphCount = 0;
        int startParagraphIndex = mEndPosition.getParagraphIndex();
        for (int currentParagraphIndex = startParagraphIndex; currentParagraphIndex >= 0; currentParagraphIndex--) {

            Paragraph paragraph = mBook.getParagraph(currentParagraphIndex);
            int elementCount = paragraph.getElementCount();
            int endElementIndex = currentParagraphIndex == startParagraphIndex ? mEndPosition.getElementIndex() : elementCount - 1;
            ParagraphInfo paragraphInfo = makeParagraphLine(mBook,
                    currentParagraphIndex,
                    0,
                    endElementIndex,
                    maxWidth,
                    -1);
            List<LineInfo> pLineInfoList = paragraphInfo.lineInfoList;
            boolean outOfRange = false;
            for (int pLineIndex = pLineInfoList.size() - 1; pLineIndex >= 0; pLineIndex--) {
                LineInfo pLineInfo = pLineInfoList.get(pLineIndex);
                int lineHeight = pLineInfo.lineHeight;
                if (currentHeight + lineHeight <= maxHeight) {
                    mLineInfoList.add(0, pLineInfo);
                    currentHeight += lineHeight;
                    if (pLineInfo.startPosition.getElementIndex() == 0) {
                        currentHeight += mParagraphSpace;
                    }
                    if (currentParagraphIndex != lastParagraphIndex) {
                        paragraphCount++;
                        lastParagraphIndex = currentParagraphIndex;
                    }
                } else {
                    outOfRange = true;
                    mRecycleBin.addScrapView(pLineInfo);
                    pLineInfo.recycle();
                }
            }
            paragraphInfo.recycle();
            if (outOfRange) {
                break;
            }
        }
        if (!mLineInfoList.isEmpty()) {
            TextWordPosition startPosition = mLineInfoList.get(0).startPosition;
            if (TextWordPosition.isStartOfParagraph(mBook, startPosition)) {
                currentHeight -= mParagraphSpace;
            }
            mStartPosition.set(startPosition);
        }
        layoutLineInfo(mLineInfoList, currentHeight, paragraphCount);
    }

    private void layoutChildFromStart() {


        final int maxWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int maxHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int currentHeight = 0;
        int lastParagraphIndex = -1;
        int paragraphCount = 0;
        int startParagraphIndex = mStartPosition.getParagraphIndex();
        int endParagraphIndex = mBook.getParagraphCount() - 1;
        for (int currentParagraphIndex = startParagraphIndex; currentParagraphIndex <= endParagraphIndex; currentParagraphIndex++) {

            long currentTimeMillis = System.currentTimeMillis();
            Paragraph paragraph = mBook.getParagraph(currentParagraphIndex);
            int startElementIndex = currentParagraphIndex == startParagraphIndex ? mStartPosition.getElementIndex() : 0;
            int elementCount = paragraph.getElementCount();
            ParagraphInfo paragraphInfo = makeParagraphLine(mBook,
                    currentParagraphIndex,
                    startElementIndex,
                    elementCount - 1,
                    maxWidth,
                    maxHeight - currentHeight);
            List<LineInfo> pLineInfoList = paragraphInfo.lineInfoList;
            boolean outOfRange = false;
            int pLineSize = pLineInfoList.size();
            if (pLineSize <= 0) {
                break;
            }
            for (int pLineIndex = 0; pLineIndex < pLineSize; pLineIndex++) {
                LineInfo pLineInfo = pLineInfoList.get(pLineIndex);
                int lineHeight = pLineInfo.lineHeight;
                if (currentHeight + lineHeight <= maxHeight) {
                    mLineInfoList.add(pLineInfo);
                    currentHeight += lineHeight;
                    if (pLineInfo.endPosition.getElementIndex() == elementCount - 1) {
                        currentHeight += mParagraphSpace;
                    }
                    if (currentParagraphIndex != lastParagraphIndex) {
                        paragraphCount++;
                        lastParagraphIndex = currentParagraphIndex;
                    }
                } else {
                    outOfRange = true;
                    mRecycleBin.addScrapView(pLineInfo);
                    pLineInfo.recycle();
                }
            }
            paragraphInfo.recycle();
            if (outOfRange) {
                break;
            }
            if (debug) {
                LogUtils.logI(TAG, "======handleParagraph=====cost====" + (System.currentTimeMillis() - currentTimeMillis));
            }
        }

        if (!mLineInfoList.isEmpty()) {
            LineInfo lastLineInfo = mLineInfoList.get(mLineInfoList.size() - 1);
            TextWordPosition endPosition = lastLineInfo.endPosition;
            if (TextWordPosition.isEndOfParagraph(mBook, endPosition)) {
                currentHeight -= mParagraphSpace;
            }
            mEndPosition.set(endPosition);
        }
        layoutLineInfo(mLineInfoList, currentHeight, paragraphCount);
    }

    private void layoutLineInfo(List<LineInfo> lineInfoList, int totalHeight, int paragraphCount) {
        long currentTimeMillis = System.currentTimeMillis();
        int l = getPaddingLeft();
        int t = getPaddingTop();
        int r;
        int b;

        int fixParagraphSpace = (paragraphCount > 1 ? (getHeight() - getPaddingTop() - getPaddingBottom() - totalHeight) / (paragraphCount - 1) : 0) + mParagraphSpace;
        for (LineInfo info : lineInfoList) {
            List<View> lineViews = info.lineViews;
            int lineViewCount = lineViews.size();
            int fixElementSpace = 0;
            if (lineViewCount > 1 && !TextWordPosition.isEndOfParagraph(mBook,info.endPosition)) {
                fixElementSpace = (getWidth() - getPaddingLeft() - getPaddingRight() - info.lineWidth) / (lineViewCount - 1);
            }
            for (View lineView : lineViews) {
                addViewInLayout(lineView,getChildCount(),lineView.getLayoutParams(),true);
                r = l + lineView.getMeasuredWidth();
                b = t + lineView.getMeasuredHeight();
                lineView.layout(l, t, r, b);
                l = r + fixElementSpace;
            }
            l = getPaddingLeft();
            t += info.lineHeight;
            if (info.endPosition.getParagraphIndex() == -1) {
                LogUtils.logI(TAG, "======parag=====cost====");
            }
            if (TextWordPosition.isEndOfParagraph(mBook, info.endPosition)) {
                t += fixParagraphSpace;
            }
        }
        if (debug) {
            LogUtils.logI(TAG, "====layoutLineInfo cost======" + (System.currentTimeMillis() - currentTimeMillis));
        }
    }

    public void setBook(Book book, TextWordPosition position, boolean reverse) {
        mBook = book;
        mPosition = position;
        mReverse = reverse;
        requestLayout();
    }

    public void setAdapter(PageViewAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unRegisterAdapterObserver(mObserver);
            mAdapter = null;
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerAdapterObserver(mObserver);
        }
        requestLayout();
    }

    private final PageViewAdapter.AdapterObserver mObserver = new PageViewAdapter.AdapterObserver() {
        @Override
        public void onDataChanged() {
            requestLayout();
        }
    };

    private View createView(Element element) {

        int itemViewType = mAdapter.getItemType(element);
        View scrapView = mRecycleBin.getScrapView(itemViewType);

        View view = mAdapter.getView(this, element, itemViewType, scrapView);
        if (scrapView != null && scrapView != view) {
            mRecycleBin.addScrapView(itemViewType, scrapView);
        }
//        view.forceLayout();
        LayoutParams layoutParams = ((LayoutParams) view.getLayoutParams());
        if (layoutParams == null) {
            layoutParams = generateDefaultLayoutParams();
            view.setLayoutParams(layoutParams);
        }
        layoutParams.viewType = itemViewType;
        layoutParams.checkViewType();
        return view;
    }

    @Override
    public void addView(View child) {
        throw new RuntimeException("cannot add view");
    }


    @Override
    public void removeView(View child) {
        throw new RuntimeException("cannot remove view");
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        private int viewType = -1;

        private void checkViewType() {
            if (viewType == -1) {
                throw new IllegalArgumentException("illegal view type");
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

    }

    public TextWordPosition getEndPosition() {
        return mEndPosition;
    }

    public TextWordPosition getStartPosition() {
        return mStartPosition;
    }

    private static class LineInfo {
        private final List<View> lineViews = new ArrayList<>();
        private final TextWordPosition startPosition = new TextWordPosition();
        private final TextWordPosition endPosition = new TextWordPosition();
        private int lineHeight;
        private int lineWidth;
        private LineInfo next;
        private static LineInfo sPool;

        private LineInfo() {
        }

        static LineInfo obtain() {
            if (sPool == null) {
                return new LineInfo();
            }
            LineInfo info = sPool;
            sPool = info.next;
            return info;
        }

        void recycle() {
            next = sPool;
            sPool = this;
            lineHeight = 0;
            lineWidth = 0;
            lineViews.clear();
            startPosition.reset();
            endPosition.reset();
        }

        void addLineView(View view) {
            lineViews.add(view);
            lineHeight = Math.max(lineHeight, view.getMeasuredHeight());
            lineWidth += view.getMeasuredWidth();
        }
    }

    private static class ParagraphInfo {
        private final List<LineInfo> lineInfoList = new ArrayList<>();
        private ParagraphInfo next;
        private int paragraphHeight;
        static ParagraphInfo sPool;

        ParagraphInfo() {
        }

        static ParagraphInfo obtain() {
            if (sPool == null) {
                return new ParagraphInfo();
            }
            ParagraphInfo info = sPool;
            sPool = info.next;
            return info;
        }

        void recycle() {
            lineInfoList.clear();
            paragraphHeight = 0;
            next = sPool;
            sPool = this;
        }

        void addLineInfo(LineInfo lineInfo) {
            lineInfoList.add(lineInfo);
            paragraphHeight += lineInfo.lineHeight;
        }

    }

    private ParagraphInfo makeParagraphLine(Book book,
                                            int paragraphIndex,
                                            int startElementIndex,
                                            int endElementIndex,
                                            int maxLineWidth,
                                            int maxHeight) {

        long currentTimeMillis = System.currentTimeMillis();

        ParagraphInfo paragraphInfo = ParagraphInfo.obtain();

        Paragraph paragraph = book.getParagraph(paragraphIndex);
        int elementCount = paragraph.getElementCount();
        if (elementCount <= 0) {
            return paragraphInfo;
        }

        if (endElementIndex < 0) {
            endElementIndex = elementCount - 1;
        }
        endElementIndex = Math.min(elementCount - 1, endElementIndex);
        if (startElementIndex < 0) {
            startElementIndex = 0;
        }
        startElementIndex = Math.min(elementCount - 1, startElementIndex);

        LineInfo currentLineInfo = null;
        for (int currentElementIndex = startElementIndex; currentElementIndex <= endElementIndex; currentElementIndex++) {

            Element element = paragraph.getElement(currentElementIndex);
            View child = createView(element);

            int widthMeasureOptions = MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY);
            int heightMeasureOptions = MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY);
            measureChild(child,widthMeasureOptions,heightMeasureOptions);

            int measuredWidth = child.getMeasuredWidth();

            if (currentLineInfo != null && currentLineInfo.lineWidth + measuredWidth > maxLineWidth) {
                paragraphInfo.addLineInfo(currentLineInfo);
                currentLineInfo = null;
            }

            if (currentLineInfo == null) {
                currentLineInfo = LineInfo.obtain();
                currentLineInfo.startPosition.set(paragraphIndex, currentElementIndex);
            }
            currentLineInfo.addLineView(child);
            currentLineInfo.endPosition.set(paragraphIndex, currentElementIndex);

            if (debug) {
//                SLContext.getLogger().logI(TAG, paragraphIndex + "=======add element=====" + ((TextElement) element).getText());
            }
            if (maxHeight > 0 && paragraphInfo.paragraphHeight + currentLineInfo.lineHeight > maxHeight) {
                mRecycleBin.addScrapView(currentLineInfo);
                currentLineInfo.recycle();
                break;
            }

            if (currentElementIndex == endElementIndex) {
                paragraphInfo.addLineInfo(currentLineInfo);
                currentLineInfo = null;
            }
        }
        if (debug) {
//            SLContext.getLogger().logI(TAG,"=======makeParagraphLine cost" + (System.currentTimeMillis() - currentTimeMillis) + "===" + elementCount);
        }
        return paragraphInfo;
    }

    public static class RecycleBin {

        private HashMap<Integer, ViewNode> mScrapViewsMap = new HashMap<>();
        private int mScrapViewCount;

        View getScrapView(int viewType) {
            ViewNode mScrapViews = mScrapViewsMap.get(viewType);
            ViewNode node = mScrapViews;
            if (node != null) {
                mScrapViewCount--;
                mScrapViews = node.next;
                mScrapViewsMap.put(viewType, mScrapViews);
                View view = node.view;
                node.recycle();
                return view;
            }
            return null;
        }

        void addScrapView(int viewType, View view) {
            if (view == null) {
                throw new NullPointerException();
            }

            ViewNode mScrapViews = mScrapViewsMap.get(viewType);
            ViewNode node = ViewNode.obtain();
            node.view = view;
            node.next = mScrapViews;
            mScrapViews = node;
            mScrapViewsMap.put(viewType, mScrapViews);
            mScrapViewCount++;
        }

        void addScrapView(LineInfo lineInfo) {
            for (int i = 0; i < lineInfo.lineViews.size(); i++) {
                View view = lineInfo.lineViews.get(i);
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                int viewType = layoutParams.viewType;
                addScrapView(viewType, view);
            }
        }

        void addChildrenToScrapViews(ViewGroup viewGroup) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                View child = viewGroup.getChildAt(i);
                viewGroup.removeViewInLayout(child);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                addScrapView(layoutParams.viewType, child);
            }
        }

        int getScrapViewCount() {
            return mScrapViewCount;
        }
    }

    private static class ViewNode {

        private View view;
        private ViewNode next;
        private static ViewNode sPool;

        private ViewNode() {
        }

        static ViewNode obtain() {
            if (sPool == null) {
                return new ViewNode();
            }
            ViewNode target = sPool;
            sPool = target.next;
            return target;
        }

        void recycle() {
            next = sPool;
            sPool = this;
            view = null;
        }
    }

    public void setRecycleBin(RecycleBin recycleBin) {
        this.mRecycleBin = recycleBin;
    }


    public boolean isReverse() {
        return mReverse;
    }

    public Book getBook() {
        return mBook;
    }

    public TextWordPosition getPosition() {
        return mPosition;
    }

    public void setParagraphSpace(int paragraphSpace) {
        if (mParagraphSpace != paragraphSpace) {
            this.mParagraphSpace = paragraphSpace;
            requestLayout();
        }
    }
}
