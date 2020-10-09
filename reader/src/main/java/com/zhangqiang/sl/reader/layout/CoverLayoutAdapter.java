package com.zhangqiang.sl.reader.layout;

import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class CoverLayoutAdapter {

    private List<Observer> observers = new ArrayList<>();

    public abstract SLView getView(SLViewGroup parent, SLView prevView, SLView convertView, int intent);

    public  interface Observer{

        void onDataChanged();
    }

    public void registerObserver(Observer observer){
        if (observers.contains(observer)) {
            return;
        }
        observers.add(observer);
    }

    public void unRegisterObserver(Observer observer){
        observers.remove(observer);
    }

    protected void notifyDataChanged(){
        for (int i = observers.size() - 1; i >= 0; i--) {
            observers.get(i).onDataChanged();
        }
    }
}
