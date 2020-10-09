package com.zhangqiang.sl.reader.position;

import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.Element;
import com.zhangqiang.sl.reader.parser.Paragraph;

public class TextWordPosition {

    private int mParagraphIndex;
    private int mElementIndex;

    public TextWordPosition() {
    }

    public TextWordPosition(int paragraphIndex, int elementIndex) {
        mParagraphIndex = paragraphIndex;
        mElementIndex = elementIndex;
    }

    public int getParagraphIndex() {
        return mParagraphIndex;
    }

    public void set(TextWordPosition position) {
        this.mParagraphIndex = position.mParagraphIndex;
        mElementIndex = position.mElementIndex;
    }

    public void set(int paragraphIndex, int elementIndex) {
        mParagraphIndex = paragraphIndex;
        mElementIndex = elementIndex;
    }

    public int getElementIndex() {
        return mElementIndex;
    }

    public void reset() {
        mParagraphIndex = 0;
        mElementIndex = 0;
    }

    public static boolean isEndOfBook(Book book, TextWordPosition position) {
        int lastParagraphIndex = book.getParagraphCount() - 1;
        return lastParagraphIndex == position.getParagraphIndex()
                && book.getParagraph(lastParagraphIndex).getElementCount() - 1 == position.getElementIndex();
    }

    public static boolean isStartOfBook(Book book, TextWordPosition position) {
        return position.getParagraphIndex() == 0 && position.getElementIndex() == 0;
    }

    public static TextWordPosition next(Book book, TextWordPosition curr) {

        TextWordPosition next = new TextWordPosition();

        if (isEndOfBook(book, curr)) {
            next.set(curr);
        } else {

            int targetParagraphIndex;
            int targetElementIndex;
            int paragraphIndex = curr.getParagraphIndex();
            int elementIndex = curr.getElementIndex();
            Paragraph paragraph = book.getParagraph(paragraphIndex);
            int elementCount = paragraph.getElementCount();
            if (elementIndex == elementCount - 1) {
                targetParagraphIndex = paragraphIndex + 1;
                targetElementIndex = 0;
            } else {
                targetParagraphIndex = paragraphIndex;
                targetElementIndex = elementIndex + 1;
            }
            next.set(targetParagraphIndex, targetElementIndex);
        }
        return next;
    }

    public static TextWordPosition previous(Book book, TextWordPosition curr) {
        TextWordPosition next = new TextWordPosition();
        if (isStartOfBook(book, curr)) {
            next.set(curr);
        } else {
            int targetParagraphIndex;
            int targetElementIndex;
            int paragraphIndex = curr.getParagraphIndex();
            int elementIndex = curr.getElementIndex();
            if (elementIndex == 0) {
                targetParagraphIndex = paragraphIndex - 1;
                targetElementIndex = book.getParagraph(targetParagraphIndex).getElementCount() - 1;
            } else {
                targetParagraphIndex = paragraphIndex;
                targetElementIndex = elementIndex - 1;
            }
            next.set(targetParagraphIndex, targetElementIndex);
        }
        return next;
    }

    public static boolean isEndOfParagraph(Book book, TextWordPosition position) {
        return book.getParagraph(position.getParagraphIndex()).getElementCount() - 1 == position.getElementIndex();
    }

    public static boolean isStartOfParagraph(Book book, TextWordPosition position) {
        return position.getElementIndex() == 0;
    }

    @Override
    public String toString() {
        return "TextWordPosition{" +
                "paragraphIndex=" + getParagraphIndex() +
                ", elementIndex=" + getElementIndex() +
                '}';
    }


}
