package com.zhangqiang.starlightreader.ui.theme;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class Theme1 extends PageTheme{

    public static final String ID = "1";

    public Theme1() {
        super(ID);
    }

    @Override
    public Drawable getBackground() {
        return new ColorDrawable(0xFFF0ECE2);
    }

    @Override
    public int getTxtColor() {
        return 0xff202020;
    }
}
