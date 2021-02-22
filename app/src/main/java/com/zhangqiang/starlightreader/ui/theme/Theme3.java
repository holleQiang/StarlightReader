package com.zhangqiang.starlightreader.ui.theme;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class Theme3 extends PageTheme{

    public static final String ID = "3";

    public Theme3() {
        super(ID);
    }

    @Override
    public Drawable getBackground() {
        return new ColorDrawable(0xFFCEEDD1);
    }

    @Override
    public int getTxtColor() {
        return 0xFF353E33;
    }
}
