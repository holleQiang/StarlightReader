package com.zhangqiang.starlightreader.ui.dialog;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.zhangqiang.starlightreader.R;
import com.zhangqiang.starlightreader.base.ui.BaseDialogFragment;
import com.zhangqiang.starlightreader.extend.BaseObserver;
import com.zhangqiang.starlightreader.helper.BrightnessHelper;
import com.zhangqiang.starlightreader.model.ReadSettingsModel;
import com.zhangqiang.starlightreader.ui.theme.Theme1;
import com.zhangqiang.starlightreader.ui.theme.Theme2;
import com.zhangqiang.starlightreader.ui.theme.Theme3;
import com.zhangqiang.starlightreader.ui.theme.Theme4;
import com.zhangqiang.starlightreader.utils.RxJavaUtils;

import io.reactivex.annotations.NonNull;

public class ReadSettingsDialog extends BaseDialogFragment {

    private RadioButton rbCharsetGBK;
    private RadioButton rbCharsetUTF8;
    private RadioButton rbCharsetISO88591;
    private CheckBox cbSimpleTxt;
    private RadioGroup rgBG;
    private RadioButton rbPageBG1;
    private RadioButton rbPageBG2;
    private RadioButton rbPageBG3;
    private RadioButton rbPageBG4;
    private CheckBox cbEyeProtect;
    private CheckBox cbNightMode;
    private CheckBox cbLightnessFollowSystem;
    private SeekBar sbLightness;
    private BrightnessHelper mBrightnessHelper;

    public static ReadSettingsDialog newInstance() {
        return new ReadSettingsDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBrightnessHelper = new BrightnessHelper(getContext());
        mBrightnessHelper.setOnBrightnessChangedListener(new BrightnessHelper.OnBrightnessChangedListener() {
            @Override
            public void onBrightnessChanged() {
                updateLightnessProgress(mBrightnessHelper.getBrightness());
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_reader_settings;
    }

    @Override
    public int getTheme() {
        return R.style.ReaderSettingDialog;
    }

    @Override
    public void initViews() {
        super.initViews();
        getUIHelper().findViewById(R.id.bt_font_size_small).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float txtSizeFactor = ReadSettingsModel.getInstance(v.getContext()).getTxtSizeFactor();
                ReadSettingsModel.getInstance(v.getContext()).setTxtSizeFactor(txtSizeFactor - 0.1f);
            }
        });
        getUIHelper().findViewById(R.id.bt_font_size_big).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float txtSizeFactor = ReadSettingsModel.getInstance(v.getContext()).getTxtSizeFactor();
                ReadSettingsModel.getInstance(v.getContext()).setTxtSizeFactor(txtSizeFactor + 0.1f);
            }
        });
        rbCharsetGBK = getUIHelper().findViewById(R.id.bt_charset_gbk);
        rbCharsetUTF8 = getUIHelper().findViewById(R.id.bt_charset_utf_8);
        rbCharsetISO88591 = getUIHelper().findViewById(R.id.bt_charset_ISO_8859_1);
        ReadSettingsModel.getInstance(getContext()).getTxtCharsetOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<String>() {

                    @Override
                    protected boolean handNext(@NonNull String s) {
                        updateCharsetView(s);
                        return false;
                    }
                });

        cbSimpleTxt = (CheckBox) getUIHelper().findViewById(R.id.bt_font_complex);
        ReadSettingsModel.getInstance(getContext()).getTxtSimpleOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Boolean>() {

                    @Override
                    protected boolean handNext(@NonNull Boolean aBoolean) {
                        updateSimpleTxtView(aBoolean);
                        return false;
                    }
                });

        rgBG = getUIHelper().findViewById(R.id.rg_bg);
        rbPageBG1 = getUIHelper().findViewById(R.id.rb_bg_1);
        rbPageBG2 = getUIHelper().findViewById(R.id.rb_bg_2);
        rbPageBG3 = getUIHelper().findViewById(R.id.rb_bg_3);
        rbPageBG4 = getUIHelper().findViewById(R.id.rb_bg_4);
        ReadSettingsModel.getInstance(getContext()).getPageThemeOptionId().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<String>() {

                    @Override
                    protected boolean handNext(@NonNull String s) {
                        rgBG.setOnCheckedChangeListener(null);
                        if (Theme1.ID.equals(s)) {
                            rbPageBG1.setChecked(true);
                        } else if (Theme2.ID.equals(s)) {
                            rbPageBG2.setChecked(true);
                        } else if (Theme3.ID.equals(s)) {
                            rbPageBG3.setChecked(true);
                        } else if (Theme4.ID.equals(s)) {
                            rbPageBG4.setChecked(true);
                        } else {
                            rbPageBG1.setChecked(true);
                        }
                        rgBG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                String pageThemeId;
                                if (checkedId == rbPageBG1.getId()) {
                                    pageThemeId = Theme1.ID;
                                } else if (checkedId == rbPageBG2.getId()) {
                                    pageThemeId = Theme2.ID;
                                } else if (checkedId == rbPageBG3.getId()) {
                                    pageThemeId = Theme3.ID;
                                } else if (checkedId == rbPageBG4.getId()) {
                                    pageThemeId = Theme4.ID;
                                } else {
                                    throw new IllegalArgumentException("unknown view id:" + checkedId);
                                }
                                ReadSettingsModel.getInstance(getContext()).setPageThemeId(pageThemeId);
                                ReadSettingsModel.getInstance(getContext()).getNightModeOption().set(false);
                            }
                        });
                        return false;
                    }
                });
        cbEyeProtect = getUIHelper().findViewById(R.id.cb_eye_protect);
        ReadSettingsModel.getInstance(getContext()).getEyeProtectOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Boolean>() {

                    @Override
                    protected boolean handNext(@NonNull Boolean aBoolean) {
                        cbEyeProtect.setOnCheckedChangeListener(null);
                        cbEyeProtect.setChecked(aBoolean);
                        cbEyeProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                ReadSettingsModel.getInstance(getContext()).getEyeProtectOption().set(isChecked);
                            }
                        });
                        return false;
                    }
                });
        cbNightMode = getUIHelper().findViewById(R.id.cb_night_mode);
        ReadSettingsModel.getInstance(getContext()).getNightModeOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Boolean>() {

                    @Override
                    protected boolean handNext(@NonNull Boolean aBoolean) {
                        cbNightMode.setOnCheckedChangeListener(null);
                        cbNightMode.setChecked(aBoolean);
                        cbNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                ReadSettingsModel.getInstance(getContext()).getNightModeOption().set(isChecked);
                            }
                        });
                        return false;
                    }
                });
        cbLightnessFollowSystem = getUIHelper().findViewById(R.id.cb_lightness_system);
        sbLightness = getUIHelper().findViewById(R.id.sb_lightness);
        sbLightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float factor = (float) progress / sbLightness.getMax();
                    ReadSettingsModel.getInstance(getContext()).getLightnessValueOption().set(factor);
                    ReadSettingsModel.getInstance(getContext()).getLightnessFollowSystemOption().set(false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ReadSettingsModel.getInstance(getContext()).getLightnessFollowSystemOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Boolean>() {

                    @Override
                    protected boolean handNext(@NonNull Boolean aBoolean) {
                        cbLightnessFollowSystem.setOnCheckedChangeListener(null);
                        cbLightnessFollowSystem.setChecked(aBoolean);
                        cbLightnessFollowSystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                ReadSettingsModel.getInstance(getContext()).getLightnessFollowSystemOption().set(isChecked);
                                if (isChecked) {
                                    updateLightnessProgress(mBrightnessHelper.getBrightness());
                                }
                            }
                        });
                        sbLightness.setProgressDrawable(ContextCompat.getDrawable(getContext(), aBoolean ? R.drawable.lightness_progress_follow_system : R.drawable.lightness_progress));
                        return false;
                    }
                });
        ReadSettingsModel.getInstance(getContext()).getLightnessValueOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Float>() {

                    @Override
                    protected boolean handNext(@NonNull Float aFloat) {
                        updateLightnessProgress(aFloat);
                        return false;
                    }
                });
    }

    private void updateLightnessProgress(Float aFloat) {
        sbLightness.setProgress((int) (aFloat * sbLightness.getMax()));
    }

    private void updateSimpleTxtView(boolean simple) {
        cbSimpleTxt.setOnCheckedChangeListener(null);
        cbSimpleTxt.setChecked(!simple);
        cbSimpleTxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ReadSettingsModel.getInstance(getContext()).setTxtSimple(!isChecked);
            }
        });
    }

    private void updateCharsetView(String txtCharset) {
        ((RadioGroup) getUIHelper().findViewById(R.id.rg_charset)).setOnCheckedChangeListener(null);
        if (ReadSettingsModel.CHARSET_GBK.equals(txtCharset)) {
            rbCharsetGBK.setChecked(true);
        } else if (ReadSettingsModel.CHARSET_ISO_8859_1.equals(txtCharset)) {
            rbCharsetISO88591.setChecked(true);
        } else if (ReadSettingsModel.CHARSET_UTF_8.equals(txtCharset)) {
            rbCharsetUTF8.setChecked(true);
        }
        ((RadioGroup) getUIHelper().findViewById(R.id.rg_charset)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbCharsetGBK.getId()) {
                    ReadSettingsModel.getInstance(group.getContext()).setTxtCharset(ReadSettingsModel.CHARSET_GBK);
                } else if (checkedId == rbCharsetUTF8.getId()) {
                    ReadSettingsModel.getInstance(group.getContext()).setTxtCharset(ReadSettingsModel.CHARSET_UTF_8);
                } else if (checkedId == rbCharsetISO88591.getId()) {
                    ReadSettingsModel.getInstance(group.getContext()).setTxtCharset(ReadSettingsModel.CHARSET_ISO_8859_1);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mBrightnessHelper.registerBrightnessReceiver();

        Window window = getDialog().getWindow();
        View decorView = window.getDecorView();
        int systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        decorView.setSystemUiVisibility(systemUiVisibility);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mBrightnessHelper.unRegisterBrightnessReceiver();
    }

    @Override
    protected boolean useBottomSheetFeature() {
        return true;
    }

    @Override
    protected boolean closeFitSystemWindow() {
        return true;
    }
}
