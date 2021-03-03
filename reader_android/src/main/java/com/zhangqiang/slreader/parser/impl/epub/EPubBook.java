package com.zhangqiang.slreader.parser.impl.epub;

import com.zhangqiang.slreader.parser.Book;
import com.zhangqiang.slreader.parser.Paragraph;

public class EPubBook implements Book {

    nl.siegmann.epublib.domain.Book ePubBook;

    public EPubBook() {
    }

    @Override
    public String getName() {
        return ePubBook.getTitle();
    }

    @Override
    public int getParagraphCount() {
        return ePubBook.getContents().size();
    }

    @Override
    public Paragraph getParagraph(int index) {
        return null;
    }
}
