package com.zhangqiang.sl.reader.view;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.layout.SLLinearLayout;
import com.zhangqiang.sl.framework.text.SingleLineTextView;
import com.zhangqiang.sl.framework.view.Gravity;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

public class BottomBarView extends SLLinearLayout {


    private final BatteryView batteryView;
    private final SingleLineTextView dateView;
    private final SingleLineTextView progressView;

    public BottomBarView(SLContext context) {
        super(context);

        setOrientation(ORIENTATION_HORIZONTAL);
        batteryView = new BatteryView(context);
        LayoutParams batteryLayoutParams = new LayoutParams(SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT, SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT);
        batteryLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        batteryView.setLayoutParams(batteryLayoutParams);
        addView(batteryView);


        dateView = new SingleLineTextView(context);
        dateView.setText("你好啊");
        dateView.setTextColor(0xff333333);
        dateView.setTextSize(30);
        LayoutParams dateLayoutParams = new LayoutParams(SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT, SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT);
        dateLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        dateView.setLayoutParams(dateLayoutParams);
        addView(dateView);


        SLView emptyView = new SLView(context);
        SLLinearLayout.LayoutParams layoutParams = new SLLinearLayout.LayoutParams(0, 0);
        layoutParams.weight = 1;
        emptyView.setLayoutParams(layoutParams);
        addView(emptyView);

        progressView = new SingleLineTextView(context);
        progressView.setText("你好啊");
        progressView.setTextSize(30);
        progressView.setTextColor(0xff333333);
        LayoutParams progressLayoutParams = new LayoutParams(SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT, SLViewGroup.LayoutParams.SIZE_WRAP_CONTENT);
        progressLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        progressView.setLayoutParams(progressLayoutParams);
        addView(progressView);
    }

    public BatteryView getBatteryView() {
        return batteryView;
    }

    public SingleLineTextView getDateView() {
        return dateView;
    }

    public SingleLineTextView getProgressView() {
        return progressView;
    }
}
