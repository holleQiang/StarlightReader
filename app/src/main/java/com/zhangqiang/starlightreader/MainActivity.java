package com.zhangqiang.starlightreader;

import android.view.View;

import com.zhangqiang.starlightreader.base.ui.BaseActivity;
import com.zhangqiang.starlightreader.ui.fragment.LocalTxtFileFragment;

public class MainActivity extends BaseActivity {


    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        super.initViews();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_fragment_container, new LocalTxtFileFragment())
                .commit();


    }
}
