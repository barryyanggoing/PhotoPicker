package com.barryyang.photopicker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.barryyang.photopicker.adapter.PhotoAdapter;
import com.barryyang.photopicker.bean.ImageInfo;
import com.barryyang.photopicker.listener.LoadPhotoListener;
import com.barryyang.photopicker.utils.ConstantsUtil;
import com.barryyang.photopicker.utils.PhotoTaskUtil;
import com.barryyang.photopicker.utils.PhotoUtil;

import java.util.ArrayList;


public class PhotoActivity extends AppCompatActivity implements View.OnClickListener, LoadPhotoListener {

    private RecyclerView mRvList;
    private TextView mTitle;
    private TextView mPreview;
    private TextView mSubmit;
    private TextView mBack;

    private PhotoUtil photoUtil;
    private ArrayList<ImageInfo> imageList = new ArrayList<>();
    private PhotoAdapter photoAdapter;

    private PhotoTaskUtil photoTask;

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
    }

    private void initData() {
        checkPermission();
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
            mRvList.setAdapter(photoAdapter);
        }
    }

    @Override
    public void loadingPhoto() {
        imageList = photoUtil.getImagesBucketList();
    }

    private void initView() {
        mRvList = findViewById(R.id.rv_list);
        mRvList.setLayoutManager(new GridLayoutManager(this,3));
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText(getResources().getString(R.string.pp_album_title));
        mBack = findViewById(R.id.tv_back);
        mSubmit = findViewById(R.id.tv_submit);
        mPreview = findViewById(R.id.tv_pre);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
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
    }
}
