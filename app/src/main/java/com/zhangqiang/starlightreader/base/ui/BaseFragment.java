package com.zhangqiang.starlightreader.base.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangqiang.visiblehelper.FragmentVisibleHelper;
import com.zhangqiang.visiblehelper.VisibleHelper;
import com.zhangqiang.visiblehelper.VisibleHelperOwner;

public class BaseFragment extends Fragment implements VisibleHelperOwner,PageUI,UIHelperOwner{

    private FragmentVisibleHelper mVisibleHelper = new FragmentVisibleHelper(this);
    private FragmentUIHelper uiHelper = new FragmentUIHelper(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return uiHelper.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uiHelper.onDestroyView();
    }

    @NonNull
    @Override
    public VisibleHelper getVisibleHelper() {
        return mVisibleHelper;
    }

    @Override
    public void onStart() {
        super.onStart();
        mVisibleHelper.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mVisibleHelper.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mVisibleHelper.setUserVisibleHint();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mVisibleHelper.onHiddenChanged();
    }

    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public void initViews() {

    }

    @Override
    public PageUIHelper getUIHelper() {
        return uiHelper;
    }
}
