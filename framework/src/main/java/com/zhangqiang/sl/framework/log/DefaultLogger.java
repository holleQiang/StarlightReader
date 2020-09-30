package com.zhangqiang.sl.framework.log;

public class DefaultLogger extends SLLogger {

    @Override
    protected void onLogI(String tag, String log) {
        System.out.println("tag:" + tag + ",log:" + log);
    }

    @Override
    protected void onLogW(String tag, String log) {
        System.out.println("tag:" + tag + ",log:" + log);
    }


    @Override
    protected void onLogE(String tag, String log) {
        System.err.println("tag:" + tag + ",log:" + log);
    }
}
