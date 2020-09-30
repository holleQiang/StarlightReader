package com.zhangqiang.starlightreader;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.zhangqiang.starlightreader.base.ui.BaseActivity;
import com.zhangqiang.starlightreader.base.ResultCallback;
import com.zhangqiang.starlightreader.utils.PermissionUtils;

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initViews() {
        super.initViews();
        PermissionUtils.requestSDPermission(this, new ResultCallback<String[]>() {
            @Override
            public void onSuccess(@NonNull String[] strings) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                finish();
            }
        });
    }
}
