package com.barryyang.photopicker.utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.barryyang.photopicker.listener.LoadPhotoListener;

import java.lang.ref.WeakReference;

/**
 * @author：barryyang on 2017/11/29 10:07
 * @description:
 * @version:
 */
public class PhotoTaskUtil extends AsyncTask {

    private WeakReference<Activity> mWeakReference;
    private Activity mActivity;
    private LoadPhotoListener mLoadPhotoListener;

    public PhotoTaskUtil(Activity activity) {
        this.mWeakReference = new WeakReference<>(activity);
        this.mActivity = activity;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if (mLoadPhotoListener != null) {
            mLoadPhotoListener.loadingPhoto();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        this.mActivity = mWeakReference.get();
        if (mActivity != null) {
            if (mLoadPhotoListener != null) {
                mLoadPhotoListener.loadPhotoSuccess();
            }
        }
    }

    public void setLoadPhotoListener(LoadPhotoListener loadPhotoListener) {
        this.mLoadPhotoListener = loadPhotoListener;
    }
}
