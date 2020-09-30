package com.zhangqiang.sl.android;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.view.SLView;

public interface ISLView {

    void setContentView(SLView view);

    SLContext getSLContext();
}
