package com.barryyang.photopicker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barryyang.photopicker.adapter.PhotoAdapter;
import com.barryyang.photopicker.bean.Photo;
import com.barryyang.photopicker.bean.PhotoFolder;
import com.barryyang.photopicker.utils.ConstantUtil;
import com.barryyang.photopicker.utils.LogUtils;
import com.barryyang.photopicker.utils.OtherUtils;
import com.barryyang.photopicker.utils.PhotoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author：barryyang on 2017/11/30 10:10
 * @description:
 * @version:
 */
public class PhotoPickerActivity extends Activity implements View.OnClickListener {

    public final static String TAG = "PhotoPickerActivity";

    private boolean mShowCamera;
    private boolean mSelectMode;
    private int mMaxSelect;

    private GridView mGridView;
    private TextView mPhotoNumTV;
    private TextView mPhotoNameTV;
    private ImageView mBack;

    private Map<String, PhotoFolder> mFolderMap;
    private List<Photo> mPhotoLists = new ArrayList<>();

    private PhotoAdapter mPhotoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        initView();
        initData();
    }

    private void initView() {
        mGridView = findViewById(R.id.photo_gridview);
        mPhotoNumTV = findViewById(R.id.photo_num);
        mPhotoNameTV = findViewById(R.id.floder_name);
        mBack = findViewById(R.id.btn_back);
    }

    private void initData() {
        getParams();
        checkSdCard();
        getPhotos();
    }

    /**
     * 获取本地图片
     */
    private void getPhotos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ConstantUtil.REQUEST_PERMISSION);
            } else {
                PhotosTask.execute();
            }
        } else {
            PhotosTask.execute();
        }
    }

    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ConstantUtil.REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PhotosTask.execute();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.app_no_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 异步获取照片信息
     */
    private AsyncTask PhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mFolderMap = PhotoUtils.getPhotos(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };

    /**
     * 获取照片成功
     */
    private void getPhotosSuccess() {
        mPhotoLists.addAll(mFolderMap.get(ConstantUtil.ALL_PHOTO).getPhotoList());
        mPhotoNumTV.setText(OtherUtils.formatResourceString(getApplicationContext(), R.string.app_photo_num, mPhotoLists.size()));
        mPhotoAdapter = new PhotoAdapter(getApplicationContext(), mPhotoLists);
        mPhotoAdapter.setShowCamera(mShowCamera);
        mPhotoAdapter.setSelectMode(mSelectMode);
        mPhotoAdapter.setMaxNum(mMaxSelect);
        mGridView.setAdapter(mPhotoAdapter);

//        Set<String> keys = mFolderMap.keySet();
//        final List<PhotoFolder> folders = new ArrayList<>();
//        for (String key : keys) {
//            if (ConstantUtil.ALL_PHOTO.equals(key)) {
//                PhotoFolder folder = mFolderMap.get(key);
//                folder.setIsSelected(true);
//                folders.add(0, folder);
//            } else {
//                folders.add(mFolderMap.get(key));
//            }
//        }
    }

    /**
     * 检查是否有SD CARD
     */
    private void checkSdCard() {
        if (!OtherUtils.isExternalStorageAvailable()) {
            Toast.makeText(this, getResources().getString(R.string.app_check_sdcard), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 获取传过来的参数
     */
    private void getParams() {
        mShowCamera = getIntent().getBooleanExtra(ConstantUtil.SHOW_CAMERA, false);
        mSelectMode = getIntent().getBooleanExtra(ConstantUtil.SELECT_MODE, false);
        mMaxSelect = getIntent().getIntExtra(ConstantUtil.SELECT_MAX, ConstantUtil.DEFAULT_MAX);
        if (mSelectMode) {
            if (mMaxSelect < 1 || mMaxSelect > ConstantUtil.DEFAULT_MAX) {
                mMaxSelect = ConstantUtil.DEFAULT_MAX;
            }
        } else {
            if (mMaxSelect < 1 || mMaxSelect > ConstantUtil.DEFAULT_MAX) {
                mMaxSelect = 1;
            }
        }
        LogUtils.d(TAG, "getParams: " + mShowCamera + "->" + mSelectMode + "->" + mMaxSelect);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }
}
