package com.zhangqiang.starlightreader.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.starlightreader.R;
import com.zhangqiang.starlightreader.base.ui.BaseActivity;

public class FontSettingsActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private CellRVAdapter mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_font_settings;
    }

    @Override
    public void initViews() {
        super.initViews();
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.font_settings);
        mRecyclerView = (RecyclerView) findViewById(R.id.m_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CellRVAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}
