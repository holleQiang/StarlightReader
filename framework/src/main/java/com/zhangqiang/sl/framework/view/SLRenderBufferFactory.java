package com.zhangqiang.sl.framework.view;

import com.zhangqiang.sl.framework.render.SLRenderBuffer;

public interface SLRenderBufferFactory {

    SLRenderBuffer create(int width,int height);
}
