package com.zhangqiang.starlightreader.ui.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;
import com.zhangqiang.starlightreader.R;
import com.zhangqiang.starlightreader.base.ui.BaseDialogFragment;
import com.zhangqiang.starlightreader.extend.BaseObserver;
import com.zhangqiang.starlightreader.model.ReadSettingsModel;
import com.zhangqiang.starlightreader.ui.activity.ReaderActivity;
import com.zhangqiang.starlightreader.utils.RxJavaUtils;

import io.reactivex.annotations.NonNull;

public class ChooseCharsetDialog extends BaseDialogFragment {

    private CellRVAdapter mAdapter;

    public static ChooseCharsetDialog newInstance() {
        return new ChooseCharsetDialog();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_choose_charset;
    }

    @Override
    public void initViews() {
        super.initViews();
        RecyclerView mRecyclerView = getUIHelper().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CellRVAdapter();
        for (String charset : ReadSettingsModel.supportCharsets) {
            mAdapter.addDataAtLast(makeCell(charset));
        }
        mRecyclerView.setAdapter(mAdapter);

        ReadSettingsModel.getInstance(getContext()).getTxtCharsetOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<String>() {

                    @Override
                    protected boolean handNext(@NonNull String s) {
                        mAdapter.notifyDataSetChanged();
                        return false;
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private Cell makeCell(String charset) {
        return new MultiCell<>(R.layout.item_charset, charset, new ViewHolderBinder<String>() {
            @Override
            public void onBind(ViewHolder viewHolder, String s) {
                viewHolder.setText(R.id.tv_title, s);
                viewHolder.setVisibility(R.id.iv_select, ReadSettingsModel.getInstance(getContext()).getTxtCharset().equals(s) ? View.VISIBLE : View.INVISIBLE);
                viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReadSettingsModel.getInstance(getContext()).setTxtCharset(s);
                    }
                });
            }
        });
    }
}
