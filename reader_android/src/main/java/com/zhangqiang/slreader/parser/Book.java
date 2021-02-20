package com.zhangqiang.slreader.parser;

public interface Book {

    String getName();

    int getParagraphCount();

    Paragraph getParagraph(int index);
}
