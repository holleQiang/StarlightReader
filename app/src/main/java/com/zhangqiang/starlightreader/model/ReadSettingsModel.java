package com.zhangqiang.starlightreader.model;

import android.content.Context;
import android.graphics.Color;

import com.zhangqiang.options.Option;
import com.zhangqiang.options.Options;
import com.zhangqiang.options.store.ValueStore;
import com.zhangqiang.options.store.shared.SharedValueStore;

public class ReadSettingsModel {

    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";
    public static final String CHARSET_ISO_8859_1 = "ISO_8859_1";
    public static final String[] supportCharsets = new String[]{CHARSET_UTF_8, CHARSET_GBK, CHARSET_ISO_8859_1};
    private static final int MIN_FONT_SIZE_SP = 15;
    private static final int MAX_FONT_SIZE_SP = 35;
    private final Option<Float> mTxtSizeOption;
    private final Option<String> mTxtCharsetOption;
    private final Option<Integer> mTxtColorOption;
    private final Option<Boolean> mTxtSimpleOption;
    private static volatile ReadSettingsModel instance;

    private ReadSettingsModel(Context context) {
        ValueStore valueStore = new SharedValueStore(context, "reader_config");
        mTxtSizeOption = Options.ofFloat("reader_font_size", 20f, valueStore);
        mTxtCharsetOption = Options.ofString("reader_charset", supportCharsets[0], valueStore);
        mTxtColorOption = Options.ofInt("reader_txt_color", Color.parseColor("#333333"), valueStore);
        mTxtSimpleOption = Options.ofBoolean("reader_txt_simple", true, valueStore);
    }

    public static ReadSettingsModel getInstance(Context context) {
        if (instance == null) {
            synchronized (ReadSettingsModel.class) {
                if (instance == null) {
                    instance = new ReadSettingsModel(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public Option<Float> getTxtSizeOption() {
        return mTxtSizeOption;
    }

    public float getTxtSize() {
        return mTxtSizeOption.get();
    }

    public void setTextSize(float textSize) {
        mTxtSizeOption.set(Math.max(MIN_FONT_SIZE_SP, Math.min(textSize, MAX_FONT_SIZE_SP)));
    }

    public void setTxtSizeFactor(float factor) {
        float targetFontSize = (MAX_FONT_SIZE_SP - MIN_FONT_SIZE_SP) * factor + MIN_FONT_SIZE_SP;
        setTextSize(targetFontSize);
    }

    public float getTxtSizeFactor() {
        return (getTxtSize() - MIN_FONT_SIZE_SP) / (MAX_FONT_SIZE_SP - MIN_FONT_SIZE_SP);
    }

    public String getTxtCharset() {
        return mTxtCharsetOption.get();
    }

    public void setTxtCharset(String charset) {
        mTxtCharsetOption.set(charset);
    }

    public int getTxtColor() {
        return mTxtColorOption.get();
    }

    public void setTxtColor(int color) {
        mTxtColorOption.set(color);
    }

    public Option<String> getTxtCharsetOption() {
        return mTxtCharsetOption;
    }

    public Option<Integer> getTxtColorOption() {
        return mTxtColorOption;
    }

    public Option<Boolean> getTxtSimpleOption() {
        return mTxtSimpleOption;
    }

    public boolean isTxtSimple(){
        return mTxtSimpleOption.get();
    }

    public void setTxtSimple(boolean simple){
        mTxtSimpleOption.set(simple);
    }
}
