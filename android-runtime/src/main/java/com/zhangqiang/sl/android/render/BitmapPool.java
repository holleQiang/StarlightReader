package com.zhangqiang.sl.android.render;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

public class BitmapPool {

    private static final int MAX_BITMAP_SIZE = 0xFFFB;
    private static final int WIDTH_SHIFT = 14;
    private static final int CONFIG_SHIFT = 28;
    private static final int MODE_MASK = 0xF << CONFIG_SHIFT;
    private static final  int WIDTH_MASK = MAX_BITMAP_SIZE << WIDTH_SHIFT;
    private static final  int HEIGHT_MASK = MAX_BITMAP_SIZE;
    private static final LruCache<Integer, BitmapRecord> mBitmapCache = new LruCache<Integer, BitmapRecord>(500 * 1024 * 1024) {
        @Override
        protected int sizeOf(@NonNull Integer key, @NonNull BitmapRecord value) {
            Bitmap bitmap = value.bitmap;
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                return bitmap.getAllocationByteCount();
            } else {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        }
    };


    public static Bitmap getBitmap(int width, int height, Bitmap.Config config) {
        int key = buildKey(width,height,config);
        BitmapRecord record = mBitmapCache.remove(key);
        if (record == null) {
            return Bitmap.createBitmap(width, height, config);
        }else {
            BitmapRecord head = record.next;
            Bitmap bitmap = record.bitmap;
            record.recycle();
            if (head != null) {
                mBitmapCache.put(key,head);
            }
            return bitmap;
        }
    }

    private static int buildKey(int width, int height, Bitmap.Config config) {
        if (width > MAX_BITMAP_SIZE || height > MAX_BITMAP_SIZE) {
            throw new IllegalArgumentException("with or height cannot be larger than : " + MAX_BITMAP_SIZE);
        }
        int configMapping;
        if (config == Bitmap.Config.ARGB_8888) {
            configMapping = 0x1;
        } else if (config == Bitmap.Config.ARGB_4444) {
            configMapping = 0x2;
        } else if (config == Bitmap.Config.RGB_565) {
            configMapping = 0x3;
        } else if (config == Bitmap.Config.ALPHA_8) {
            configMapping = 0x4;
        } else {
            throw new IllegalArgumentException("not support this bitmap type:" + config);
        }
        return (width << WIDTH_SHIFT) | height | (configMapping << CONFIG_SHIFT);
    }


    public static void remove(int width, int height, Bitmap.Config config) {
        int key = buildKey(width, height, config);
        BitmapRecord record = mBitmapCache.remove(key);
        if (record != null) {
            record.recycle();
        }
    }

    public static void put(Bitmap bitmap){
        int key = buildKey(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        BitmapRecord record = mBitmapCache.get(key);
        BitmapRecord newRecord = BitmapRecord.obtain();
        newRecord.bitmap = bitmap;
        newRecord.next = record;

        mBitmapCache.put(key,newRecord);
    }

    private static class BitmapRecord{

        private Bitmap bitmap;
        private BitmapRecord next;
        private static BitmapRecord sPool;

        private BitmapRecord() {
        }

        public static BitmapRecord obtain(){
            if (sPool == null) {
                return new BitmapRecord();
            }
            BitmapRecord target = sPool;
            sPool = sPool.next;
            return target;
        }

        public void recycle(){
            this.next = sPool;
            sPool = this;
            bitmap = null;
            next = null;
        }
    }
}
