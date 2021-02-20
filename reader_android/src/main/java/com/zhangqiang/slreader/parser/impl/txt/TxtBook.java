package com.zhangqiang.slreader.parser.impl.txt;

import com.zhangqiang.slreader.parser.Book;
import com.zhangqiang.slreader.parser.Paragraph;
import com.zhangqiang.slreader.position.TextWordPosition;

import java.util.List;

public class TxtBook implements Book {

    private List<Chapter> chapters;
    private List<Paragraph> paragraphs;
    private String mBookName;

    public TxtBook(String bookName, List<Chapter> chapters, List<Paragraph> paragraphs) {
        this.mBookName = bookName;
        this.chapters = chapters;
        this.paragraphs = paragraphs;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    @Override
    public String getName() {
        return mBookName;
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

    public String getChapterName(TextWordPosition position) {
        if (chapters == null || chapters.isEmpty()) {
            return null;
        }
        int paragraphIndex = position.getParagraphIndex();
        for (int i = 0; i < chapters.size(); i++) {
            Chapter chapter = chapters.get(i);
            if (paragraphIndex >= chapter.getStartPosition().getParagraphIndex()
                    && paragraphIndex <= chapter.getEndPosition().getParagraphIndex()) {
                return chapter.getName();
            }
        }
        return null;
    }

}
