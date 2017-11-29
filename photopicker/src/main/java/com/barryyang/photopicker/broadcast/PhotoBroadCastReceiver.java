package com.barryyang.photopicker.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.barryyang.photopicker.bean.ImageInfo;
import com.barryyang.photopicker.utils.ConstantsUtil;
import com.barryyang.photopicker.utils.PhotoUtil;

import java.util.ArrayList;

/**
 * @author：barryyang on 2017/11/29 14:39
 * @description: 接收选中的图片
 * @version:
 */
public abstract class PhotoBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConstantsUtil.RECEIVER_SUBMIT)){
            receiverPhoto(PhotoUtil.imageSelected);
        }
    }

    protected abstract void receiverPhoto(ArrayList<ImageInfo> imageSelected);
}
