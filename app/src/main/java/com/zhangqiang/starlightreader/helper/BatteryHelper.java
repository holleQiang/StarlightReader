package com.zhangqiang.starlightreader.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

public class BatteryHelper {

    private Context mContext;
    private OnBatteryLevelChangedListener onBatteryLevelChangedListener;

    public BatteryHelper(Context context) {
        this.mContext = context;
    }

    public void registerBatterChangeReceiver() {

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        mContext.registerReceiver(mBatterChangeReceiver, intentFilter);
    }

    public void unRegisterBatteryChangeReceiver() {
        mContext.unregisterReceiver(mBatterChangeReceiver);
    }

    private final BroadcastReceiver mBatterChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (onBatteryLevelChangedListener != null) {
                onBatteryLevelChangedListener.onBatteryLevelChanged(getBatteryLevelFromIntent(intent));
            }
        }
    };

    private float getBatteryLevelFromIntent(Intent intent) {
        float current = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int count = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
        return current / count;
    }

    public float getBatteryLevel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) mContext.getSystemService(Context.BATTERY_SERVICE);
            return (float) batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) / 100;
        } else {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent intent = mContext.registerReceiver(null, intentFilter);
            if (intent == null) {
                return 0;
            }
            return getBatteryLevelFromIntent(intent);
        }
    }

    public interface OnBatteryLevelChangedListener{
        void onBatteryLevelChanged(float level);
    }

    public void setOnBatteryLevelChangedListener(OnBatteryLevelChangedListener onBatteryLevelChangedListener) {
        this.onBatteryLevelChangedListener = onBatteryLevelChangedListener;
    }
}
