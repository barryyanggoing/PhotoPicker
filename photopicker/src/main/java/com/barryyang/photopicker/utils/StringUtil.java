package com.barryyang.photopicker.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * @author：Administrator on 2017/11/30 18:33
 * @description:
 * @version:
 */
public class StringUtil {

    /**
     * 根据string.xml资源格式化字符串
     *
     * @param context
     * @param resource
     * @param args
     * @return
     */
    public static String formatResourceString(Context context, int resource, Object... args) {
        String str = context.getResources().getString(resource);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return String.format(str, args);
    }
}
