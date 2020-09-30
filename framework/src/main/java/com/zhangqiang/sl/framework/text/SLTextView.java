package com.zhangqiang.sl.framework.text;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.view.MeasureOptions;
import com.zhangqiang.sl.framework.view.SLView;

public class SLTextView extends SLView {

    private final boolean debug = false;
    private static final String TAG = SLTextView.class.getCanonicalName();
    private SLPaint mPaint;
    private CharSequence mText;

    public SLTextView(SLContext context) {
        super(context);
        mPaint = context.newPaint();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (debug) {
            SLContext.getLogger().logI(TAG, "onLayout:left =" + left + ",top = " + top + ",right = " + right + ",bottom = " + bottom);
        }
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        if (mText != null && mText.length() > 0) {

            int textWidth;
            if (mText.length() == 1) {
                char c = mText.charAt(0);
                float textSize = mPaint.getTextSize();
                float width = CharMeasuredCache.get(c, textSize);
                if (width < 0) {
                    width = (int) mPaint.measureText(String.valueOf(c), 0, 1);
                    CharMeasuredCache.put(c,textSize,width);
                }
                textWidth = (int) width;
            } else {
                textWidth = (int) mPaint.measureText(mText.toString(), 0, mText.length());
            }
            int textHeight = (int) mPaint.getTextHeight();
            setMeasuredResult(resolveSizeAndState(textWidth, widthOptions), resolveSizeAndState(textHeight, heightOptions));
        } else {
            setMeasuredResult(resolveSizeAndState(0, widthOptions), resolveSizeAndState(0, heightOptions));
        }
    }

    @Override
    protected void onDraw(SLCanvas canvas) {
        super.onDraw(canvas);
        if (debug) {
            SLContext.getLogger().logI(TAG, "onDraw:left =" + getLeft() + ",top = " + getTop() + ",right = " + getRight() + ",bottom = " + getBottom());
        }

        if (mText != null && mText.length() > 0) {
            canvas.drawText(mText.toString(), 0, mText.length(), 0, -mPaint.ascent(), mPaint);
        }
    }

    public CharSequence getText() {
        return mText;
    }

    public void setText(CharSequence text) {
        if (this.mText != text) {
            this.mText = text;
            if (debug) {
                SLContext.getLogger().logI(TAG, "设置文字：" + text);
            }
            requestLayout();
        }
    }

    public float getTextSize() {
        return mPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        if (mPaint.getTextSize() != textSize) {
            this.mPaint.setTextSize(textSize);
            requestLayout();
        }
    }

    public SLPaint getPaint() {
        return mPaint;
    }

    public int getTextLength() {
        CharSequence text = getText();
        if (text == null) {
            return 0;
        }
        return text.length();
    }

    public void setTextColor(int color){
        if (mPaint.getColor() != color) {
            mPaint.setColor(color);
            invalidate();
        }
    }
}
