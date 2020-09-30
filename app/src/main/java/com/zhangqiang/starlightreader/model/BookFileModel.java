package com.zhangqiang.starlightreader.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zhangqiang.starlightreader.bean.BookFileBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class BookFileModel {

    public static Observable<List<BookFileBean>> getAllLocalBookFiles(Context context) {


        return Observable.create(new ObservableOnSubscribe<List<BookFileBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookFileBean>> emitter) throws Exception {
                Cursor cursor = null;
                try {

                    ArrayList<BookFileBean> beanList = new ArrayList<>();
                    Uri uri = MediaStore.Files.getContentUri("external");
                    cursor = context.getContentResolver().query(uri,
                            null,
                            MediaStore.Files.FileColumns.MIME_TYPE + " = ? and "
                                    + MediaStore.Files.FileColumns.SIZE + " > ?",
                            new String[]{"text/plain", "102400"},
                            null);

                    if (cursor != null) {
                        while (cursor.moveToNext()) {

                            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                            int fileLength = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                            String mineType = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE));
                            BookFileBean bookFileBean = new BookFileBean();
                            bookFileBean.setFileLength(fileLength);
                            bookFileBean.setFileName(title);
                            bookFileBean.setFilePath(path);
                            bookFileBean.setMimeType(mineType);
                            beanList.add(bookFileBean);
                        }
                    }
                    emitter.onNext(beanList);
                    emitter.onComplete();
                } catch (Throwable e) {
                    emitter.onError(e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });

    }
}
