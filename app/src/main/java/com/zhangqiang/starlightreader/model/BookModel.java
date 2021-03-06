package com.zhangqiang.starlightreader.model;

import android.content.Context;

import com.zhangqiang.slreader.parser.Book;
import com.zhangqiang.slreader.parser.BookParser;
import com.zhangqiang.slreader.parser.impl.txt.TxtBookParser;
import com.zhangqiang.starlightreader.reader.AssetTxtParser;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class BookModel {

    public static Observable<Book> parseBook(String filePath, String charset) {

        return parseBook(new TxtBookParser(charset, filePath));
    }

    public static Observable<Book> parseAssetBook(Context context, String fileName, String charset){
        return parseBook(new AssetTxtParser(charset,context,fileName));
    }


    public static Observable<Book> parseBook(BookParser bookParser) {

        return Observable.create(new ObservableOnSubscribe<Book>() {
            @Override
            public void subscribe(ObservableEmitter<Book> emitter) throws Exception {
                try {
                    Book book = bookParser.parse();
                    emitter.onNext(book);
                    emitter.onComplete();
                } catch (Throwable e) {
                    emitter.onError(e);
                }
            }
        });
    }
}
