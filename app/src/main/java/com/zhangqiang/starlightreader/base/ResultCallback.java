package com.zhangqiang.starlightreader.base;

import android.support.annotation.NonNull;

public interface ResultCallback<T> extends ErrorCallback{

    void onSuccess(@NonNull T t);

}
