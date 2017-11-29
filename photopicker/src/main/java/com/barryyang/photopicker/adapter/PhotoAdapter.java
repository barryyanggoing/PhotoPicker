package com.barryyang.photopicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.barryyang.photopicker.bean.ImageInfo;
import com.barryyang.photopicker.R;
import com.barryyang.photopicker.listener.CheckBoxClickListener;
import com.barryyang.photopicker.utils.ConstantsUtil;
import com.barryyang.photopicker.utils.ImageUtil;
import com.barryyang.photopicker.utils.PhotoUtil;
import com.barryyang.photopicker.utils.ScreenUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * @author：barryyang on 2017/11/28 16:38
 * @description:
 * @version:
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ImageInfo> imageList;
    private int width;
    private CheckBoxClickListener mCheckBoxClickListener;

    public PhotoAdapter(Context context, ArrayList<ImageInfo> imageList) {
        this.context = context;
        this.imageList = imageList;
        this.width = ScreenUtil.getScreenSize(context)[0];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_photo_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        ImageInfo imageInfo = imageList.get(position);
        ImageUtil.setImageViewWidth(viewHolder.fl_layout, width, 3);
        Glide.with(context).load(imageInfo.getPhotoPath()).into(viewHolder.iv_image);
        viewHolder.checkBox.setChecked(imageInfo.isSelected());
        viewHolder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBoxClickListener != null) {
                    if (viewHolder.checkBox.isChecked()) {
                        mCheckBoxClickListener.unSelected(viewHolder, position);
                    } else {
                        if (PhotoUtil.imageSelected.size() < ConstantsUtil.IMAGE_SELECTED_MAX) {
                            mCheckBoxClickListener.selected(viewHolder, position);
                        }else{
                            Toast.makeText(context, context.getResources().getString(R.string.pp_image_selected_max), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_image;
        private CheckBox checkBox;
        private FrameLayout fl_layout;

        public ViewHolder(View view) {
            super(view);
            iv_image = view.findViewById(R.id.iv_image);
            checkBox = view.findViewById(R.id.checkbox);
            fl_layout = view.findViewById(R.id.fl_layout);
        }
    }

    public void setCheckBoxClickListener(CheckBoxClickListener checkBoxClickListener) {
        this.mCheckBoxClickListener = checkBoxClickListener;
    }
}
