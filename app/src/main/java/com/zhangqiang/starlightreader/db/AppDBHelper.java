package com.zhangqiang.starlightreader.db;

import android.content.Context;
import android.support.annotation.Nullable;

import com.zhangqiang.fastdatabase.DBOpenHelper;
import com.zhangqiang.fastdatabase.entity.DBEntity;

import java.util.List;

public class AppDBHelper extends DBOpenHelper {

    private static AppDBHelper instance;

    private AppDBHelper(@Nullable Context context) {
        super(context, "reader", null, 1);
    }

    public static synchronized void init(Context context){
        if (instance == null) {
            instance = new AppDBHelper(context);
        }
    }

    public static AppDBHelper getInstance() {
        return instance;
    }

    @Override
    protected void onRegisterEntity(List<Class<? extends DBEntity>> entityClasses) {
        entityClasses.add(ReadRecordEntity.class);
    }
}
