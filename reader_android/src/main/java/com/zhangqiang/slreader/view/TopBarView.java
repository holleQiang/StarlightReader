package com.zhangqiang.slreader.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TopBarView extends LinearLayout {

    private final TextView bookNameView;
    private final TextView chapterNameView;

    public TopBarView(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        bookNameView = new TextView(context);
        LayoutParams bookNameLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bookNameLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        bookNameView.setLayoutParams(bookNameLayoutParams);
        addView(bookNameView);

        View emptyView = new View(context);
        LayoutParams emptyLayoutParams = new LayoutParams(0, 0);
        emptyLayoutParams.weight = 1;
        emptyView.setLayoutParams(emptyLayoutParams);
        addView(emptyView);

        chapterNameView = new TextView(context);
        LayoutParams chapterNameLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
