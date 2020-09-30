package com.zhangqiang.starlightreader.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.zhangqiang.instancerestore.annotations.Instance;
import com.zhangqiang.sl.android.ISLView;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.reader.layout.CoverLayout;
import com.zhangqiang.sl.reader.layout.CoverAdapter;
import com.zhangqiang.sl.reader.page.PageView;
import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.position.TextWordPosition;
import com.zhangqiang.starlightreader.R;
import com.zhangqiang.starlightreader.base.ui.BaseActivity;
import com.zhangqiang.starlightreader.extend.BaseObserver;
import com.zhangqiang.starlightreader.model.BookModel;
import com.zhangqiang.starlightreader.model.ReadRecordModel;
import com.zhangqiang.starlightreader.model.ReadSettingsModel;
import com.zhangqiang.starlightreader.ui.dialog.ReadSettingsDialog;
import com.zhangqiang.starlightreader.utils.RxJavaUtils;
import com.zhangqiang.starlightreader.utils.ViewUtils;

public class ReaderActivity extends BaseActivity {


    private ISLView mSLView;
    @Instance
    String bookPath;
    private CoverLayout mCoverLayout;
    private CoverAdapter mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_reader;
    }

    @Override
    public void initViews() {
        super.initViews();
        mSLView = findViewById(R.id.sl_view);
        mCoverLayout = new CoverLayout(mSLView.getSLContext());
        mSLView.setContentView(mCoverLayout);
        ((View) mSLView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadSettingsDialog dialog = ReadSettingsDialog.newInstance();
                dialog.show(getSupportFragmentManager(), "reader_settings");
            }
        });
        initSettings();
        mCoverLayout.setOnPageChangeListener(new CoverLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(SLView view) {
                if (view instanceof PageView) {
                    TextWordPosition startPosition = ((PageView) view).getStartPosition();
                    ReadRecordModel.updateReadPosition("111",bookPath,startPosition);
                }
            }
        });
    }

    private void loadBook(String charset) {
        BookModel.parseBook(bookPath, charset).compose(RxJavaUtils.applyIOMainSchedules())
                .compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Book>() {
                    @Override
                    public void onNext(Book book) {
                        mCoverLayout.post(new Runnable() {
                            @Override
                            public void run() {

                               TextWordPosition readPosition = ReadRecordModel.getReadPosition(bookPath);

                                mAdapter = new CoverAdapter(book, readPosition);
                                mCoverLayout.setAdapter(mAdapter);
                                mAdapter.getPageAdapter().setTextColor(ReadSettingsModel.getTxtColor());
                                mAdapter.getPageAdapter().setTextSize(ViewUtils.spToPx(ReaderActivity.this,ReadSettingsModel.getTxtSize()));
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initSettings() {

        ReadSettingsModel.getTxtCharsetOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        loadBook(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        ReadSettingsModel.getTxtColorOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        if (mAdapter != null) {
                            mAdapter.getPageAdapter().setTextColor(integer);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        ReadSettingsModel.getTxtSizeOption().toObservable().compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Float>() {
                    @Override
                    public void onNext(Float aFloat) {
                        if (mAdapter != null) {
                            mAdapter.getPageAdapter().setTextSize(ViewUtils.spToPx(ReaderActivity.this,aFloat));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
