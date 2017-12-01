package com.barryyang.photopicker;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barryyang.photopicker.adapter.FolderAdapter;
import com.barryyang.photopicker.adapter.PhotoAdapter;
import com.barryyang.photopicker.bean.Photo;
import com.barryyang.photopicker.bean.PhotoFolder;
import com.barryyang.photopicker.listener.OnSelectPhotoListener;
import com.barryyang.photopicker.utils.ConstantUtil;
import com.barryyang.photopicker.utils.FileUtil;
import com.barryyang.photopicker.utils.LogUtils;
import com.barryyang.photopicker.utils.ScreenUtil;
import com.barryyang.photopicker.utils.SdCardUtils;
import com.barryyang.photopicker.utils.PhotoUtils;
import com.barryyang.photopicker.utils.StringUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author：barryyang on 2017/11/30 10:10
 * @description:
 * @version:
 */
public class PhotoPickerActivity extends Activity implements View.OnClickListener, OnSelectPhotoListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    public final static String TAG = "PhotoPickerActivity";

    private boolean mShowCamera;
    private boolean mSelectMode;
    private int mMaxSelect;

    private GridView mGridView;
    private TextView mPhotoTotal;
    private TextView mFolderName;
    private ImageView mBack;
    private Button mCommit;
    private RelativeLayout mBottomTab;
    private ListView mFolderListView;
    private ViewStub mFolderStub;
    private View mViewBg;

    private Map<String, PhotoFolder> mFolderMap;
    private List<Photo> mPhotoLists = new ArrayList<>();
    private ArrayList<String> mSelectList = new ArrayList<>();
    private List<PhotoFolder> mFoldersList;

    private PhotoAdapter mPhotoAdapter;

    private File mFileCamera;

    private MyPhotoTask myPhotoTask;

    /**
     * 文件夹列表是否处于显示状态
     */
    private boolean mFolderViewShow = false;

    /**
     * 文件夹列表是否被初始化，确保只被初始化一次
     */
    private boolean mFolderViewInit = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        initView();
        initOnclick();
        initData();
    }

    private void initOnclick() {
        mBack.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        mGridView.setOnItemClickListener(this);
        mFolderName.setOnClickListener(this);
        mBottomTab.setOnTouchListener(this);
    }

    private void initView() {
        mGridView = findViewById(R.id.gv_list);
        mPhotoTotal = findViewById(R.id.tv_total);
        mFolderName = findViewById(R.id.tv_folder_name);
        mBack = findViewById(R.id.iv_back);
        mCommit = findViewById(R.id.btn_commit);
        mBottomTab = findViewById(R.id.rl_bottom);
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
         myPhotoTask = new MyPhotoTask(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ConstantUtil.REQUEST_PERMISSION);
            } else {
                myPhotoTask.execute();
            }
        } else {
            myPhotoTask.execute();
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
                myPhotoTask.execute();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.app_no_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 异步获取照片信息
     */
    private class MyPhotoTask extends AsyncTask {

        private WeakReference<PhotoPickerActivity> weakReference;

        public MyPhotoTask(PhotoPickerActivity context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            mFolderMap = PhotoUtils.getPhotos(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            PhotoPickerActivity activity = weakReference.get();
            if (activity == null) {
                return;
            }
            getPhotosSuccess();
        }
    }

    /**
     * 获取照片成功
     */
    private void getPhotosSuccess() {
        mPhotoLists.addAll(mFolderMap.get(ConstantUtil.ALL_PHOTO).getPhotoList());
        mPhotoTotal.setText(StringUtil.formatResourceString(getApplicationContext(), R.string.app_photo_num, mPhotoLists.size()));
        mPhotoAdapter = new PhotoAdapter(getApplicationContext(), mPhotoLists);
        mPhotoAdapter.setOnSelectPhotoListener(this);
        mPhotoAdapter.setShowCamera(mShowCamera);
        mPhotoAdapter.setSelectMode(mSelectMode);
        mPhotoAdapter.setMaxNum(mMaxSelect);
        mGridView.setAdapter(mPhotoAdapter);
        Set<String> keys = mFolderMap.keySet();
        mFoldersList = new ArrayList<>();
        for (String key : keys) {
            if (ConstantUtil.ALL_PHOTO.equals(key)) {
                PhotoFolder folder = mFolderMap.get(key);
                folder.setIsSelected(true);
                mFoldersList.add(0, folder);
            } else {
                mFoldersList.add(mFolderMap.get(key));
            }
        }
    }

    /**
     * 检查是否有SD CARD
     */
    private void checkSdCard() {
        if (!SdCardUtils.isExternalStorageAvailable()) {
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
        int i = v.getId();
        if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.btn_commit) {
            photoSelectSuccess(mSelectList);
            finish();
        } else if (i == R.id.tv_folder_name) {
            toggleFolderList(mFoldersList);
        }
    }

    /**
     * 左下角全部图片点击
     *
     * @param folders
     */
    private void toggleFolderList(final List<PhotoFolder> folders) {
        if (!mFolderViewInit) {
            mFolderStub = findViewById(R.id.vs_folder);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_in_from_bottom);
            mFolderStub.inflate().setAnimation(animation);
            mFolderViewShow = true;
            mViewBg = findViewById(R.id.view_folder_bg);
            mFolderListView = findViewById(R.id.lv_floder);
            final FolderAdapter adapter = new FolderAdapter(this, folders);
            mFolderListView.setAdapter(adapter);
            mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (PhotoFolder folder : folders) {
                        folder.setIsSelected(false);
                    }
                    PhotoFolder folder = folders.get(position);
                    folder.setIsSelected(true);
                    adapter.notifyDataSetChanged();
                    mPhotoLists.clear();
                    mPhotoLists.addAll(folder.getPhotoList());
                    if (ConstantUtil.ALL_PHOTO.equals(folder.getName())) {
                        mPhotoAdapter.setShowCamera(mShowCamera);
                    } else {
                        mPhotoAdapter.setShowCamera(false);
                    }
                    mGridView.setAdapter(mPhotoAdapter);
                    mPhotoTotal.setText(StringUtil.formatResourceString(getApplicationContext(), R.string.app_photo_num, mPhotoLists.size()));
                    mFolderName.setText(folder.getName());
                    setFolderVisible(mViewBg);
                }
            });

            mViewBg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mFolderViewShow) {
                        setFolderVisible(mViewBg);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            mFolderViewInit = true;
        } else {
            setFolderVisible(mViewBg);
        }
    }

    /**
     * 全部图片点击的显示和隐藏
     *
     * @param mViewBg
     */
    private void setFolderVisible(View mViewBg) {
        int height = ScreenUtil.getScreenWH(this)[1] - 3 * 50;
        if (mFolderViewShow) {
            mFolderViewShow = false;
            AnimatorSet inAnimatorSet = new AnimatorSet();
            ObjectAnimator alphaInAnimator = ObjectAnimator.ofFloat(mViewBg, "alpha", 0.8f, 0f);
            ObjectAnimator transInAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", 0, height);
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
            inAnimatorSet.setDuration(300);
            inAnimatorSet.setInterpolator(linearInterpolator);
            inAnimatorSet.start();
        } else {
            mFolderViewShow = true;
            AnimatorSet inAnimatorSet = new AnimatorSet();
            ObjectAnimator alphaInAnimator = ObjectAnimator.ofFloat(mViewBg, "alpha", 0f, 0.8f);
            ObjectAnimator transInAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", height, 0);
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
            inAnimatorSet.setDuration(300);
            inAnimatorSet.setInterpolator(linearInterpolator);
            inAnimatorSet.start();
        }
    }

    /**
     * 选中图片时候的回调
     *
     * @param selectList
     */
    @Override
    public void onPhotoSelect(ArrayList<String> selectList) {
        if (mSelectMode) {
            this.mSelectList = selectList;
            setBtnVisible(selectList);
        } else {
            photoSelectSuccess(selectList);
            finish();
        }
    }

    /**
     * 相片选择完成回调
     *
     * @param selectList
     */
    private void photoSelectSuccess(ArrayList<String> selectList) {
        Intent intent = new Intent(ConstantUtil.PHOTO_PICKER);
        intent.putStringArrayListExtra("path", selectList);
        sendBroadcast(intent);
        LogUtils.d(TAG, "onPhotoSelect：" + selectList);
    }

    /**
     * 设置确定按钮的显示隐藏
     *
     * @param selectList
     */
    private void setBtnVisible(ArrayList<String> selectList) {
        if (selectList.size() > 0) {
            mCommit.setVisibility(View.VISIBLE);
        } else {
            mCommit.setVisibility(View.GONE);
        }
    }

    /**
     * 当相机显示并且 position = 0
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mShowCamera && position == 0) {
            toSystenCamera();
        }
    }

    /**
     * 跳转到的照相机
     */
    private void toSystenCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ComponentName name = intent.resolveActivity(getPackageManager());
        if (name != null) {
            //拍照后的输出路径
            mFileCamera = FileUtil.createFile(getApplicationContext());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileCamera));
            startActivityForResult(intent, ConstantUtil.REQUEST_CAMERA);
        } else {
            Toast.makeText(getApplicationContext(), R.string.app_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 拍照后回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantUtil.REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mFileCamera != null) {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFileCamera.getAbsolutePath())));
                    mSelectList.add(mFileCamera.getAbsolutePath());
                    photoSelectSuccess(mSelectList);
                    finish();
                }
            }
        }
    }

    /**
     * 点击底部Tab，事件拦截，不让点到图片
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
