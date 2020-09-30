package com.zhangqiang.starlightreader.app;

import android.app.Application;

import com.zhangqiang.options.store.ValueStore;
import com.zhangqiang.options.store.shared.SharedValueStore;
import com.zhangqiang.starlightreader.db.AppDBHelper;
import com.zhangqiang.starlightreader.model.ReadSettingsModel;

public class App extends Application {

    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ReadSettingsModel.init(this);
        AppDBHelper.init(this);
    }
}
