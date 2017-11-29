package com.barryyang.photopicker.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import com.barryyang.photopicker.bean.ImageInfo;

import java.util.ArrayList;


public class PhotoUtil {

    private Context context;
    private ContentResolver contentResolver;
    private ArrayList<ImageInfo> mImageList = new ArrayList<>();

    public static ArrayList<ImageInfo> imageSelected = new ArrayList<>();

    public PhotoUtil(Context context) {
        this.context = context.getApplicationContext();
        this.contentResolver = this.context.getContentResolver();
    }

    /**
     * 获取本地图片List
     *
     * @return
     */
    public ArrayList<ImageInfo> getImagesBucketList() {
        Cursor cur = contentResolver.query(Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.MIME_TYPE + " in(?, ?)", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cur.moveToFirst()) {
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            do {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setPhotoId(cur.getString(photoIDIndex));
                imageInfo.setPhotoPath(cur.getString(photoPathIndex));
                imageInfo.setPhotoName(cur.getString(photoNameIndex));
                mImageList.add(imageInfo);
            } while (cur.moveToNext());
        }
        cur.close();
        return mImageList;
    }
}
