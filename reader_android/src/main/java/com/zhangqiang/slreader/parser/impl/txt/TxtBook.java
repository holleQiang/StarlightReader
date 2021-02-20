package com.zhangqiang.slreader.parser.impl.txt;

import com.zhangqiang.slreader.parser.Book;
import com.zhangqiang.slreader.parser.Paragraph;
import com.zhangqiang.slreader.position.TextWordPosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TxtBook implements Book {

    private List<Chapter> chapters;
    private List<Paragraph> paragraphs;
    private final Map<Integer, Chapter> mChapterSearchCache = new HashMap<>();
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

        Chapter targetChapter = mChapterSearchCache.get(paragraphIndex);
        if (targetChapter == null) {

            Chapter preChapter = null;
            int chapterCount = chapters.size();
            for (int i = 0; i < chapterCount; i++) {
                Chapter chapter = chapters.get(i);
                TextWordPosition chapterPosition = chapter.getPosition();
                int tempIndex = chapterPosition.getParagraphIndex();
                if (preChapter != null && paragraphIndex >= preChapter.getPosition().getParagraphIndex() && paragraphIndex < tempIndex) {
                    targetChapter = preChapter;
                    break;
                } else if (i == chapterCount - 1) {
                    targetChapter = chapter;
                }
                preChapter = chapter;
            }
        }
        if (targetChapter != null) {
            mChapterSearchCache.put(paragraphIndex, targetChapter);
            return targetChapter.getName();
        }
        return null;
    }
}
