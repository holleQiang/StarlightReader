package com.zhangqiang.sl.reader.position;

import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.Element;
import com.zhangqiang.sl.reader.parser.Paragraph;

public class TextWordPosition {

    private int paragraphIndex;
    private int elementIndex;


    public TextWordPosition() {
    }

    public TextWordPosition(int paragraphIndex, int elementIndex) {
        this.paragraphIndex = paragraphIndex;
        this.elementIndex = elementIndex;
    }

    public int getParagraphIndex() {
        return paragraphIndex;
    }

    public void set(TextWordPosition position) {
        this.paragraphIndex = position.paragraphIndex;
        this.elementIndex = position.elementIndex;
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

    public void reset() {
        paragraphIndex = -1;
        elementIndex = -1;
    }

    public boolean isValid() {
        return paragraphIndex >= 0 && elementIndex >= 0;
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
        }else {

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
            next.setParagraphIndex(targetParagraphIndex);
            next.setElementIndex(targetElementIndex);
        }
        return next;
    }

    public static TextWordPosition previous(Book book, TextWordPosition curr) {
        TextWordPosition next = new TextWordPosition();
        if (isStartOfBook(book, curr)) {
            next.set(curr);
        }else {
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
            next.setParagraphIndex(targetParagraphIndex);
            next.setElementIndex(targetElementIndex);
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
                "paragraphIndex=" + paragraphIndex +
                ", elementIndex=" + elementIndex +
                '}';
    }
}
