package com.zhangqiang.starlightreader.ui.dialog;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhangqiang.starlightreader.R;
import com.zhangqiang.starlightreader.base.ui.BaseDialogFragment;
import com.zhangqiang.starlightreader.extend.BaseObserver;
import com.zhangqiang.starlightreader.model.ReadSettingsModel;
import com.zhangqiang.starlightreader.utils.RxJavaUtils;

public class ReadSettingsDialog extends BaseDialogFragment {

    private RadioButton rbCharsetGBK;
    private RadioButton rbCharsetUTF8;
    private RadioButton rbCharsetISO88591;
    private CheckBox cbSimpleTxt;

    public static ReadSettingsDialog newInstance() {
        return new ReadSettingsDialog();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_reader_settings;
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
                    public void onNext(@NonNull String s) {
                        updateCharsetView(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

        cbSimpleTxt = (CheckBox) getUIHelper().findViewById(R.id.bt_font_complex);
        ReadSettingsModel.getInstance(getContext()).getTxtSimpleOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        updateSimpleTxtView(aBoolean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
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
        Window window = getDialog().getWindow();

        View decorView = window.getDecorView();
        int systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
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
    protected boolean useBottomSheetFeature() {
        return true;
    }

    @Override
    protected boolean closeFitSystemWindow() {
        return true;
    }
}
