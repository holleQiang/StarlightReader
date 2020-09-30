package com.zhangqiang.sl.reader.parser.impl;

import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.Paragraph;

import java.util.List;

public class TextBookImpl implements Book {

    private List<Paragraph> paragraphs;

    public TextBookImpl(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    @Override
    public int getParagraphCount() {
        return paragraphs != null ? paragraphs.size() : 0;
    }

    @Override
    public Paragraph getParagraph(int index) {
        if (paragraphs == null) {
            return null;
        }
        return paragraphs.get(index);
    }
}
