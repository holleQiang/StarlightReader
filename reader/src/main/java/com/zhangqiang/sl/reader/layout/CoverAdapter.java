package com.zhangqiang.sl.reader.layout;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.image.SLColorDrawable;
import com.zhangqiang.sl.framework.layout.SLLinearLayout;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;
import com.zhangqiang.sl.reader.page.DefaultAdapter;
import com.zhangqiang.sl.reader.page.PageView;
import com.zhangqiang.sl.reader.page.PageViewAdapter;
import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.impl.txt.TxtBook;
import com.zhangqiang.sl.reader.position.TextWordPosition;
import com.zhangqiang.sl.reader.view.TopBarView;

public class CoverAdapter extends CoverLayoutAdapter {

    private Book book;
    private PageView.RecycleBin recycleBin;
    private TextWordPosition mPosition;
    private int mTextSize = 50;
    private int mTextColor;
    private int mContentPaddingLeft,mContentPaddingTop,mContentPaddingRight,mContentPaddingBottom;
    private int mTopBarPaddingLeft,mTopBarPaddingTop,mTopBarPaddingRight,mTopBarPaddingBottom;
    private int mParagraphSpace;
    private int mTopBarTextSize = 30;
    private int mTopBarTextColor;

    public CoverAdapter(Book book, TextWordPosition position) {
        this.book = book;
        recycleBin = new PageView.RecycleBin();
        mPosition = position;
    }

    @Override
    public SLView getView(SLViewGroup parent, SLView prevView, SLView convertView, int intent) {
        SLView view;
        SLContext context = parent.getContext();

        SLLinearLayout contentView;
        if (convertView != null) {
            contentView = (SLLinearLayout) convertView;
        } else {
            contentView = new SLLinearLayout(context);
            contentView.addView(new TopBarView(context));
            PageView pageView = new PageView(context);
            pageView.setLayoutParams(new SLLinearLayout.LayoutParams(SLViewGroup.LayoutParams.SIZE_MATCH_PARENT,
                    SLViewGroup.LayoutParams.SIZE_MATCH_PARENT));
            contentView.addView(pageView);
        }

        TopBarView topBarView = (TopBarView) contentView.getChildAt(0);
        topBarView.setTextSize(mTopBarTextSize);
        topBarView.setTextColor(mTopBarTextColor);
        topBarView.setBookName(book.getName());
        topBarView.setPadding(mTopBarPaddingLeft,mTopBarPaddingTop,mTopBarPaddingRight,mTopBarPaddingBottom);

        PageView pageView = (PageView) contentView.getChildAt(1);
        pageView.setRecycleBin(recycleBin);
        pageView.setPadding(mContentPaddingLeft,mContentPaddingTop,mContentPaddingRight,mContentPaddingBottom);
        pageView.setParagraphSpace(mParagraphSpace);

        if (prevView == null) {

            pageView.setBook(book, mPosition, false);
            pageView.setAdapter(makePageViewAdapter());
        } else {

            SLLinearLayout prevContentView = (SLLinearLayout) prevView;
            PageView prevPageView = (PageView) prevContentView.getChildAt(1);
            if (intent == CoverLayout.INTENT_PREVIOUS) {

                TextWordPosition startPosition = prevPageView.getStartPosition();
                if (TextWordPosition.isStartOfBook(book, startPosition)) {
                    return null;
                }
                TextWordPosition position = TextWordPosition.previous(book, startPosition);
                pageView.setBook(book, position, true);
            } else if (intent == CoverLayout.INTENT_NEXT) {

                TextWordPosition endPosition = prevPageView.getEndPosition();
                if (TextWordPosition.isEndOfBook(book, endPosition)) {
                    return null;
                }
                TextWordPosition position = TextWordPosition.next(book, endPosition);
                pageView.setBook(book, position, false);
            }
            pageView.setAdapter(makePageViewAdapter());
        }

        if (book instanceof TxtBook) {
            topBarView.setChapterName(((TxtBook) book).getChapterName(pageView.getPosition()));
        }

        view = contentView;
        view.setBackground(new SLColorDrawable(0xffffffff));
        view.setDrawingCacheEnable(true);
        return view;
    }


    public void setTextSize(int textSize) {
        if (mTextSize != textSize) {
            this.mTextSize = textSize;
            notifyDataChanged();
        }
    }

    public void setTextColor(int textColor) {
        if (mTextColor != textColor) {
            this.mTextColor = textColor;
            notifyDataChanged();
        }
    }

    private PageViewAdapter makePageViewAdapter(){

        DefaultAdapter adapter = new DefaultAdapter();
        adapter.setTextSize(mTextSize);
        adapter.setTextColor(mTextColor);
        return adapter;
    }

    public void setContentPadding(int paddingLeft,int paddingTop,int paddingRight,int paddingBottom) {
        boolean changed = mContentPaddingLeft != paddingLeft
                || mContentPaddingTop != paddingTop
                || mContentPaddingRight != paddingRight
                || mContentPaddingBottom != paddingBottom;
        if (changed) {
            this.mContentPaddingLeft = paddingLeft;
            this.mContentPaddingTop = paddingTop;
            this.mContentPaddingRight = paddingRight;
            this.mContentPaddingBottom = paddingBottom;
            notifyDataChanged();
        }
    }

    public void setTopBarPadding(int paddingLeft,int paddingTop,int paddingRight,int paddingBottom) {
        boolean changed = mTopBarPaddingLeft != paddingLeft
                || mTopBarPaddingTop != paddingTop
                || mTopBarPaddingRight != paddingRight
                || mTopBarPaddingBottom != paddingBottom;
        if (changed) {
            this.mTopBarPaddingLeft = paddingLeft;
            this.mTopBarPaddingTop = paddingTop;
            this.mTopBarPaddingRight = paddingRight;
            this.mTopBarPaddingBottom = paddingBottom;
            notifyDataChanged();
        }
    }

    public void setParagraphSpace(int paragraphSpace) {
        if (mParagraphSpace != paragraphSpace) {
            this.mParagraphSpace = paragraphSpace;
            notifyDataChanged();
        }
    }

    public void setTopBarTextSize(int topBarTextSize) {
        if (mTopBarTextSize != topBarTextSize) {
            this.mTopBarTextSize = topBarTextSize;
            notifyDataChanged();
        }
    }

    public void setTopBarTextColor(int topBarTextColor) {
        if (mTopBarTextColor != topBarTextColor) {
            this.mTopBarTextColor = topBarTextColor;
            notifyDataChanged();
        }
    }
}
