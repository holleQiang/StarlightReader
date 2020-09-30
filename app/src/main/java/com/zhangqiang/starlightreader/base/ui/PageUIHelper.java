package com.zhangqiang.starlightreader.base.ui;

import android.util.SparseArray;
import android.view.View;

public abstract class PageUIHelper {

    private PageUI mPageUI;
    private SparseArray<View> mViewIdsCache = new SparseArray<>();

    public PageUIHelper(PageUI pageUI) {
        this.mPageUI = pageUI;
    }


    public PageUI getPageUI() {
        return mPageUI;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int id) {
        View view = mViewIdsCache.get(id);
        if (view != null) {
            return (T) view;
        } else {
            view = onFindViewById(id);
            mViewIdsCache.put(id, view);
        }
        return (T) view;
    }

    protected abstract View onFindViewById(int id);


    protected void clearViewIdsCache(){
        mViewIdsCache.clear();
    }
}
