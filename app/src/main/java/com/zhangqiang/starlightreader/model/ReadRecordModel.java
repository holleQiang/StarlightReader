package com.zhangqiang.starlightreader.model;

import com.zhangqiang.fastdatabase.dao.Dao;
import com.zhangqiang.sl.reader.position.TextWordPosition;
import com.zhangqiang.starlightreader.db.AppDBHelper;
import com.zhangqiang.starlightreader.db.ReadRecordEntity;

import java.util.List;

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
}
