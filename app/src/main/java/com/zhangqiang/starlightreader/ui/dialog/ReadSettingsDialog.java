package com.zhangqiang.starlightreader.ui.dialog;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhangqiang.starlightreader.R;
import com.zhangqiang.starlightreader.base.ui.BaseDialogFragment;
import com.zhangqiang.starlightreader.extend.BaseObserver;
import com.zhangqiang.starlightreader.model.ReadSettingsModel;
import com.zhangqiang.starlightreader.utils.RxJavaUtils;

public class ReadSettingsDialog extends BaseDialogFragment {


    private SeekBar mFontSizeSeekBar;
    private TextView tvCharset;

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
        mFontSizeSeekBar = getUIHelper().findViewById(R.id.sb_font_size);
        float fontSizeProgressFactor = ReadSettingsModel.getTxtSizeRangeFactor();
        mFontSizeSeekBar.setProgress((int) (mFontSizeSeekBar.getMax() * fontSizeProgressFactor));
        mFontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float factor = (float) seekBar.getProgress() / seekBar.getMax();
                ReadSettingsModel.setTxtSize(factor);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseCharsetDialog.newInstance().show(getChildFragmentManager(),"choose_charset");
            }
        };
        getUIHelper().findViewById(R.id.tv_txt_charset_title).setOnClickListener(onClickListener);
        tvCharset = getUIHelper().findViewById(R.id.tv_txt_charset);
        tvCharset.setOnClickListener(onClickListener);
        ReadSettingsModel.getTxtCharsetOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        tvCharset.setText(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    @Override
    protected boolean useBottomSheetFeature() {
        return true;
    }
}
