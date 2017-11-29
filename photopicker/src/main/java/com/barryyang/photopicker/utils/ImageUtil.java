package com.barryyang.photopicker.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author：barryyang on 2017/11/28 11:21
 * @description:图片类工具
 * @version:
 */
public class ImageUtil {

    /**
     * 获取图片的宽高
     *
     * @param inputStream
     * @return
     */
    public static String[] getImageSize(InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        return new String[]{options.outWidth + "", options.outHeight + ""};
    }

    /**
     * 将图片旋转
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap roateBitmap(int angle, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap roateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return roateBitmap;
    }

    /**
     * bitmap Base64
     * @param bitmap
     * @return
     */
    public static String bitmapEncode(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,bos);
        byte[] bytes = bos.toByteArray();
        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
        String encodeString = new String(encode);
        return encodeString;
    }

    /**
     * 压缩图片
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Bitmap compImage(String path, int reqWidth, int reqHeight) {
        BufferedInputStream in = null;
        try {
            int inSampleSize = 1;
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            int outWidth = options.outWidth;
            int outHeight = options.outHeight;
            if (outWidth > reqWidth || outHeight > reqHeight) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                int halfHeight = outHeight / 2;
                int halfWidth = outWidth / 2;
                while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
                options.inSampleSize = inSampleSize;
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 设置图片的宽高
     * @param view
     * @param width
     * @param index
     */
    public static void setImageViewWidth(View view,int width,int index) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width / index;
        params.height = width / index;
        view.setLayoutParams(params);
    }
}
