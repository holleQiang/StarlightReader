package com.zhangqiang.sl.framework.log;

public abstract class SLLogger {

    private boolean open = true;

    public final void logI(String tag, String log) {
        if (!open) {
            return;
        }
        onLogI(tag, log);
    }

    protected abstract void onLogI(String tag, String log);

    public final void logW(String tag, String log) {
        if (!open) {
            return;
        }
        onLogW(tag, log);
    }

    protected abstract void onLogW(String tag, String log);

    public final void logE(String tag, String log) {
        if (!open) {
            return;
        }
        onLogE(tag, log);
    }

    protected abstract void onLogE(String tag, String log);

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
