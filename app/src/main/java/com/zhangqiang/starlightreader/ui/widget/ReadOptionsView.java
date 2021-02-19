package com.zhangqiang.starlightreader.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.zhangqiang.starlightreader.R;

public class ReadOptionsView extends ConstraintLayout {

    public ReadOptionsView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ReadOptionsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ReadOptionsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.dialog_reader_settings, this);
    }
}
