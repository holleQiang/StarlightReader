package com.zhangqiang.starlightreader.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.starlightreader.R;
import com.zhangqiang.starlightreader.base.ui.BaseFragment;
import com.zhangqiang.starlightreader.bean.BookShelfBean;
import com.zhangqiang.starlightreader.extend.BaseObserver;
import com.zhangqiang.starlightreader.model.BookShelfModel;
import com.zhangqiang.starlightreader.utils.RxJavaUtils;

import java.util.List;

public class BookShelfFragment extends BaseFragment {

    private CellRVAdapter mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_shelf;
    }

    @Override
    public void initViews() {
        super.initViews();
        RecyclerView mRecyclerView = getUIHelper().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CellRVAdapter();
        mRecyclerView.setAdapter(mAdapter);
        BookShelfModel.getAllBookShelfs().compose(RxJavaUtils.applyIOMainSchedules())
                .compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<List<BookShelfBean>>() {
                    @Override
                    public void onNext(List<BookShelfBean> bookShelfBeans) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
