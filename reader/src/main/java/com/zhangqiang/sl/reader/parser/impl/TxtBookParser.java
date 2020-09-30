package com.zhangqiang.sl.reader.parser.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TxtBookParser extends BaseTxtBookParser {

    private String filePath;

    public TxtBookParser(String charset, String filePath) {
        super(charset);
        this.filePath = filePath;
    }

    @Override
    protected InputStream openStream() throws IOException{
        return new FileInputStream(filePath);
    }


}
