package com.zhangqiang.slreader;


import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangqiang.slreader.parser.Element;
import com.zhangqiang.slreader.parser.impl.txt.TextElement;

public class DefaultAdapter extends PageViewAdapter {

    public static final int ITEM_TYPE_TEXT = 0;
    public static final int ITEM_TYPE_IMAGE = 1;
    private float mTextSize = 20;
    private int mTextColor = 0xff000000;
    private float mLineHeightMultiple = 1f;

    @Override
    public int getItemType(Element element) {

        if (element instanceof TextElement) {
            return ITEM_TYPE_TEXT;
        }
        return -1;
    }

    @Override
    public View getView(ViewGroup parent, Element element, int itemType, View convertView) {
        if (itemType == ITEM_TYPE_TEXT) {

            TextView textView;
            if (convertView == null) {
                textView = new TextView(parent.getContext());
            } else {
                textView = (TextView) convertView;
            }

            textView.setText(((TextElement) element).getText());
            textView.setTextSize(mTextSize);
            textView.setTextColor(mTextColor);
            textView.setLineSpacing(0,mLineHeightMultiple);
            return textView;
        } else if (itemType == ITEM_TYPE_IMAGE) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(parent.getContext());
            } else {
                imageView = ((ImageView) convertView);
            }
            imageView.setImageDrawable(new ColorDrawable(0xFFFF0000));
            imageView.setLayoutParams(new PageView.LayoutParams(120, 120));
            return imageView;
        }
        return null;
    }

    public void setTextSize(float textSize) {
        if (this.mTextSize != textSize) {
            this.mTextSize = textSize;
            notifyDataChanged();
        }
    }

    public void setTextColor(int textColor) {
        if (this.mTextColor != textColor) {
            this.mTextColor = textColor;
            notifyDataChanged();
        }
    }

    public void setLineHeightMultiple(float lineHeightMultiple) {
        if (mLineHeightMultiple != lineHeightMultiple) {
            this.mLineHeightMultiple = lineHeightMultiple;
            notifyDataChanged();
        }
    }
}
