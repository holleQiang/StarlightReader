package com.zhangqiang.sl.reader.parser;

public interface Book {

    String getName();

    int getParagraphCount();

    Paragraph getParagraph(int index);
}
