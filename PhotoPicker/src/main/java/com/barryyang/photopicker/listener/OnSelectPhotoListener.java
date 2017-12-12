package com.barryyang.photopicker.listener;

import java.util.ArrayList;

/**
 * @author：barryyang on 2017/11/30 13:29
 * @description:
 * @version:
 */
public interface OnSelectPhotoListener {
    void onPhotoSelect(ArrayList<String> selectList, int position, boolean mIsSelected);
}
