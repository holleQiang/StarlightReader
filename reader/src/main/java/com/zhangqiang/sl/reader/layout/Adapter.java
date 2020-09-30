package com.zhangqiang.sl.reader.layout;

import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

public abstract class Adapter {

    public abstract SLView getView(SLViewGroup parent, SLView prevView, SLView convertView, int intent);

}
