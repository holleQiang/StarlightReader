package com.zhangqiang.starlightreader.ui.theme;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public abstract class PageTheme {

    private String id;

    public PageTheme(String id) {
        this.id = id;
    }

    public abstract Drawable getBackground();

    public abstract int getTxtColor();

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageTheme pageTheme = (PageTheme) o;
        return TextUtils.equals(id, pageTheme.id);
    }
}
