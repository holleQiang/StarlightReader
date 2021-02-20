package com.zhangqiang.starlightreader.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;

public class WindowBottomPlaceholderView extends View {

    private int mSystemWindowInsetBottom;

    public WindowBottomPlaceholderView(Context context) {
        super(context);
    }


    public WindowBottomPlaceholderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowBottomPlaceholderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                resolveSize(Math.max(0, mSystemWindowInsetBottom), heightMeasureSpec));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                int systemWindowInsetBottom = windowInsetsCompat.getSystemWindowInsetBottom();
                if (mSystemWindowInsetBottom != systemWindowInsetBottom) {
                    mSystemWindowInsetBottom = systemWindowInsetBottom;
                    requestLayout();
                }
                return windowInsetsCompat;
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewCompat.setOnApplyWindowInsetsListener(this, null);
    }

}
