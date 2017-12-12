package com.barryyang.photopicker.utils;

import android.os.Environment;

public class SdCardUtils {

    /**
     * 判断外部存储卡是否可用
     *
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

}
