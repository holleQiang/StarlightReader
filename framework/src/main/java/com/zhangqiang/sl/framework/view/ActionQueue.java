package com.zhangqiang.sl.framework.view;

import java.util.LinkedList;
import java.util.List;

public final class ActionQueue {

    private final List<Action> mActions = new LinkedList<>();

    static class Action {

        private Runnable runnable;
        private int delayMillions;

        Action(Runnable runnable, int delayMillions) {
            this.runnable = runnable;
            this.delayMillions = delayMillions;
        }
    }

    public void post(Runnable runnable) {
        mActions.add(new Action(runnable, 0));
    }

    public void postDelay(Runnable runnable, int delayMillions) {
        mActions.add(new Action(runnable, delayMillions));
    }

    public void executeActions(SLAttachInfo attachInfo) {
        for (int i = mActions.size() - 1; i >= 0; i--) {
            Action action = mActions.get(i);
            attachInfo.getHandler().postDelayed(action.runnable, action.delayMillions);
        }
        mActions.clear();
    }
}
