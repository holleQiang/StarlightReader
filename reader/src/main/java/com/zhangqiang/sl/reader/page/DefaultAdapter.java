package com.zhangqiang.sl.reader.page;

import com.zhangqiang.sl.framework.image.SLColorDrawable;
import com.zhangqiang.sl.framework.image.SLImageView;
import com.zhangqiang.sl.framework.text.SingleLineTextView;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;
import com.zhangqiang.sl.reader.parser.Element;
import com.zhangqiang.sl.reader.parser.impl.txt.TextElement;

public class DefaultAdapter extends PageViewAdapter {

    public static final int ITEM_TYPE_TEXT = 0;
    public static final int ITEM_TYPE_IMAGE = 1;
    private float mTextSize = 50;
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
    public SLView getView(SLViewGroup parent, Element element, int itemType, SLView convertView) {
        if (itemType == ITEM_TYPE_TEXT) {

            SingleLineTextView textView;
            if (convertView == null) {
                textView = new SingleLineTextView(parent.getContext());
            } else {
                textView = (SingleLineTextView) convertView;
            }

            textView.setText(((TextElement) element).getText());
            textView.setTextSize(mTextSize);
            textView.setTextColor(mTextColor);
            textView.setLineHeightMultiple(mLineHeightMultiple);
            return textView;
        } else if (itemType == ITEM_TYPE_IMAGE) {
            SLImageView imageView;
            if (convertView == null) {
                imageView = new SLImageView(parent.getContext());
            } else {
                imageView = ((SLImageView) convertView);
            }
            imageView.setImageDrawable(new SLColorDrawable(0xFFFF0000));
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
