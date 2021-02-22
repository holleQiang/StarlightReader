package com.zhangqiang.starlightreader.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;

import com.zhangqiang.starlightreader.utils.SystemUtils;

public class BrightnessHelper {

    private static final Uri URI_BRIGHTNESS = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Context mContext;
    private OnBrightnessChangedListener onBrightnessChangedListener;

    public BrightnessHelper(Context context) {
        this.mContext = context;
    }

    public void registerBrightnessReceiver() {
        ContentResolver contentResolver = mContext.getContentResolver();
        contentResolver.registerContentObserver(URI_BRIGHTNESS, false, contentObserver);
    }

    public void unRegisterBrightnessReceiver() {
        ContentResolver contentResolver = mContext.getContentResolver();
        contentResolver.unregisterContentObserver(contentObserver);
    }

    private final  ContentObserver contentObserver = new ContentObserver(null) {

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (selfChange) {
                return;
            }
            if (URI_BRIGHTNESS.equals(uri)) {
                if (onBrightnessChangedListener != null) {
                    onBrightnessChangedListener.onBrightnessChanged();
                }
            }
        }
    };

    public interface OnBrightnessChangedListener{
        void onBrightnessChanged();
    }

    public float getBrightness(){
        return SystemUtils.getScreenBrightnessPercent(mContext);
    }

    public void setOnBrightnessChangedListener(OnBrightnessChangedListener onBrightnessChangedListener) {
        this.onBrightnessChangedListener = onBrightnessChangedListener;
    }
}
