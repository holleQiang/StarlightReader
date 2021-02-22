package com.zhangqiang.starlightreader.extend;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public final void onNext(@NonNull T t) {
        boolean handed = handNext(t);
    }

    @Override
    public final void onError(@NonNull Throwable e) {
        boolean handed = handError(e);
        if (!handed) {
            e.printStackTrace();
        }
    }

    protected abstract boolean handNext(@NonNull T t);

    protected boolean handError(Throwable e) {
        return false;
    }
}
