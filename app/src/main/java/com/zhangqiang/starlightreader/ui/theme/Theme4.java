package com.zhangqiang.starlightreader.ui.theme;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class Theme4 extends PageTheme{

    public static final String ID = "4";

    public Theme4() {
        super(ID);
    }

    @Override
    public Drawable getBackground() {
        return new ColorDrawable(0xFF3C4143);
    }

    @Override
    public int getTxtColor() {
        return 0xFFB2B2B2;
    }
}
