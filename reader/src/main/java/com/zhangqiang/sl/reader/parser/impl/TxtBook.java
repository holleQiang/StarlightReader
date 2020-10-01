package com.zhangqiang.sl.reader.parser.impl;

import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.Paragraph;

import java.util.List;

public class TxtBook implements Book {

    private List<Chapter> chapters;
    private List<Paragraph> paragraphs;

    public TxtBook(List<Chapter> chapters, List<Paragraph> paragraphs) {
        this.chapters = chapters;
        this.paragraphs = paragraphs;
    }

    public List<Chapter> getChapters() {
        return chapters;
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
