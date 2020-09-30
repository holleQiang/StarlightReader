package com.zhangqiang.sl.framework.view;

import com.zhangqiang.sl.framework.render.SLFramePoster;

public abstract class FramePosterFactory {

    public SLFramePoster create(){
        return onCreateFramePoster();
    }

    protected abstract SLFramePoster onCreateFramePoster();
}
