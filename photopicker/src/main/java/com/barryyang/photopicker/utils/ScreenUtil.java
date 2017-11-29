package com.barryyang.photopicker.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author：barryyang on 2017/11/28 10:45
 * @description:获取屏幕的宽高和密度
 * @version:
 */
public class ScreenUtil {

    /**
     * 获取屏幕的宽高
     *
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    /**
     * 获取屏幕的宽高
     *
     * @param context
     * @return
     */
    public static String[] getScreenSizeRes(Context context){
        DisplayMetrics displayMetrics1 = context.getResources().getDisplayMetrics();
        int heightPixels = displayMetrics1.heightPixels;
        int widthPixels = displayMetrics1.widthPixels;
        return new String[]{widthPixels + "", heightPixels + ""};
    }

    /**
     * 获取屏幕的密度
     *
     * @param context
     * @return
     */
    public static float getDeviceDensity(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics.density;
    }
}
