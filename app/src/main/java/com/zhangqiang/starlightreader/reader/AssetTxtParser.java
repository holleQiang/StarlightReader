package com.zhangqiang.starlightreader.reader;

import android.content.Context;

import com.zhangqiang.slreader.parser.impl.txt.BaseTxtBookParser;

import java.io.IOException;
import java.io.InputStream;

public class AssetTxtParser extends BaseTxtBookParser {

    private Context context;
    private String assetName;

    public AssetTxtParser(String charset, Context context, String assetName) {
        super(charset);
        this.context = context.getApplicationContext();
        this.assetName = assetName;
    }

    @Override
    protected InputStream openStream() throws IOException {
        return context.getAssets().open(assetName);
    }

    @Override
    protected String parseBookName() {
        return assetName;
    }


}
