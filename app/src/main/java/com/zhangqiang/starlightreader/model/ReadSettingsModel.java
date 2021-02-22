package com.zhangqiang.starlightreader.model;

import android.content.Context;
import android.graphics.Color;

import com.zhangqiang.options.Option;
import com.zhangqiang.options.Options;
import com.zhangqiang.options.store.ValueStore;
import com.zhangqiang.options.store.shared.SharedValueStore;
import com.zhangqiang.starlightreader.ui.theme.Theme1;
import com.zhangqiang.starlightreader.ui.theme.ThemeNight;

public class ReadSettingsModel {

    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";
    public static final String CHARSET_ISO_8859_1 = "ISO_8859_1";
    public static final String[] supportCharsets = new String[]{CHARSET_UTF_8, CHARSET_GBK, CHARSET_ISO_8859_1};
    private static final int MIN_FONT_SIZE_SP = 15;
    private static final int MAX_FONT_SIZE_SP = 35;
    private static volatile ReadSettingsModel instance;
    private final Option<Float> mTxtSizeOption;
    private final Option<String> mTxtCharsetOption;
    private final Option<Boolean> mTxtSimpleOption;
    private final Option<String> mPageThemeIdOption;
    private final Option<Boolean> mEyeProtectOption;
    private final Option<Boolean> mNightModeOption;
    private final Option<Float> mLightnessValueOption;
    private final Option<Boolean> mLightnessFollowSystemOption;

    private ReadSettingsModel(Context context) {
        ValueStore valueStore = new SharedValueStore(context, "reader_config");
        mTxtSizeOption = Options.ofFloat("reader_font_size", 20f, valueStore);
        mTxtCharsetOption = Options.ofString("reader_charset", supportCharsets[0], valueStore);
        mTxtSimpleOption = Options.ofBoolean("reader_txt_simple", true, valueStore);
        mPageThemeIdOption = Options.ofString("reader_page_theme_id", Theme1.ID, valueStore);
        mEyeProtectOption = Options.ofBoolean("reader_eye_protect", false, valueStore);
        mNightModeOption = Options.ofBoolean("reader_night_mode", false, valueStore);
        mLightnessValueOption = Options.ofFloat("reader_lightness_value", 0.5f, valueStore);
        mLightnessFollowSystemOption = Options.ofBoolean("reader_lightness_follow_system", true, valueStore);
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

    public Option<String> getTxtCharsetOption() {
        return mTxtCharsetOption;
    }

    public Option<Boolean> getTxtSimpleOption() {
        return mTxtSimpleOption;
    }

    public boolean isTxtSimple() {
        return mTxtSimpleOption.get();
    }

    public void setTxtSimple(boolean simple) {
        mTxtSimpleOption.set(simple);
    }

    public Option<String> getPageThemeOptionId() {
        return mPageThemeIdOption;
    }

    public void setPageThemeId(String id) {
        mPageThemeIdOption.set(id);
    }

    public String getPageThemeId() {
        return mPageThemeIdOption.get();
    }

    public Option<Boolean> getEyeProtectOption() {
        return mEyeProtectOption;
    }

    public Option<Boolean> getNightModeOption() {
        return mNightModeOption;
    }

    public Option<Float> getLightnessValueOption() {
        return mLightnessValueOption;
    }

    public Option<Boolean> getLightnessFollowSystemOption() {
        return mLightnessFollowSystemOption;
    }
}
