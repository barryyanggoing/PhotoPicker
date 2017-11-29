package com.barryyang.photopicker.listener;

import com.barryyang.photopicker.adapter.PhotoAdapter;

/**
 * @author：Administrator on 2017/11/29 13:13
 * @description:
 * @version:
 */
public interface CheckBoxClickListener {
    void selected(PhotoAdapter.ViewHolder viewHolder,int position);
    void unSelected(PhotoAdapter.ViewHolder viewHolder,int position);
}
