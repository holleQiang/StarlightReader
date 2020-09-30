package com.zhangqiang.sl.reader.parser.impl;

import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.Paragraph;

public class TxtBook implements Book {

    private int paragraphCount;
    private LruCache<Integer,Paragraph> paragraphCache = new LruCache<>(50);

    @Override
    public int getParagraphCount() {
        return 0;
    }

    @Override
    public Paragraph getParagraph(int index) {
        return paragraphCache.get(index);
    }
}
