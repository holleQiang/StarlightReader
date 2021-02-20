package com.zhangqiang.slreader.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomBarView extends LinearLayout {


    private final BatteryView batteryView;
    private final TextView dateView;
    private final TextView progressView;

    public BottomBarView(Context context) {
        super(context);

        setOrientation(HORIZONTAL);
        batteryView = new BatteryView(context);
        LayoutParams batteryLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        batteryLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        batteryView.setLayoutParams(batteryLayoutParams);
        addView(batteryView);


        dateView = new TextView(context);
        dateView.setText("你好啊");
        dateView.setTextColor(0xff333333);
        dateView.setTextSize(30);
        LayoutParams dateLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        dateView.setLayoutParams(dateLayoutParams);
        addView(dateView);


        View emptyView = new View(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
        layoutParams.weight = 1;
        emptyView.setLayoutParams(layoutParams);
        addView(emptyView);

        progressView = new TextView(context);
        progressView.setText("你好啊");
        progressView.setTextSize(30);
        progressView.setTextColor(0xff333333);
        LayoutParams progressLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        progressView.setLayoutParams(progressLayoutParams);
        addView(progressView);
    }

    public BatteryView getBatteryView() {
        return batteryView;
    }

    public TextView getDateView() {
        return dateView;
    }

    public TextView getProgressView() {
        return progressView;
    }
}
