package com.barryyang.photopicker.utils;

import android.content.Context;

/**
 * @author：Administrator on 2017/11/30 18:28
 * @description:
 * @version:
 */
public class ScreenUtil {

    /**
     * 获取屏幕的高度和高度
     *
     * @param context
     * @return
     */
    public static Integer[] getScreenWH(Context context) {
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return new Integer[]{width, height};
    }


}
