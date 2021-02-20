package com.zhangqiang.starlightreader.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;
import com.zhangqiang.starlightreader.R;
import com.zhangqiang.starlightreader.base.ui.BaseFragment;
import com.zhangqiang.starlightreader.bean.BookFileBean;
import com.zhangqiang.starlightreader.extend.BaseObserver;
import com.zhangqiang.starlightreader.model.BookFileModel;
import com.zhangqiang.starlightreader.ui.activity.ReadRecordActivity;
import com.zhangqiang.starlightreader.ui.activity.ReaderActivity;
import com.zhangqiang.starlightreader.utils.RxJavaUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LocalTxtFileFragment extends BaseFragment {

    private CellRVAdapter mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_local_txt_files;
    }

    @Override
    public void initViews() {
        super.initViews();
        RecyclerView mRecyclerView = getUIHelper().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CellRVAdapter();
        mRecyclerView.setAdapter(mAdapter);

        BookFileModel.getAllLocalBookFiles(getContext())
                .compose(RxJavaUtils.applyIOMainSchedules())
                .compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<List<BookFileBean>>() {
                    @Override
                    public void onNext(List<BookFileBean> txtFileBeans) {
                        ArrayList<Cell> dataList = new ArrayList<>();
                        for (int i = 0; i < txtFileBeans.size(); i++) {
                            BookFileBean txtFileBean = txtFileBeans.get(i);
                            dataList.add(new MultiCell<>(R.layout.item_local_txt_file, txtFileBean, new ViewHolderBinder<BookFileBean>() {
                                @Override
                                public void onBind(ViewHolder viewHolder, BookFileBean txtFileBean) {
                                    viewHolder.setText(R.id.tv_file_name, txtFileBean.getFileName());
                                    viewHolder.setText(R.id.tv_file_length, formatFileSize(txtFileBean.getFileLength()));
                                    viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(v.getContext(), ReaderActivity.class);
                                            intent.putExtra("bookPath", txtFileBean.getFilePath());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }));
                        }
                        mAdapter.setDataList(dataList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private static String formatFileSize(int fileLength) {
        if (fileLength < 1024) {
            return fileLength + "B";
        } else if (fileLength < 1024 * 1024) {

            return new DecimalFormat("#.#").format((float) fileLength / 1024) + "K";
        } else {
            return new DecimalFormat("#.#").format((float) fileLength / 1024 / 1024) + "M";
        }
    }
}
