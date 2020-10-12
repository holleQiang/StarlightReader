package com.zhangqiang.sl.reader.view;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.layout.SLLinearLayout;
import com.zhangqiang.sl.framework.text.SingleLineTextView;
import com.zhangqiang.sl.framework.view.Gravity;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

public class TopBarView extends SLLinearLayout {

    private final SingleLineTextView bookNameView;
    private final SingleLineTextView chapterNameView;

    public TopBarView(SLContext context) {
        super(context);
        setOrientation(ORIENTATION_HORIZONTAL);
        bookNameView = new SingleLineTextView(context);
        LayoutParams bookNameLayoutParams = new LayoutParams(SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT, SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT);
        bookNameLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        bookNameView.setLayoutParams(bookNameLayoutParams);
        addView(bookNameView);

        SLView emptyView = new SLView(context);
        LayoutParams emptyLayoutParams = new LayoutParams(0, 0);
        emptyLayoutParams.weight = 1;
        emptyView.setLayoutParams(emptyLayoutParams);
        addView(emptyView);

        chapterNameView = new SingleLineTextView(context);
        LayoutParams chapterNameLayoutParams = new LayoutParams(SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT, SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT);
        chapterNameLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        chapterNameView.setLayoutParams(chapterNameLayoutParams);
        addView(chapterNameView);
    }

    public float getTextSize() {
        return bookNameView.getTextSize();
    }

    public void setTextSize(float textSize) {
        bookNameView.setTextSize(textSize);
        chapterNameView.setTextSize(textSize);
    }

    public int getTextColor() {
        return bookNameView.getTextColor();
    }

    public void setTextColor(int textColor) {
        bookNameView.setTextColor(textColor);
        chapterNameView.setTextColor(textColor);
    }

    public void setBookName(String bookName) {
        bookNameView.setText(bookName);
    }

    public void setChapterName(String chapterName) {
        chapterNameView.setText(chapterName);
    }
}
