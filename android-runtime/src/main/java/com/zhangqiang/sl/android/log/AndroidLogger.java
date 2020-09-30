package com.zhangqiang.sl.android.log;

import android.util.Log;

import com.zhangqiang.sl.framework.log.SLLogger;

public class AndroidLogger extends SLLogger {

    @Override
    protected void onLogI(String tag, String log) {
        Log.i(tag, log);
    }


    @Override
    protected void onLogW(String tag, String log) {
        Log.w(tag, log);
    }


    @Override
    protected void onLogE(String tag, String log) {
        Log.e(tag, log);
    }
}