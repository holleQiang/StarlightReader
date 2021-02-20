package com.zhangqiang.slreader.parser.impl.txt;

import com.zhangqiang.slreader.position.TextWordPosition;

public class Chapter {

    private String name;
    private final TextWordPosition startPosition = new TextWordPosition();
    private final TextWordPosition endPosition = new TextWordPosition();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextWordPosition getStartPosition() {
        return startPosition;
    }

    public TextWordPosition getEndPosition() {
        return endPosition;
    }
}
