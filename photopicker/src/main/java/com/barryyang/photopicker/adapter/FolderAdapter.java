package com.barryyang.photopicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.barryyang.photopicker.R;
import com.barryyang.photopicker.bean.PhotoFolder;
import com.barryyang.photopicker.utils.OtherUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class FolderAdapter extends BaseAdapter {

    private List<PhotoFolder> mDatas;
    private Context mContext;
    private int mWidth;

    public FolderAdapter(Context context, List<PhotoFolder> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        mWidth = OtherUtils.dip2px(context, 90);
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public PhotoFolder getItem(int position) {
        if (mDatas == null || mDatas.size() == 0) {
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_folder_layout, null);
            holder.photoIV = convertView.findViewById(R.id.imageview_folder_img);
            holder.folderNameTV = convertView.findViewById(R.id.textview_folder_name);
            holder.photoNumTV = convertView.findViewById(R.id.textview_photo_num);
            holder.selectIV = convertView.findViewById(R.id.imageview_folder_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PhotoFolder folder = getItem(position);
        if (folder == null) {
            return convertView;
        }
        if (folder.getPhotoList() == null || folder.getPhotoList().size() == 0) {
            return convertView;
        }
        holder.selectIV.setVisibility(View.GONE);
        holder.photoIV.setImageResource(R.drawable.ic_photo_loading);
        if (folder.isSelected()) {
            holder.selectIV.setVisibility(View.VISIBLE);
        }
        holder.folderNameTV.setText(folder.getName());
        holder.photoNumTV.setText(folder.getPhotoList().size() + "张");
        Glide.with(mContext).load(folder.getPhotoList().get(0).getPath()).into(holder.photoIV);
        return convertView;
    }

    private class ViewHolder {
        private ImageView photoIV;
        private TextView folderNameTV;
        private TextView photoNumTV;
        private ImageView selectIV;
    }
}
