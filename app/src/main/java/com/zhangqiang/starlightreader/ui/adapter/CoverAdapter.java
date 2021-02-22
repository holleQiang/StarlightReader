package com.zhangqiang.starlightreader.ui.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhangqiang.slreader.PageView;
import com.zhangqiang.slreader.PageViewAdapter;
import com.zhangqiang.slreader.parser.Book;
import com.zhangqiang.slreader.parser.impl.txt.TxtBook;
import com.zhangqiang.slreader.position.TextWordPosition;
import com.zhangqiang.slreader.view.BatteryView;
import com.zhangqiang.slreader.view.BottomBarView;
import com.zhangqiang.slreader.view.CoverLayout;
import com.zhangqiang.slreader.view.TopBarView;
import com.zhangqiang.starlightreader.ui.theme.PageTheme;
import com.zhangqiang.starlightreader.ui.theme.Theme1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CoverAdapter extends CoverLayout.Adapter {

    private Book book;
    private PageView.RecycleBin recycleBin = new PageView.RecycleBin();
    private PositionFactory mPositionFactory;
    private float mTextSize = 25;
    private int mContentPaddingLeft, mContentPaddingTop, mContentPaddingRight, mContentPaddingBottom;
    private int mTopBarPaddingLeft, mTopBarPaddingTop, mTopBarPaddingRight, mTopBarPaddingBottom;
    private int mBottomBarPaddingLeft, mBottomBarPaddingTop, mBottomBarPaddingRight, mBottomBarPaddingBottom;
    private int mParagraphSpace;
    private float mTopBarTextSize = 25;
    private int mTopBarTextColor;
    private int mTopBarHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private float mLineHeightMultiple = 1f;
    private int mBottomBarDatePaddingLeft;
    private int mBottomBarTextSize;
    private int mBottomBarTextColor;
    private float mBottomBarBattery;
    private int mBottomBarBatteryColor;
    private int mBottomBarBatteryHeaderWidth;
    private int mBottomBarBatteryHeaderHeight;
    private int mBottomBarBatteryBodyWidth;
    private int mBottomBarBatteryBodyHeight;
    private int mBottomBarBatteryBodyBorderWidth;
    private boolean mTextSimple;
    private PageTheme mPageTheme = new Theme1();

    public CoverAdapter(Book book, PositionFactory factory) {
        this.book = book;
        mPositionFactory = factory;
    }

    @Override
    public View getView(ViewGroup parent, View prevView, View convertView, int intent) {
        View view;
        Context context = parent.getContext();

        LinearLayout contentView;
        if (convertView != null) {
            contentView = (LinearLayout) convertView;
        } else {
            contentView = new LinearLayout(context);
            contentView.setOrientation(LinearLayout.VERTICAL);
            TopBarView topBarView = new TopBarView(context);
            LinearLayout.LayoutParams topBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            topBarView.setLayoutParams(topBarLayoutParams);
            contentView.addView(topBarView);
            PageView pageView = new PageView(context);
            LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            contentLayoutParams.weight = 1;
            pageView.setLayoutParams(contentLayoutParams);
            contentView.addView(pageView);

            BottomBarView bottomBarView = new BottomBarView(context);
            LinearLayout.LayoutParams bottomBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            bottomBarView.setLayoutParams(bottomBarLayoutParams);
            contentView.addView(bottomBarView);
        }

        TopBarView topBarView = (TopBarView) contentView.getChildAt(0);
        topBarView.setTextSize(mTopBarTextSize);
        topBarView.setTextColor(mTopBarTextColor);
        topBarView.setBookName(book.getName());
        topBarView.setPadding(mTopBarPaddingLeft, mTopBarPaddingTop, mTopBarPaddingRight, mTopBarPaddingBottom);
        topBarView.getLayoutParams().height = mTopBarHeight;

        PageView pageView = (PageView) contentView.getChildAt(1);
        pageView.setRecycleBin(recycleBin);
        pageView.setPadding(mContentPaddingLeft, mContentPaddingTop, mContentPaddingRight, mContentPaddingBottom);
        pageView.setParagraphSpace(mParagraphSpace);

        BottomBarView bottomBar = (BottomBarView) contentView.getChildAt(2);
        bottomBar.setPadding(mBottomBarPaddingLeft, mBottomBarPaddingTop, mBottomBarPaddingRight, mBottomBarPaddingBottom);
        BatteryView batteryView = bottomBar.getBatteryView();
        batteryView.setBatteryLevel(mBottomBarBattery);
        bottomBar.getDateView().setTextSize(mBottomBarTextSize);
        bottomBar.getDateView().setTextColor(mBottomBarTextColor);
        bottomBar.getDateView().setPadding(mBottomBarDatePaddingLeft, 0, 0, 0);
        bottomBar.getDateView().setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
        bottomBar.getProgressView().setTextSize(mBottomBarTextSize);
        bottomBar.getProgressView().setTextColor(mBottomBarTextColor);
        batteryView.setBatteryColor(mBottomBarBatteryColor);
        batteryView.setBatteryBodyBorderWidth(mBottomBarBatteryBodyBorderWidth);
        batteryView.setBatteryBodyHeight(mBottomBarBatteryBodyHeight);
        batteryView.setBatteryBodyWidth(mBottomBarBatteryBodyWidth);
        batteryView.setBatteryHeadHeight(mBottomBarBatteryHeaderHeight);
        batteryView.setBatteryHeadWidth(mBottomBarBatteryHeaderWidth);

        if (prevView == null) {

            pageView.setBook(book, mPositionFactory.getReadPosition(), false);
        } else {

            LinearLayout prevContentView = (LinearLayout) prevView;
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
        }
        pageView.setAdapter(makePageViewAdapter());

        if (book instanceof TxtBook) {
            topBarView.setChapterName(((TxtBook) book).getChapterName(pageView.getPosition()));
        }
        int paragraphIndex = pageView.getStartPosition().getParagraphIndex();
        int paragraphCount = book.getParagraphCount();
        bottomBar.getProgressView().setText(paragraphIndex + "/" + paragraphCount);

        view = contentView;
        ViewCompat.setBackground(view,mPageTheme.getBackground());
        return view;
    }


    public void setTextSize(float textSize) {
        if (mTextSize != textSize) {
            this.mTextSize = textSize;
            notifyDataChanged();
        }
    }

    private PageViewAdapter makePageViewAdapter() {

        DefaultPageViewAdapter adapter = new DefaultPageViewAdapter();
        adapter.setTextSize(mTextSize);
        adapter.setTextColor(mPageTheme.getTxtColor());
        adapter.setLineHeightMultiple(mLineHeightMultiple);
        adapter.setTextSimple(mTextSimple);
        return adapter;
    }

    public void setContentPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
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

    public void setTopBarPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
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

    public void setBottomBarPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        boolean changed = mBottomBarPaddingLeft != paddingLeft
                || mBottomBarPaddingTop != paddingTop
                || mBottomBarPaddingRight != paddingRight
                || mBottomBarPaddingBottom != paddingBottom;
        if (changed) {
            this.mBottomBarPaddingLeft = paddingLeft;
            this.mBottomBarPaddingTop = paddingTop;
            this.mBottomBarPaddingRight = paddingRight;
            this.mBottomBarPaddingBottom = paddingBottom;
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

    public void setTextSimple(boolean textSimple) {
        if (mTextSimple != textSimple) {
            mTextSimple = textSimple;
            notifyDataChanged();
        }
    }

    public interface PositionFactory {

        TextWordPosition getReadPosition();
    }

    public void setLineHeightMultiple(float lineHeightMultiple) {
        if (mLineHeightMultiple != lineHeightMultiple) {
            this.mLineHeightMultiple = lineHeightMultiple;
            notifyDataChanged();
        }
    }

    public void setBottomBarDatePaddingLeft(int bottomBarBatteryPadding) {
        if (mBottomBarDatePaddingLeft != bottomBarBatteryPadding) {
            this.mBottomBarDatePaddingLeft = bottomBarBatteryPadding;
            notifyDataChanged();
        }
    }

    public void setBottomBarTextSize(int bottomBarTextSize) {
        if (mBottomBarTextSize != bottomBarTextSize) {
            this.mBottomBarTextSize = bottomBarTextSize;
            notifyDataChanged();
        }
    }

    public void setBottomBarTextColor(int bottomBarTextColor) {
        if (mBottomBarTextColor != bottomBarTextColor) {
            this.mBottomBarTextColor = bottomBarTextColor;
            notifyDataChanged();
        }
    }

    public void setBottomBarBattery(float bottomBarBattery) {
        if (mBottomBarBattery != bottomBarBattery) {
            this.mBottomBarBattery = bottomBarBattery;
            notifyDataChanged();
        }
    }

    public void setBottomBarBatteryColor(int bottomBarBatteryColor) {
        if (mBottomBarBatteryColor != bottomBarBatteryColor) {
            this.mBottomBarBatteryColor = bottomBarBatteryColor;
            notifyDataChanged();
        }
    }

    public void setBottomBarBatteryHeaderWidth(int bottomBarBatteryHeaderWidth) {
        if (mBottomBarBatteryHeaderWidth != bottomBarBatteryHeaderWidth) {
            this.mBottomBarBatteryHeaderWidth = bottomBarBatteryHeaderWidth;
            notifyDataChanged();
        }
    }

    public void setBottomBarBatteryHeaderHeight(int bottomBarBatteryHeaderHeight) {
        if (mBottomBarBatteryHeaderHeight != bottomBarBatteryHeaderHeight) {
            this.mBottomBarBatteryHeaderHeight = bottomBarBatteryHeaderHeight;
            notifyDataChanged();
        }
    }

    public void setBottomBarBatteryBodyWidth(int bottomBarBatteryBodyWidth) {
        if (mBottomBarBatteryBodyWidth != bottomBarBatteryBodyWidth) {
            this.mBottomBarBatteryBodyWidth = bottomBarBatteryBodyWidth;
            notifyDataChanged();
        }
    }

    public void setBottomBarBatteryBodyHeight(int bottomBarBatteryBodyHeight) {
        if (mBottomBarBatteryBodyHeight != bottomBarBatteryBodyHeight) {
            this.mBottomBarBatteryBodyHeight = bottomBarBatteryBodyHeight;
            notifyDataChanged();
        }
    }

    public void setBottomBarBatteryBodyBorderWidth(int bottomBarBatteryBodyBorderWidth) {
        if (mBottomBarBatteryBodyBorderWidth != bottomBarBatteryBodyBorderWidth) {
            this.mBottomBarBatteryBodyBorderWidth = bottomBarBatteryBodyBorderWidth;
            notifyDataChanged();
        }
    }

    public void setTopBarHeight(int topBarHeight) {
        if (mTopBarHeight != topBarHeight) {
            this.mTopBarHeight = topBarHeight;
            notifyDataChanged();
        }
    }

    public void setPageTheme(PageTheme pageTheme) {
        if(this.mPageTheme != pageTheme){
            this.mPageTheme = pageTheme;
            notifyDataChanged();
        }
    }
}
