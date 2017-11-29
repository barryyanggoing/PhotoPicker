package com.barryyang.photopicker.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import com.barryyang.photopicker.R;
import com.barryyang.photopicker.utils.PhotoUtil;
import com.bumptech.glide.Glide;

/**
 * @author：Administrator on 2017/11/29 15:31
 * @description:
 * @version:
 */
public class PreViewAdapter extends PagerAdapter {

    private Context context;

    public PreViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return PhotoUtil.imageSelected.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_photo_pre, null);
        ImageView imageView = view.findViewById(R.id.iv_image);
        String imgUrl = PhotoUtil.imageSelected.get(position).getPhotoPath();
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(context).load(imgUrl).into(imageView);
            ((ViewPager) container).addView(view, 0);
        }
        return view;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
