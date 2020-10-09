package com.zhangqiang.sl.reader.view;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.image.SLDrawable;
import com.zhangqiang.sl.framework.view.SLView;

public class BottomBarView extends SLView {

    private SLDrawable batteryBg;

    public BottomBarView(SLContext context) {
        super(context);
    }


    @Override
    protected void onDraw(SLCanvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        int width = batteryBg.getWidth();
        int height = batteryBg.getHeight();
    }
}
