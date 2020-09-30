package com.zhangqiang.starlightreader.base.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangqiang.instancerestore.InstanceRestore;
import com.zhangqiang.visiblehelper.ActivityVisibleHelper;
import com.zhangqiang.visiblehelper.VisibleHelperOwner;

public abstract class BaseActivity extends AppCompatActivity implements VisibleHelperOwner, PageUI, UIHelperOwner {

    private ActivityVisibleHelper mVisibleHelper = new ActivityVisibleHelper();
    private ActivityUIHelper mUIHelper = new ActivityUIHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            InstanceRestore.restore(this, savedInstanceState);
        } else {
            InstanceRestore.restore(this, getIntent().getExtras());
        }
        mUIHelper.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        InstanceRestore.save(this, outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVisibleHelper.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVisibleHelper.onStop();
    }

    @NonNull
    @Override
    public ActivityVisibleHelper getVisibleHelper() {
        return mVisibleHelper;
    }

    @Override
    public PageUIHelper getUIHelper() {
        return mUIHelper;
    }
}
