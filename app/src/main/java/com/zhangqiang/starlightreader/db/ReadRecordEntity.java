package com.zhangqiang.starlightreader.db;

import com.zhangqiang.fastdatabase.entity.DBEntity;

public class ReadRecordEntity extends DBEntity {

    private String bookPath;
    private int paragraphIndex;
    private int elementIndex;

    public int getParagraphIndex() {
        return paragraphIndex;
    }

    public void setParagraphIndex(int paragraphIndex) {
        this.paragraphIndex = paragraphIndex;
    }

    public int getElementIndex() {
        return elementIndex;
    }

    public void setElementIndex(int elementIndex) {
        this.elementIndex = elementIndex;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }
}
