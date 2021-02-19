package com.zhangqiang.starlightreader.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;
import com.zhangqiang.instancerestore.annotations.Instance;
import com.zhangqiang.sl.android.ISLView;
import com.zhangqiang.sl.android.image.AndroidDrawable;
import com.zhangqiang.sl.framework.layout.CoverLayout;
import com.zhangqiang.sl.framework.layout.SLLinearLayout;
import com.zhangqiang.sl.framework.view.SLView;
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
import com.zhangqiang.starlightreader.ui.widget.WindowBottomPlaceholderView;
import com.zhangqiang.starlightreader.utils.RxJavaUtils;
import com.zhangqiang.starlightreader.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReaderActivity extends BaseActivity {


    private static final int COVER_ANIMATION_DURATION = 200;
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
    @Instance
    boolean mInMenuMode = true;
    private View mMenuView;
    private View mTopMenuView;
    private View mBottomMenuView;
    private View mBtOptions;
    private View mBtCatalog;
    private View mBtBrightness;
    private View mBtBack;
    private WindowBottomPlaceholderView mNavigationBarPlaceholderView;
    private TextView mTvMenuTitle;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_reader;
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        Window window = getWindow();
        int systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mSLView = findViewById(R.id.sl_view);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setKeepScreenOn(true);
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                    systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                    systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
                }
            }
        });

        mMenuView = findViewById(R.id.view_cover);
        mTopMenuView = findViewById(R.id.view_top_cover);
        mBottomMenuView = findViewById(R.id.view_bottom_cover);
        mBtOptions = findViewById(R.id.bt_options);
        mBtCatalog = findViewById(R.id.bt_catalog);
        mBtBrightness = findViewById(R.id.bt_brightness);
        mBtBack = findViewById(R.id.bt_back);
        mTvMenuTitle = findViewById(R.id.tv_menu_title);
        mNavigationBarPlaceholderView = findViewById(R.id.view_navigation_bar_placeholder);
        initMenuView();


        tvChapterListLabel = findViewById(R.id.tv_chapter_list_label);
        chapterRV = findViewById(R.id.chapter_recycler_view);
        chapterRV.setLayoutManager(new LinearLayoutManager(this));
        mChapterAdapter = new CellRVAdapter();
        chapterRV.setAdapter(mChapterAdapter);
        tvChapterListLabel.setPadding(0, ViewUtils.getStatusBarHeight(this), 0, 0);

        mCoverLayout = new CoverLayout(mSLView.getSLContext());
        mSLView.setContentView(mCoverLayout);

        initCoverLayout();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (isInMenuMode()) {
                enterMenuMode();
            } else {
                exitMenuMode(false,false);
            }
        }
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

    private void initMenuView() {

        mMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitMenuMode(true,false);
            }
        });
        mTopMenuView.setPadding(mTopMenuView.getPaddingLeft(),
                mTopMenuView.getPaddingTop() + ViewUtils.getStatusBarHeight(this),
                mTopMenuView.getPaddingRight(),
                mTopMenuView.getPaddingBottom());

        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (view == mBtOptions) {
                    exitMenuMode(true,true);
                    ReadSettingsDialog readSettingsDialog = ReadSettingsDialog.newInstance();
                    readSettingsDialog.show(getSupportFragmentManager(),"dialog_read_settings");
                } else if (view == mBtCatalog) {
                    mDrawerLayout.openDrawer(Gravity.START);
                } else if (view == mBtBrightness) {

                } else if (view == mBtBack) {
                    finish();
                }
            }
        };
        mBtBrightness.setOnClickListener(onClickListener);
        mBtCatalog.setOnClickListener(onClickListener);
        mBtOptions.setOnClickListener(onClickListener);
        mBtBack.setOnClickListener(onClickListener);
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

        int hPadding = ViewUtils.dpToPx(this, 16);
        int vPadding = ViewUtils.dpToPx(this, 10);

        int topCutoutHeight = 0;
        int topBarRightPadding = hPadding;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            WindowInsets windowInsets = getWindow().getDecorView().getRootWindowInsets();
            DisplayCutout displayCutout = windowInsets.getDisplayCutout();
            if (displayCutout != null) {
                List<Rect> boundingRectList = displayCutout.getBoundingRects();
                if (boundingRectList.size() > 0) {
                    Rect rect = boundingRectList.get(0);
                    if (rect.top == 0 && rect.right == getResources().getDisplayMetrics().widthPixels) {
                        topBarRightPadding += rect.width();
                        //右上侧挖孔屏
                    }
                    if (rect.top == 0) {
                        topCutoutHeight = Math.max(topCutoutHeight, rect.height());
                    }
                }
            }
        }

        mAdapter = new CoverAdapter(book, new CoverAdapter.PositionFactory() {
            @Override
            public TextWordPosition getReadPosition() {
                return ReadRecordModel.getReadPosition(bookPath);
            }
        });
        mAdapter.setTextColor(ReadSettingsModel.getTxtColor());
        mAdapter.setTextSize(ViewUtils.spToPx(ReaderActivity.this, ReadSettingsModel.getTxtSize()));

        mAdapter.setContentPadding(hPadding, 0, hPadding, 0);
        mAdapter.setTopBarPadding(hPadding, vPadding, topBarRightPadding, vPadding);
        mAdapter.setTopBarTextColor(0xff666666);
        mAdapter.setTopBarTextSize(ViewUtils.spToPx(this, 15));
        mAdapter.setParagraphSpace(ViewUtils.dpToPx(this, 5));
        mAdapter.setLineHeightMultiple(1.2f);
        mAdapter.setBottomBarPadding(hPadding, vPadding, hPadding, vPadding);
        mAdapter.setBottomBarTextColor(0xff666666);
        mAdapter.setBottomBarTextSize(ViewUtils.spToPx(this, 15));
        mAdapter.setBottomBarDatePaddingLeft(ViewUtils.dpToPx(this, 5));
        mAdapter.setBottomBarBattery(getBatteryLevel());
        mAdapter.setBottomBarBatteryColor(0xff666666);
        mAdapter.setBottomBarBatteryBodyBorderWidth(ViewUtils.dpToPx(this, 2));
        mAdapter.setBottomBarBatteryHeaderWidth(ViewUtils.dpToPx(this, 2));
        mAdapter.setBottomBarBatteryHeaderHeight(ViewUtils.dpToPx(this, 5));
        mAdapter.setBottomBarBatteryBodyWidth(ViewUtils.dpToPx(this, 18));
        mAdapter.setBottomBarBatteryBodyHeight(ViewUtils.dpToPx(this, 12));
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
                            position.set(chapter.getStartPosition());
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


    private void initCoverLayout() {

        int shadowWidth = ViewUtils.dpToPx(this, 25);
        AndroidDrawable leftShadowDrawable = new AndroidDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0x00000000, 0x46000000}));
        leftShadowDrawable.setBounds(0, 0, shadowWidth, 0);
        mCoverLayout.setLeftShadowDrawable(leftShadowDrawable);

        AndroidDrawable rightShadowDrawable = new AndroidDrawable(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{0x00000000, 0x46000000}));
        rightShadowDrawable.setBounds(0, 0, shadowWidth, 0);
        mCoverLayout.setRightShadowDrawable(rightShadowDrawable);

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

                enterMenuMode();
            }
        });
    }

    private void enterMenuMode() {
        mInMenuMode = true;
        ViewUtils.safePost(mMenuView, new Runnable() {
            @Override
            public void run() {
                showMenuAndSystemView();
                mTopMenuView.animate().translationY(0).setListener(null).setDuration(COVER_ANIMATION_DURATION).start();
                mBottomMenuView.animate().translationY(0).setDuration(COVER_ANIMATION_DURATION).start();
            }
        });
    }

    private void exitMenuMode(boolean animated,boolean keepNavigationBar) {
        mInMenuMode = false;
        ViewUtils.safePost(mMenuView, new Runnable() {
            @Override
            public void run() {
                mTvMenuTitle.setText(mBottomMenuView.getHeight()
                        + "_____" + mNavigationBarPlaceholderView.getHeight());
                if (!animated) {
                    mTopMenuView.setTranslationY(-mTopMenuView.getHeight());
                    mBottomMenuView.setTranslationY(mBottomMenuView.getHeight());
                    hideMenuAndSystemView(keepNavigationBar);
                } else {
                    mTopMenuView.animate()
                            .translationY(-mTopMenuView.getHeight())
                            .setDuration(COVER_ANIMATION_DURATION)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    hideMenuAndSystemView(keepNavigationBar);
                                }
                            })
                            .start();
                    mBottomMenuView.animate()
                            .translationY(mBottomMenuView.getHeight())
                            .setDuration(COVER_ANIMATION_DURATION)
                            .start();
                }
            }
        });
    }

    private void hideMenuAndSystemView(boolean keepNavigationBar) {

        mMenuView.setVisibility(View.GONE);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            if (!keepNavigationBar) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(systemUiVisibility);
        }
    }

    private void showMenuAndSystemView() {

        mMenuView.setVisibility(View.VISIBLE);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            systemUiVisibility &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
            systemUiVisibility &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(systemUiVisibility);
        }
    }


    private boolean isInMenuMode() {
        return mInMenuMode;
    }
}
