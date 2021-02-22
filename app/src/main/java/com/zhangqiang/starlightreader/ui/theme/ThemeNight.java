package com.zhangqiang.starlightreader.ui.theme;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class ThemeNight extends PageTheme{

    public static final String ID = "5";

    public ThemeNight() {
        super(ID);
    }

    @Override
    public Drawable getBackground() {
        return new ColorDrawable(0xFF0E0E0E);
    }

    @Override
    public int getTxtColor() {
        return 0xFF5B5B5B;
    }
}
