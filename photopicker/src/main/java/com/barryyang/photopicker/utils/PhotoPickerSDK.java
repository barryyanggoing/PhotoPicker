package com.barryyang.photopicker.utils;

import android.content.Context;
import android.content.Intent;

import com.barryyang.photopicker.PhotoPickerActivity;

/**
 * @author：barryyang on 2017/11/30 10:10
 * @description:
 * @version:
 */
public class PhotoPickerSDK {

    private static PhotoPickerSDK photoPickerSDK;
    private Context mContext;
    private boolean mShowCamera;
    private boolean mSelectMode;
    private int mMaxSelect;

    public static PhotoPickerSDK getInstance() {
        if (photoPickerSDK == null) {
            synchronized (PhotoPickerSDK.class) {
                if (photoPickerSDK == null) {
                    photoPickerSDK = new PhotoPickerSDK();
                }
            }
        }
        return photoPickerSDK;
    }

    /**
     * @param context
     * @param showCamera 是否显示相机 true 显示  false 不显示
     * @param selectMode 单选还是多选 0 单选 1 多选
     * @param maxSelect  大多选择的图片，<0 则为9 >9 则为9
     */
    public void init(Context context, boolean selectMode, boolean showCamera, int maxSelect) {
        this.mContext = context;
        this.mSelectMode = selectMode;
        this.mShowCamera = showCamera;
        this.mMaxSelect = maxSelect;
    }

    /**
     * 进入相册
     */
    public void photoPicker() {
        Intent intent = new Intent(mContext, PhotoPickerActivity.class);
        intent.putExtra(ConstantUtil.SHOW_CAMERA, mShowCamera);
        intent.putExtra(ConstantUtil.SELECT_MODE, mSelectMode);
        intent.putExtra(ConstantUtil.SELECT_MAX, mMaxSelect);
        mContext.startActivity(intent);
    }
}
