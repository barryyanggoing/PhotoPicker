package com.barryyang.photopicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.barryyang.photopicker.R;
import com.barryyang.photopicker.bean.Photo;
import com.barryyang.photopicker.listener.OnSelectPhotoListener;
import com.barryyang.photopicker.utils.ConstantUtil;
import com.barryyang.photopicker.utils.SdCardUtils;
import com.barryyang.photopicker.utils.StringUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class PhotoAdapter extends BaseAdapter {

    private Context mContext;
    private List<Photo> mPhotoList;

    private boolean mShowCamera;
    private int mMaxNum;
    private boolean mSelectMode;

    private OnSelectPhotoListener mOnSelectPhotoListener;

    private ArrayList<String> pathList = new ArrayList<>();

    public PhotoAdapter(Context context, List<Photo> photoList) {
        this.mPhotoList = photoList;
        this.mContext = context;
    }

    /**
     * 显示相机
     *
     * @param showCamera
     */
    public void setShowCamera(boolean showCamera) {
        this.mShowCamera = showCamera;
        if (mShowCamera) {
            Photo camera = new Photo(null);
            camera.setIsCamera(true);
            mPhotoList.add(0, camera);
        }
    }

    public void setOnSelectPhotoListener(OnSelectPhotoListener onSelectPhotoListener) {
        this.mOnSelectPhotoListener = onSelectPhotoListener;
    }

    /**
     * 设置最大的图片
     *
     * @param maxNum
     */
    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }

    /**
     * 单选还是多选
     *
     * @param selectMode
     */
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
        return mPhotoList.size();
    }

    @Override
    public Photo getItem(int position) {
        if (mPhotoList == null || mPhotoList.size() == 0) {
            return null;
        }
        return mPhotoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPhotoList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == ConstantUtil.TYPE_CAMERA) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_zero_camera, null);
            convertView.setTag(null);
        } else {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_photo_item, null);
                viewHolder.mImage = convertView.findViewById(R.id.iv_image);
                viewHolder.mSelected = convertView.findViewById(R.id.iv_selected);
                viewHolder.mViewBg = convertView.findViewById(R.id.view_bg);
                viewHolder.mFlLayout = convertView.findViewById(R.id.fl_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mImage.setImageResource(R.drawable.ic_photo_loading);
            final Photo photo = getItem(position);
            if (mSelectMode) {
                viewHolder.mSelected.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mSelected.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(photo.getPath()).into(viewHolder.mImage);
            viewHolder.mFlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectMode) {
                        String selectPath = photo.getPath();
                        if (pathList.contains(selectPath)) {
                            pathList.remove(selectPath);
                            viewHolder.mSelected.setSelected(false);
                            viewHolder.mViewBg.setVisibility(View.GONE);
                        } else {
                            if (pathList.size() >= mMaxNum) {
                                String notice = StringUtil.formatResourceString(mContext, R.string.app_photo_maxnum, mMaxNum);
                                Toast.makeText(mContext, notice, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            pathList.add(selectPath);
                            viewHolder.mSelected.setSelected(true);
                            viewHolder.mViewBg.setVisibility(View.VISIBLE);
                        }
                    } else {
                        pathList.add(photo.getPath());
                    }
                    if (mOnSelectPhotoListener != null) {
                        mOnSelectPhotoListener.onPhotoSelect(pathList);
                    }
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView mImage;
        private ImageView mSelected;
        private View mViewBg;
        private FrameLayout mFlLayout;
    }

}
