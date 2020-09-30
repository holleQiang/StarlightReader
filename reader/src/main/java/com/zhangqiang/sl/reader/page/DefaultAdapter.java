package com.zhangqiang.sl.reader.page;

import com.zhangqiang.sl.framework.image.SLColorDrawable;
import com.zhangqiang.sl.framework.image.SLImageView;
import com.zhangqiang.sl.framework.text.SLTextView;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;
import com.zhangqiang.sl.reader.parser.Element;
import com.zhangqiang.sl.reader.parser.impl.TextElement;

public class DefaultAdapter extends Adapter {

    public static final int ITEM_TYPE_TEXT = 0;
    public static final int ITEM_TYPE_IMAGE = 1;
    private float textSize = 50;
    private int textColor = 0xff000000;

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

            SLTextView textView;
            if (convertView == null) {
                textView = new SLTextView(parent.getContext());
            } else {
                textView = (SLTextView) convertView;
            }

            textView.setText(((TextElement) element).getText());
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
//            textView.setDrawingCacheEnable(true);
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
        if (this.textSize != textSize) {
            this.textSize = textSize;
            requestLayout();
        }
    }

    public void setTextColor(int textColor) {
        if (this.textColor != textColor) {
            this.textColor = textColor;
            requestInvalidate();
        }
    }


}
