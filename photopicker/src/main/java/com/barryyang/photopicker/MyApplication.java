package com.barryyang.photopicker;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }


}
