package com.zhangqiang.starlightreader.ui.curl;

import android.content.Context;

import com.zhangqiang.slreader.parser.Book;
import com.zhangqiang.slreader.position.TextWordPosition;


public class PageProvider implements CurlView.PageProvider {

    private Context context;
    private Book book;
    private TextWordPosition position;
    private OnPageChangedListener listener;

    public PageProvider(Context context, Book book, TextWordPosition position) {
        this.context = context;
        this.book = book;
        this.position = position;
    }

    @Override
    public int getPageCount() {
        return 1;
    }

    @Override
    public void updatePage(final CurlPage page, int width, int height, int index) {

    }

    public interface OnPageChangedListener{

        void onPageChanged();
    }

    public void setListener(OnPageChangedListener listener) {
        this.listener = listener;
    }
}
