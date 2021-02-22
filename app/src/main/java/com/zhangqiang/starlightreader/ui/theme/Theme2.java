package com.zhangqiang.starlightreader.ui.theme;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class Theme2 extends PageTheme{

    public static final String ID = "2";

    public Theme2() {
        super(ID);
    }

    @Override
    public Drawable getBackground() {
        return new ColorDrawable(0xFFDCC5A6);
    }

    @Override
    public int getTxtColor() {
        return 0xFF2B2519;
    }
}
