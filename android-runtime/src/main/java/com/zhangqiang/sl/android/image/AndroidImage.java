package com.zhangqiang.sl.android.image;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.zhangqiang.sl.android.render.BitmapPool;
import com.zhangqiang.sl.framework.image.SLImage;

public class AndroidImage extends SLImage {

    private Bitmap bitmap;

    public AndroidImage(int width,int height) {
        this.bitmap = BitmapPool.getBitmap(width,height, Bitmap.Config.ARGB_8888);
//        this.bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public void recycle() {
//        bitmap.recycle();
//        BitmapPool.remove(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        BitmapPool.put(bitmap);
    }

    @Override
    public boolean hasAlpha() {
        return bitmap.hasAlpha();
    }

    @Override
    public void eraseColor(int color) {
        bitmap.eraseColor(color);
    }
}
