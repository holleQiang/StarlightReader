package com.zhangqiang.starlightreader.model;

import com.zhangqiang.starlightreader.bean.BookShelfBean;

import java.util.List;

import io.reactivex.Observable;

public class BookShelfModel {

    public static Observable<List<BookShelfBean>> getAllBookShelfs(){

        return Observable.empty();
    }
}
