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
import com.barryyang.photopicker.utils.SdCardUtils;
import com.barryyang.photopicker.utils.StringUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class FolderAdapter extends BaseAdapter {

    private List<PhotoFolder> mPhotoFolder;
    private Context mContext;

    public FolderAdapter(Context context, List<PhotoFolder> photoFolder) {
        this.mPhotoFolder = photoFolder;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mPhotoFolder == null) {
            return 0;
        }
        return mPhotoFolder.size();
    }

    @Override
    public PhotoFolder getItem(int position) {
        if (mPhotoFolder == null || mPhotoFolder.size() == 0) {
            return null;
        }
        return mPhotoFolder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_folder_item, null);
            viewHolder.mIvImage = convertView.findViewById(R.id.iv_image);
            viewHolder.mFolderName = convertView.findViewById(R.id.tv_folder_name);
            viewHolder.mPhotoTotal = convertView.findViewById(R.id.tv_photo_total);
            viewHolder.mSelected = convertView.findViewById(R.id.iv_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PhotoFolder folder = getItem(position);
        if (folder == null) {
            return convertView;
        }
        if (folder.getPhotoList() == null || folder.getPhotoList().size() == 0) {
            return convertView;
        }
        viewHolder.mSelected.setVisibility(View.GONE);
        viewHolder.mIvImage.setImageResource(R.drawable.ic_photo_loading);
        if (folder.isSelected()) {
            viewHolder.mSelected.setVisibility(View.VISIBLE);
        }
        viewHolder.mFolderName.setText(folder.getName());
        viewHolder.mPhotoTotal.setText(StringUtil.formatResourceString(mContext, R.string.app_photo_num, folder.getPhotoList().size()));
        Glide.with(mContext).load(folder.getPhotoList().get(0).getPath()).into(viewHolder.mIvImage);
        return convertView;
    }

    private class ViewHolder {
        private ImageView mIvImage;
        private TextView mFolderName;
        private TextView mPhotoTotal;
        private ImageView mSelected;
    }
}
