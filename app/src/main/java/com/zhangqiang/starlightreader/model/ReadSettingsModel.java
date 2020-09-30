package com.zhangqiang.starlightreader.model;

import android.content.Context;
import android.graphics.Color;

import com.zhangqiang.options.Option;
import com.zhangqiang.options.Options;
import com.zhangqiang.options.store.ValueStore;
import com.zhangqiang.options.store.shared.SharedValueStore;

public class ReadSettingsModel {

    public static final String [] supportCharsets = new String[]{"UTF-8","GBK","Unicode","UTF-16BE","UTF-16LE","ANSI|ASCII"};
    private static final int MIN_FONT_SIZE_SP = 15;
    private static final int MAX_FONT_SIZE_SP = 35;
    private static Option<Float> txtSizeOption;
    private static Option<String> txtCharsetOption;
    private static Option<Integer> txtColorOption;
    private static boolean init = false;

    public static void init(Context context) {

        if (init) {
            return;
        }
        init = true;
        ValueStore valueStore = new SharedValueStore(context, "reader_config");
        txtSizeOption = Options.ofFloat("reader_font_size", (float) MIN_FONT_SIZE_SP + (MAX_FONT_SIZE_SP - MIN_FONT_SIZE_SP) / 2, valueStore);
        txtCharsetOption = Options.ofString("reader_charset", supportCharsets[0], valueStore);
        txtColorOption = Options.ofInt("reader_txt_color", Color.parseColor("#333333"), valueStore);
    }

    public static float getTxtSizeRangeFactor() {

        return (txtSizeOption.get() - MIN_FONT_SIZE_SP) / (MAX_FONT_SIZE_SP - MIN_FONT_SIZE_SP);
    }

    public static Option<Float> getTxtSizeOption() {
        return txtSizeOption;
    }

    public static float getTxtSize() {
        return txtSizeOption.get();
    }

    public static void setTxtSize(float factor) {
        float targetFontSize = (MAX_FONT_SIZE_SP - MIN_FONT_SIZE_SP) * factor + MIN_FONT_SIZE_SP;
        txtSizeOption.set(targetFontSize);
    }

    public static String getTxtCharset() {
        return txtCharsetOption.get();
    }

    public static void setTxtCharset(String charset) {
        txtCharsetOption.set(charset);
    }

    public static int getTxtColor() {
        return txtColorOption.get();
    }

    public static void setTxtColor(int color) {
        txtColorOption.set(color);
    }

    public static Option<String> getTxtCharsetOption() {
        return txtCharsetOption;
    }

    public static Option<Integer> getTxtColorOption() {
        return txtColorOption;
    }
}
