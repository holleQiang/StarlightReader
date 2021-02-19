package com.zhangqiang.starlightreader.db;

import com.zhangqiang.fastdatabase.entity.DBEntity;

public class ChapterEntity extends DBEntity {

    private int startParagraphIndex;
    private int startElementIndex;
    private int endParagraphIndex;
    private int endElementIndex;
    private String bookPath;
    private String chapterName;

    public int getStartParagraphIndex() {
        return startParagraphIndex;
    }

    public void setStartParagraphIndex(int startParagraphIndex) {
        this.startParagraphIndex = startParagraphIndex;
    }

    public int getStartElementIndex() {
        return startElementIndex;
    }

    public void setStartElementIndex(int startElementIndex) {
        this.startElementIndex = startElementIndex;
    }

    public int getEndParagraphIndex() {
        return endParagraphIndex;
    }

    public void setEndParagraphIndex(int endParagraphIndex) {
        this.endParagraphIndex = endParagraphIndex;
    }

    public int getEndElementIndex() {
        return endElementIndex;
    }

    public void setEndElementIndex(int endElementIndex) {
        this.endElementIndex = endElementIndex;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
}
