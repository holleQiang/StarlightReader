package com.zhangqiang.sl.reader.page;

import com.zhangqiang.sl.framework.view.SLView;
import com.zhangqiang.sl.framework.view.SLViewGroup;
import com.zhangqiang.sl.reader.parser.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class Adapter {

    private final List<AdapterObserver> adapterObservers = new ArrayList<>();


    public abstract int getItemType(Element element);

    public abstract SLView getView(SLViewGroup parent, Element element,int itemType, SLView convertView);


    public interface AdapterObserver {

        void onRequestLayout();

        void onRequestInvalidate();
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

    protected void requestInvalidate() {
        for (int i = adapterObservers.size() - 1; i >= 0; i--) {
            adapterObservers.get(i).onRequestInvalidate();
        }
    }

    protected void requestLayout() {
        for (int i = adapterObservers.size() - 1; i >= 0; i--) {
            adapterObservers.get(i).onRequestLayout();
        }
    }
}
