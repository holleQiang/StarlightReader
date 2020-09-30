package com.zhangqiang.starlightreader.base.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentUIHelper extends PageUIHelper {

    private Fragment mFragment;
    private View mItemView;

    public <T extends Fragment & PageUI> FragmentUIHelper(T pageUI) {
        super(pageUI);
        mFragment = pageUI;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        PageUI pageUI = getPageUI();
        mItemView = inflater.inflate(pageUI.getLayoutResId(), container, false);
        pageUI.initViews();
        return mItemView;
    }

    public void onDestroyView() {
        clearViewIdsCache();
    }

    @Override
    protected View onFindViewById(int id) {
        return mItemView.findViewById(id);
    }
}
