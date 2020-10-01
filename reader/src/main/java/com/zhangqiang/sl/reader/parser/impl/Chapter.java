package com.zhangqiang.sl.reader.parser.impl;

import com.zhangqiang.sl.reader.position.TextWordPosition;

public class Chapter {

    private String name;
    private final TextWordPosition position = new TextWordPosition();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextWordPosition getPosition() {
        return position;
    }
}
