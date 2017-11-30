package com.barryyang.photopicker.utils;

import android.content.Context;

/**
 * @author：Administrator on 2017/11/30 18:31
 * @description:
 * @version:
 */
public class DensityUtil {

    /**
     * dp转px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
         float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
