package com.zhangqiang.sl.reader.layout;

import com.zhangqiang.sl.framework.image.SLColorDrawable;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;
import com.zhangqiang.sl.reader.layout.CoverLayout;
import com.zhangqiang.sl.reader.page.DefaultAdapter;
import com.zhangqiang.sl.reader.page.PageView;
import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.position.TextWordPosition;

public class CoverAdapter extends Adapter {

    private Book book;
    private PageView.RecycleBin recycleBin;
    private TextWordPosition mPosition;
    private DefaultAdapter mPageAdapter = new DefaultAdapter();

    public CoverAdapter(Book book, TextWordPosition position) {
        this.book = book;
        recycleBin = new PageView.RecycleBin();
        mPosition = position;
    }

    @Override
    public SLView getView(SLViewGroup parent, SLView prevView, SLView convertView, int intent) {
        SLView view;
        if (prevView == null) {
            PageView pageView;
            if (convertView != null) {
                pageView = (PageView) convertView;
            } else {
                pageView = new PageView(parent.getContext());
                pageView.setRecycleBin(recycleBin);
            }
            pageView.setBook(book, mPosition, false);
            pageView.setAdapter(mPageAdapter);
            view = pageView;
        } else {

            PageView pageView;
            if (convertView != null) {
                pageView = (PageView) convertView;
            } else {
                pageView = new PageView(parent.getContext());
                pageView.setRecycleBin(recycleBin);
            }
            PageView prevPageView = (PageView) prevView;
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
            pageView.setAdapter(mPageAdapter);
            view = pageView;
        }
        view.setBackground(new SLColorDrawable(0xffffffff));
//        view.setDrawingCacheEnable(true);
        return view;
    }


    public DefaultAdapter getPageAdapter() {
        return mPageAdapter;
    }
}
