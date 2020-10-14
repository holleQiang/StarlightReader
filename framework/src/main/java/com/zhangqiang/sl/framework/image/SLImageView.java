package com.zhangqiang.sl.framework.image;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.view.SLView;

public class SLImageView extends SLView {

    private SLDrawable imageDrawable;

    public SLImageView(SLContext context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        int width = imageDrawable == null ? 0 : imageDrawable.getIntrinsicWidth();
        int height = imageDrawable == null ? 0 : imageDrawable.getIntrinsicHeight();
        setMeasuredResult(resolveSizeAndState(width, widthOptions), resolveSizeAndState(height, heightOptions));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (imageDrawable != null) {
            imageDrawable.setBounds(0,0,getWidth(),getHeight());
        }
    }

    @Override
    protected void onDraw(SLCanvas canvas) {
        super.onDraw(canvas);
        if (imageDrawable != null) {
            imageDrawable.draw(canvas);
        }
    }

    public SLDrawable getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(SLDrawable image) {
        this.imageDrawable = image;
        requestLayout();
    }
}
