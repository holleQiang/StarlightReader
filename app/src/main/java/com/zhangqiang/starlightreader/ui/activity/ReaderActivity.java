package com.zhangqiang.starlightreader.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;
import com.zhangqiang.instancerestore.annotations.Instance;
import com.zhangqiang.sl.android.ISLView;
import com.zhangqiang.sl.framework.layout.SLLinearLayout;
import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.reader.layout.CoverLayout;
import com.zhangqiang.sl.reader.layout.CoverAdapter;
import com.zhangqiang.sl.reader.page.PageView;
import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.impl.txt.Chapter;
import com.zhangqiang.sl.reader.parser.impl.txt.TxtBook;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReaderActivity extends BaseActivity {


    private ISLView mSLView;
    @Instance
    String bookPath;
    private CoverLayout mCoverLayout;
    private CoverAdapter mAdapter;
    private RecyclerView chapterRV;
    private TextView tvChapterListLabel;
    private CellRVAdapter mChapterAdapter;
    private Book mBook;
    private DrawerLayout mDrawerLayout;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_reader;
    }

    @Override
    public void initViews() {
        super.initViews();
        mSLView = findViewById(R.id.sl_view);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setKeepScreenOn(true);

        tvChapterListLabel = findViewById(R.id.tv_chapter_list_label);
        chapterRV = findViewById(R.id.chapter_recycler_view);
        chapterRV.setLayoutManager(new LinearLayoutManager(this));
        mChapterAdapter = new CellRVAdapter();
        chapterRV.setAdapter(mChapterAdapter);

        mCoverLayout = new CoverLayout(mSLView.getSLContext());
        mSLView.setContentView(mCoverLayout);
        initSettings();
        mCoverLayout.setOnPageChangeListener(new CoverLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(SLView view) {
                if (view instanceof SLLinearLayout) {
                    PageView pageView = (PageView) ((SLLinearLayout) view).getChildAt(1);
                    TextWordPosition startPosition = pageView.getStartPosition();
                    ReadRecordModel.updateReadPosition(bookPath, startPosition);
                }
            }

            @Override
            public void onPageCenterClick(SLView view) {
                ReadSettingsDialog dialog = ReadSettingsDialog.newInstance();
                dialog.show(getSupportFragmentManager(), "reader_settings");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBatterChangeReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterBatteryChangeReceiver();
    }

    private void registerBatterChangeReceiver() {

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mBatterChangeReceiver, intentFilter);
    }

    private void unRegisterBatteryChangeReceiver() {
        unregisterReceiver(mBatterChangeReceiver);
    }

    private final BroadcastReceiver mBatterChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAdapter != null) {
                mAdapter.setBottomBarBattery(getBatteryLevelFromIntent(intent));
            }
        }
    };

    private float getBatteryLevelFromIntent(Intent intent) {
        float current = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int count = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
        return current / count;
    }

    private float getBatteryLevel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            return (float) batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) / 100;
        } else {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent intent = registerReceiver(null, intentFilter);
            if (intent == null) {
                return 0;
            }
            return getBatteryLevelFromIntent(intent);
        }
    }

    private void loadBook(String charset) {
        mBook = null;
        BookModel.parseBook(bookPath, charset).compose(RxJavaUtils.applyIOMainSchedules())
                .compose(RxJavaUtils.bindLifecycle(this))
                .subscribe(new BaseObserver<Book>() {

                    @Override
                    public void onNext(Book book) {
                        mBook = book;

                        if (book instanceof TxtBook) {
                            List<Chapter> chapters = ((TxtBook) book).getChapters();
                            mChapterAdapter.setDataList(makeChapterCellList(chapters));
                            int chapterCount = chapters == null ? 0 : chapters.size();
                            tvChapterListLabel.setText(String.format(Locale.getDefault(), getResources().getString(R.string.chapter_list_number), chapterCount));
                        }

                        mCoverLayout.post(new Runnable() {
                            @Override
                            public void run() {

                                TextWordPosition readPosition = ReadRecordModel.getReadPosition(bookPath);
                                setupBook(book, readPosition);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void setupBook(Book book, TextWordPosition readPosition) {
        mAdapter = new CoverAdapter(book, new CoverAdapter.PositionFactory() {
            @Override
            public TextWordPosition getReadPosition() {
                return ReadRecordModel.getReadPosition(bookPath);
            }
        });
        mAdapter.setTextColor(ReadSettingsModel.getTxtColor());
        mAdapter.setTextSize(ViewUtils.spToPx(ReaderActivity.this, ReadSettingsModel.getTxtSize()));
        int hPadding = ViewUtils.dpToPx(this, 16);
        int vPadding = ViewUtils.dpToPx(this, 10);
        mAdapter.setContentPadding(hPadding, 0, hPadding, 0);
        mAdapter.setTopBarPadding(hPadding, vPadding, hPadding, vPadding);
        mAdapter.setTopBarTextColor(0xff666666);
        mAdapter.setParagraphSpace(ViewUtils.dpToPx(this, 5));
        mAdapter.setTopBarTextSize(ViewUtils.spToPx(this, 15));
        mAdapter.setLineHeightMultiple(1.2f);
        mAdapter.setBottomBarPadding(hPadding, vPadding, hPadding, vPadding);
        mAdapter.setBottomBarTextColor(0xff666666);
        mAdapter.setBottomBarTextSize(ViewUtils.spToPx(this, 15));
        mAdapter.setBottomBarDatePaddingLeft(ViewUtils.dpToPx(this, 5));
        mAdapter.setBottomBarBattery(getBatteryLevel());
        mAdapter.setBottomBarBatteryColor(0xff666666);
        mAdapter.setBottomBarBatteryBodyBorderWidth(ViewUtils.dpToPx(this,2));
        mAdapter.setBottomBarBatteryHeaderWidth(ViewUtils.dpToPx(this,2));
        mAdapter.setBottomBarBatteryHeaderHeight(ViewUtils.dpToPx(this,5));
        mAdapter.setBottomBarBatteryBodyWidth(ViewUtils.dpToPx(this,18));
        mAdapter.setBottomBarBatteryBodyHeight(ViewUtils.dpToPx(this,12));
        mCoverLayout.setAdapter(mAdapter);
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
                            mAdapter.setTextColor(integer);
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
                            mAdapter.setTextSize(ViewUtils.spToPx(ReaderActivity.this, aFloat));
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


    private List<Cell> makeChapterCellList(List<Chapter> chapters) {

        if (chapters == null) {
            return null;
        }
        List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < chapters.size(); i++) {
            Chapter chapter = chapters.get(i);
            cellList.add(new MultiCell<>(R.layout.item_chapter_info, chapter, new ViewHolderBinder<Chapter>() {
                @Override
                public void onBind(ViewHolder viewHolder, Chapter chapter) {
                    viewHolder.setText(R.id.tv_chapter_title, chapter.getName());
                    viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TextWordPosition position = new TextWordPosition();
                            position.set(chapter.getPosition());
                            ReadRecordModel.updateReadPosition(bookPath, position);
                            setupBook(mBook, position);
                            mDrawerLayout.closeDrawer(Gravity.START);
                        }
                    });
                }
            }));
        }
        return cellList;
    }
}
