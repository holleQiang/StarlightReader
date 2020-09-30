package com.zhangqiang.sl.reader.parser.impl;

import com.zhangqiang.sl.reader.parser.Element;
import com.zhangqiang.sl.reader.parser.Paragraph;

import java.util.List;

public class ParagraphImpl implements Paragraph {

    private List<Element> elements;
    private int start;
    private int offset;

    public ParagraphImpl(List<Element> elements, int start, int offset) {
        this.elements = elements;
        this.start = start;
        this.offset = offset;
    }

    @Override
    public int getElementCount() {
        return offset;
    }

    @Override
    public Element getElement(int index) {

        if (index > offset - 1 || index < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return elements.get(start + index);
    }
}
