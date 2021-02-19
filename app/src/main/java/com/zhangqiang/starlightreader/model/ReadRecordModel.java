package com.zhangqiang.starlightreader.model;

import com.zhangqiang.fastdatabase.dao.Dao;
import com.zhangqiang.sl.reader.position.TextWordPosition;
import com.zhangqiang.starlightreader.bean.ReadRecordBean;
import com.zhangqiang.starlightreader.db.AppDBHelper;
import com.zhangqiang.starlightreader.db.ReadRecordEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ReadRecordModel {

    public static void updateReadPosition(String bookPath, TextWordPosition position) {
        Dao<ReadRecordEntity> dao = AppDBHelper.getInstance().getDao(ReadRecordEntity.class);
        List<ReadRecordEntity> entities = dao.query(null, "bookPath=?", new String[]{bookPath}, null, null, null, null);
        ReadRecordEntity recordEntity;
        if (entities != null && !entities.isEmpty()) {
            recordEntity = entities.get(0);
        } else {
            recordEntity = new ReadRecordEntity();
            recordEntity.setBookPath(bookPath);
        }
        recordEntity.setParagraphIndex(position.getParagraphIndex());
        recordEntity.setElementIndex(position.getElementIndex());
        recordEntity.setId(bookPath);
        dao.save(recordEntity);
    }

    public static TextWordPosition getReadPosition(String bookPath) {

        Dao<ReadRecordEntity> dao = AppDBHelper.getInstance().getDao(ReadRecordEntity.class);
        List<ReadRecordEntity> entities = dao.query(null, "bookPath=?", new String[]{bookPath}, null, null, null, null);
        if (entities != null && !entities.isEmpty()) {
            ReadRecordEntity entity = entities.get(0);
            TextWordPosition position = new TextWordPosition();
            position.set(entity.getParagraphIndex(), entity.getElementIndex());
            return position;
        }
        return new TextWordPosition(0, 0);
    }

    public static Observable<List<ReadRecordBean>> getAllReadRecords(){

        return Observable.create(new ObservableOnSubscribe<List<ReadRecordBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ReadRecordBean>> emitter) throws Exception {
                try {
                    List<ReadRecordEntity> entities = AppDBHelper.getInstance().getDao(ReadRecordEntity.class)
                            .query(null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    Dao.COLUMN_NAME_UPDATE_TIME+" desc"
                                    ,null);
                    List<ReadRecordBean> recordBeans = new ArrayList<>();
                    if (entities != null) {
                        for (int i = 0; i < entities.size(); i++) {
                            ReadRecordEntity entity = entities.get(i);
                            ReadRecordBean recordBean = new ReadRecordBean();
                            recordBeans.add(recordBean);
                        }
                    }
                    emitter.onNext(recordBeans);
                    emitter.onComplete();
                }catch (Throwable e){
                    emitter.onError(e);
                }
            }
        });
    }
}
