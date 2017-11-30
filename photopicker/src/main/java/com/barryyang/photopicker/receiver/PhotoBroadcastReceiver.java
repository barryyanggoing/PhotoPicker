package com.barryyang.photopicker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.barryyang.photopicker.utils.ConstantUtil;

import java.util.ArrayList;

/**
 * @author：Administrator on 2017/11/30 13:43
 * @description:
 * @version:
 */
public abstract class PhotoBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConstantUtil.PHOTO_PICKER)){
            ArrayList<String> pathList = intent.getStringArrayListExtra("path");
            photoSelectSuccess(pathList);
        }
    }

    protected abstract void photoSelectSuccess(ArrayList<String> pathList);
}
