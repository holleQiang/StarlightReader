package com.zhangqiang.slreader;

import android.view.View;
import android.view.ViewGroup;

import com.zhangqiang.slreader.parser.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class PageViewAdapter {

    private final List<AdapterObserver> adapterObservers = new ArrayList<>();


    public abstract int getItemType(Element element);

    public abstract View getView(ViewGroup parent, Element element, int itemType, View convertView);


    public interface AdapterObserver {

        void onDataChanged();
    }

    public void registerAdapterObserver(AdapterObserver observer) {
        if (adapterObservers.contains(observer)) {
            return;
        }
        adapterObservers.add(observer);
    }

    public void unRegisterAdapterObserver(AdapterObserver observer) {
        adapterObservers.remove(observer);
    }

    protected void notifyDataChanged() {
        for (int i = adapterObservers.size() - 1; i >= 0; i--) {
            adapterObservers.get(i).onDataChanged();
        }
    }

}
