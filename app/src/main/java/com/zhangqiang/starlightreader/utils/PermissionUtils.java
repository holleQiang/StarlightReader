package com.zhangqiang.starlightreader.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.zhangqiang.permissionrequest.PermissionRequestHelper;
import com.zhangqiang.starlightreader.base.ResultCallback;

public class PermissionUtils {

    public static void requestSDPermission(FragmentActivity activity, ResultCallback<String[]> callback) {
        if (activity == null || callback == null) {
            return;
        }
        PermissionRequestHelper.requestPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionRequestHelper.Callback() {
            @Override
            public void onPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callback.onSuccess(permissions);
                }else {
                    callback.onError(new PermissionRefusedException("permission refused"));
                }
            }
        });
    }

    public static boolean hasSDPermission(FragmentActivity activity){
        return ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static class PermissionRefusedException extends RuntimeException{

        PermissionRefusedException(String message) {
            super(message);
        }
    }
}
