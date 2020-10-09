package com.zhangqiang.sl.reader.view;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.view.MeasureOptions;
import com.zhangqiang.sl.framework.view.SLView;

public class TopBarView extends SLView {

    private String bookName;
    private String chapterName;
    private SLPaint mPaint;

    public TopBarView(SLContext context) {
        super(context);
        mPaint = context.newPaint();
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        setMeasuredResult(resolveSizeAndState(MeasureOptions.getSize(widthOptions), widthOptions),
                resolveSizeAndState((int) Math.ceil(mPaint.getTextHeight() + getPaddingTop() + getPaddingBottom()), heightOptions));
    }

    @Override
    protected void onDraw(SLCanvas canvas) {
        super.onDraw(canvas);
        float topOffset = ((getHeight() - getPaddingTop() - getPaddingBottom()) - mPaint.getTextHeight()) / 2 + getPaddingTop();
        int save = canvas.save();
        canvas.translate(0, topOffset);
        if (bookName != null && bookName.length() > 0) {
            canvas.drawText(bookName, 0, bookName.length(), getPaddingLeft(), -mPaint.ascent(), mPaint);
        }
        if (chapterName != null && chapterName.length() > 0) {
            int length = chapterName.length();
            float chapterWidth = mPaint.measureText(chapterName, 0, length);
            canvas.drawText(chapterName, 0, chapterName.length(), getWidth() - getPaddingRight() - chapterWidth, -mPaint.ascent(), mPaint);
        }
        canvas.restoreToCount(save);
    }

    public float getTextSize() {
        return mPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        if (this.mPaint.getTextSize() != textSize) {
            mPaint.setTextSize(textSize);
            requestLayout();
        }
    }

    public float getTextColor() {
        return mPaint.getColor();
    }

    public void setTextColor(int textColor) {
        if (this.mPaint.getColor() != textColor) {
            this.mPaint.setColor(textColor);
            invalidate();
        }
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        if (this.bookName == null && bookName != null
                || this.bookName != null && !this.bookName.equals(bookName)) {
            this.bookName = bookName;
            requestLayout();
        }
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        if (this.chapterName == null && chapterName != null
                || this.chapterName != null && !this.chapterName.equals(chapterName)) {
            this.chapterName = chapterName;
            requestLayout();
        }
    }
}
