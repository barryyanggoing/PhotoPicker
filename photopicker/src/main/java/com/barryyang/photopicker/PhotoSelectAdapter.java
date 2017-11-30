package com.barryyang.photopicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.barryyang.photopicker.widget.SquareImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * @author：barryyang on 2017/11/30 13:49
 * @description:
 * @version:
 */
class PhotoSelectAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mPathList;

    public PhotoSelectAdapter(Context context, ArrayList<String> pathList) {
        this.mContext = context;
        this.mPathList = pathList;
    }

    @Override
    public int getCount() {
        return mPathList.size();
    }

    @Override
    public String getItem(int position) {
        return mPathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String path = mPathList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, null);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(path).into(viewHolder.imageView);
        return convertView;
    }

    class ViewHolder {
        private SquareImageView imageView;
    }
}
