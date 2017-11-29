package com.barryyang.photopicker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.barryyang.photopicker.adapter.PhotoAdapter;
import com.barryyang.photopicker.bean.ImageInfo;
import com.barryyang.photopicker.listener.CheckBoxClickListener;
import com.barryyang.photopicker.listener.LoadPhotoListener;
import com.barryyang.photopicker.utils.ConstantsUtil;
import com.barryyang.photopicker.utils.PhotoTaskUtil;
import com.barryyang.photopicker.utils.PhotoUtil;

import java.util.ArrayList;


public class PhotoActivity extends AppCompatActivity implements View.OnClickListener, LoadPhotoListener, CheckBoxClickListener {

    private static final String TAG = "PhotoActivity";

    private RecyclerView mRvList;
    private TextView mTitle;
    private TextView mPreview;
    private TextView mSubmit;
    private TextView mBack;

    private PhotoUtil photoUtil;
    private ArrayList<ImageInfo> imageList = new ArrayList<>();
    private PhotoAdapter photoAdapter;

    private PhotoTaskUtil photoTask;

    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initView();
        initOnClick();
        initData();
    }

    private void initOnClick() {
        mBack.setOnClickListener(this);
        mPreview.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }

    private void initData() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsUtil.REFRESH_PHOTO);
        myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, intentFilter);
        checkPermission();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConstantsUtil.REFRESH_PHOTO)){
                updatePhoto(intent);
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ConstantsUtil.STORAGE_PERMISSION);
            } else {
                getPhotoList();
            }
        } else {
            getPhotoList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == ConstantsUtil.STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhotoList();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.pp_photo_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPhotoList() {
        photoUtil = new PhotoUtil(this);
        photoTask = new PhotoTaskUtil(this);
        photoTask.setLoadPhotoListener(this);
        photoTask.execute();
    }

    @Override
    public void loadPhotoSuccess() {
        if (imageList != null) {
            photoAdapter = new PhotoAdapter(PhotoActivity.this, imageList);
            photoAdapter.setCheckBoxClickListener(this);
            mRvList.setAdapter(photoAdapter);
        }
    }

    @Override
    public void loadingPhoto() {
        imageList = photoUtil.getImagesBucketList();
    }

    private void initView() {
        mRvList = findViewById(R.id.rv_list);
        mRvList.setLayoutManager(new GridLayoutManager(this, 3));
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText(getResources().getString(R.string.pp_photo_title));
        mBack = findViewById(R.id.tv_back);
        mSubmit = findViewById(R.id.tv_submit);
        mPreview = findViewById(R.id.tv_pre);
        PhotoUtil.imageSelected.clear();
        mSubmit.setText(getResources().getString(R.string.pp_submit) + "(" + PhotoUtil.imageSelected.size() + "/" + ConstantsUtil.IMAGE_SELECTED_MAX + ")");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_pre:
                if (PhotoUtil.imageSelected.size() > 0) {
                    startActivityForResult(new Intent(this, PreviewActivity.class), ConstantsUtil.PREVIEW_PHOTO_REQUEST);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.pp_notice), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_submit:
                if (PhotoUtil.imageSelected.size() > 0) {
                    sendBroadcast(new Intent(ConstantsUtil.RECEIVER_SUBMIT));
                    finish();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.pp_notice), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (photoTask != null) {
            photoTask.cancel(true);
        }
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }
    }

    @Override
    public void selected(PhotoAdapter.ViewHolder viewHolder, int position) {
        ImageInfo imageInfo = imageList.get(position);
        imageInfo.setSelected(true);
        photoAdapter.onBindViewHolder(viewHolder, position);
        PhotoUtil.imageSelected.add(imageInfo);
        mSubmit.setText(getResources().getString(R.string.pp_submit) + "(" + PhotoUtil.imageSelected.size() + "/" + ConstantsUtil.IMAGE_SELECTED_MAX + ")");
    }

    @Override
    public void unSelected(PhotoAdapter.ViewHolder viewHolder, int position) {
        ImageInfo imageInfo = imageList.get(position);
        imageInfo.setSelected(false);
        photoAdapter.onBindViewHolder(viewHolder, position);
        PhotoUtil.imageSelected.remove(imageInfo);
        mSubmit.setText(getResources().getString(R.string.pp_submit) + "(" + PhotoUtil.imageSelected.size() + "/" + ConstantsUtil.IMAGE_SELECTED_MAX + ")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantsUtil.PREVIEW_PHOTO_REQUEST && resultCode == ConstantsUtil.PREVIEW_PHOTO_RESPONSE) {
            finish();
        } else if (requestCode == ConstantsUtil.PREVIEW_PHOTO_REQUEST && resultCode == ConstantsUtil.PREVIEW_PHOTO_REFRESH) {
            updatePhoto(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updatePhoto(Intent data) {
        String photoId = data.getExtras().getString("photoId");
        Log.d(TAG, "onActivityResult: " + photoId);
        for (int i = 0; i < imageList.size(); i++) {
            ImageInfo imageInfo = imageList.get(i);
            String imageId = imageInfo.getPhotoId();
            if (photoId.equals(imageId)) {
                imageInfo.setSelected(false);
            }
        }
        photoAdapter.notifyDataSetChanged();
    }
}
