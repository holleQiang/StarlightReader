package com.zhangqiang.sl.framework.text;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.view.SLView;

public class SingleLineTextView extends SLView {

    private final boolean debug = false;
    private static final String TAG = SingleLineTextView.class.getCanonicalName();
    private SLPaint mPaint;
    private CharSequence mText;
    private float mLineHeightMultiple = 1f;

    public SingleLineTextView(SLContext context) {
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

            int textWidth = (int) mPaint.measureText(mText.toString(), 0, mText.length());
            int textHeight = (int) (mPaint.getTextHeight() * mLineHeightMultiple);
            setMeasuredResult(resolveSizeAndState(textWidth + getPaddingLeft() + getPaddingRight(), widthOptions),
                    resolveSizeAndState(textHeight + getPaddingTop() + getPaddingBottom(), heightOptions));
        } else {
            setMeasuredResult(resolveSizeAndState(getPaddingLeft() + getPaddingRight(), widthOptions),
                    resolveSizeAndState(getPaddingTop() + getPaddingBottom(), heightOptions));
        }
    }

    @Override
    protected void onDraw(SLCanvas canvas) {
        super.onDraw(canvas);
        if (debug) {
            SLContext.getLogger().logI(TAG, "onDraw:left =" + getLeft() + ",top = " + getTop() + ",right = " + getRight() + ",bottom = " + getBottom());
        }

        if (mText != null && mText.length() > 0) {
            float textHeight = mPaint.getTextHeight();
            int textTop = (int) ((getHeight() - getPaddingTop() - getPaddingBottom() - textHeight) / 2 + getPaddingTop());
            canvas.drawText(mText.toString(), 0, mText.length(), getPaddingLeft(), textTop - mPaint.ascent(), mPaint);
        }
    }

    public CharSequence getText() {
        return mText;
    }

    public void setText(CharSequence text) {
        if (mText == null && text != null || this.mText != null && !mText.equals(text)) {
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

    public void setTextColor(int color) {
        if (mPaint.getColor() != color) {
            mPaint.setColor(color);
            invalidate();
        }
    }

    public float getLineHeightMultiple() {
        return mLineHeightMultiple;
    }

    public void setLineHeightMultiple(float lineHeightMultiple) {
        if (mLineHeightMultiple != lineHeightMultiple) {
            this.mLineHeightMultiple = lineHeightMultiple;
            requestLayout();
        }
    }

    public int getTextColor() {
        return mPaint.getColor();
    }
}
