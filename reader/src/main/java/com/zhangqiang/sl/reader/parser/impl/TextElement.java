package com.zhangqiang.sl.reader.parser.impl;

import com.zhangqiang.sl.reader.parser.Element;

public class TextElement implements Element {

    private String text;

    public TextElement(char c) {
        this(String.valueOf(c));
    }

    public TextElement(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
