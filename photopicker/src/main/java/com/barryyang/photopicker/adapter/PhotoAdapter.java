package com.barryyang.photopicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.barryyang.photopicker.R;
import com.barryyang.photopicker.bean.Photo;
import com.barryyang.photopicker.listener.OnSelectPhotoListener;
import com.barryyang.photopicker.utils.ConstantUtil;
import com.barryyang.photopicker.utils.OtherUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class PhotoAdapter extends BaseAdapter {

    private Context mContext;
    private List<Photo> mDatas;

    private boolean mShowCamera;
    private int mMaxNum;
    private boolean mSelectMode;
    private int mWidth;

    private OnSelectPhotoListener mOnSelectPhotoListener;

    public PhotoAdapter(Context context, List<Photo> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        int screenWidth = OtherUtils.getWidthInPx(mContext);
        mWidth = (screenWidth - OtherUtils.dip2px(mContext, 4)) / 3;
    }

    public void setShowCamera(boolean showCamera) {
        this.mShowCamera = showCamera;
        if (mShowCamera) {
            Photo camera = new Photo(null);
            camera.setIsCamera(true);
            mDatas.add(0, camera);
        }
    }

    public void setOnSelectPhotoListener(OnSelectPhotoListener onSelectPhotoListener) {
        this.mOnSelectPhotoListener = onSelectPhotoListener;
    }

    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }

    public void setSelectMode(boolean selectMode) {
        this.mSelectMode = selectMode;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Photo photo = getItem(position);
        if (photo != null && photo.isCamera()) {
            return ConstantUtil.TYPE_CAMERA;
        } else {
            return ConstantUtil.TYPE_PHOTO;
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Photo getItem(int position) {
        if (mDatas == null || mDatas.size() == 0) {
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDatas.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == ConstantUtil.TYPE_CAMERA) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_camera_layout, null);
            convertView.setTag(null);
            GridView.LayoutParams lp = new GridView.LayoutParams(mWidth, mWidth);
            convertView.setLayoutParams(lp);
        } else {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_photo_layout, null);
                holder.photoImageView = convertView.findViewById(R.id.imageview_photo);
                holder.selectView = convertView.findViewById(R.id.checkmark);
                holder.maskView = convertView.findViewById(R.id.mask);
                holder.wrapLayout = convertView.findViewById(R.id.wrap_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.photoImageView.setImageResource(R.drawable.ic_photo_loading);
            Photo photo = getItem(position);
            if (mSelectMode) {
                holder.selectView.setVisibility(View.VISIBLE);
            } else {
                holder.selectView.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(photo.getPath()).into(holder.photoImageView);
            holder.wrapLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSelectPhotoListener != null) {
                        mOnSelectPhotoListener.onPhotoSelect(position);
                    }
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView photoImageView;
        private ImageView selectView;
        private View maskView;
        private FrameLayout wrapLayout;
    }

}
