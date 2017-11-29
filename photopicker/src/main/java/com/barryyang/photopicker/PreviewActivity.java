package com.barryyang.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.barryyang.photopicker.adapter.PreViewAdapter;
import com.barryyang.photopicker.bean.ImageInfo;
import com.barryyang.photopicker.utils.ConstantsUtil;
import com.barryyang.photopicker.utils.PhotoUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * @author：barryyang on 2017/11/29 15:04
 * @description:
 * @version:
 */
public class PreviewActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TextView mTitle;
    private TextView mDelete;
    private TextView mSubmit;
    private TextView mBack;
    private TextView mIndex;
    private ViewPager mViewPager;

    private PreViewAdapter mPreViewAdapter;

    private int location = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initView();
        initOnClick();
        initData();
    }

    private void initView() {
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText(getResources().getString(R.string.pp_photo_preview));
        mBack = findViewById(R.id.tv_back);
        mSubmit = findViewById(R.id.tv_submit);
        mDelete = findViewById(R.id.tv_delete);
        mViewPager = findViewById(R.id.vp_pager);
        mIndex = findViewById(R.id.tv_index);
        mIndex.setText("1 / " + PhotoUtil.imageSelected.size());
    }

    private void initOnClick() {
        mBack.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        mPreViewAdapter = new PreViewAdapter(this);
        mViewPager.setAdapter(mPreViewAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_submit:
                sendBroadcast(new Intent(ConstantsUtil.RECEIVER_SUBMIT));
                setResult(ConstantsUtil.PREVIEW_PHOTO_RESPONSE);
                finish();
                break;
            case R.id.tv_delete:
                int size = PhotoUtil.imageSelected.size();
                if (size == 1) {
                    PhotoUtil.imageSelected.clear();
                    setResultBack(0);
                    finish();
                } else {
                    setResultBack(1);
                    PhotoUtil.imageSelected.remove(location);
                    mViewPager.removeAllViews();
                    mPreViewAdapter.notifyDataSetChanged();
                    mIndex.setText((location + 1) + " / " + PhotoUtil.imageSelected.size());
                }
                break;
            default:
                break;
        }
    }

    private void setResultBack(int index) {
        ImageInfo imageInfo = PhotoUtil.imageSelected.get(location);
        switch (index) {
            case 0:
                Intent intent = new Intent();
                intent.putExtra("photoId", imageInfo.getPhotoId());
                setResult(ConstantsUtil.PREVIEW_PHOTO_REFRESH,intent);
                break;
            case 1:
                Intent intentCast = new Intent(ConstantsUtil.REFRESH_PHOTO);
                intentCast.putExtra("photoId", imageInfo.getPhotoId());
                sendBroadcast(intentCast);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        location = position;
        mIndex.setText((position + 1) + " / " + PhotoUtil.imageSelected.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
