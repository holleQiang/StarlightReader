package com.zhangqiang.sl.android.widget;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.zhangqiang.sl.android.render.canvas.AndroidCanvas;
import com.zhangqiang.sl.android.AndroidPaint;
import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.text.SingleLineTextView;
import com.zhangqiang.sl.framework.view.MeasureOptions;

public class AndTextView extends SingleLineTextView {

    private StaticLayout staticLayout;

    public AndTextView(SLContext context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        int size = MeasureOptions.getSize(widthOptions);
        staticLayout = newStaticLayout(getText(),0,getTextLength(),size);
        int lineCount = staticLayout.getLineCount();
        int maxWidth = 0;
        for (int i = 0; i < lineCount; i++) {
            maxWidth = (int) Math.max(maxWidth, staticLayout.getLineWidth(i));
        }
        setMeasuredResult(resolveSizeAndState(maxWidth,widthOptions),resolveSizeAndState(staticLayout.getHeight(),heightOptions));
    }

    @Override
    protected void onDraw(SLCanvas canvas) {
        staticLayout.draw(((AndroidCanvas) canvas).getCanvas());
    }

    private StaticLayout newStaticLayout(CharSequence charSequence, int start, int end,int width) {
        SLPaint paint = getPaint();
        TextPaint andPaint = (TextPaint) ((AndroidPaint) paint).getPaint();
        StaticLayout staticLayout;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            staticLayout = StaticLayout.Builder.obtain(charSequence, start, end, andPaint, width)
                    .build();
        } else {
            staticLayout = new StaticLayout(charSequence, start, end, andPaint, width, Layout.Alignment.ALIGN_NORMAL,
                    1, 0, false);
        }
        return staticLayout;
    }
}
