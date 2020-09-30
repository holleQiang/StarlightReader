package com.zhangqiang.starlightreader.base.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ActivityUIHelper extends PageUIHelper {

    private Activity mActivity;

    public <T extends Activity & PageUI> ActivityUIHelper(T pageUI) {
        super(pageUI);
        mActivity = pageUI;
    }

    public void onCreate(Bundle savedInstanceState){
        int layoutResId = getPageUI().getLayoutResId();
        if (layoutResId != 0) {
            mActivity.setContentView(layoutResId);
            getPageUI().initViews();
        }
    }

    @Override
    protected View onFindViewById(int id) {
        return mActivity.findViewById(id);
    }
}
