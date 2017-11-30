package com.barryyang.photopicker.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author：Administrator on 2017/11/30 18:32
 * @description:
 * @version:
 */
public class FileUtil {

    /**
     * 拍照相片存储文件
     *
     * @param context
     * @return
     */
    public static File createFile(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            file = new File(Environment.getExternalStorageDirectory() + File.separator + timeStamp + ".png");
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(System.currentTimeMillis());
            file = new File(cacheDir, timeStamp + ".png");
        }
        return file;
    }
}
