package com.barryyang.photopicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.barryyang.photopicker.utils.PhotoPickerSDK;


public class MainActivity extends AppCompatActivity {

    private GridView mGrideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGrideView = findViewById(R.id.gridview);
    }

    /**
     * 单选无相机
     * @param view
     */
    public void singleNoCamera(View view) {
        PhotoPickerSDK.getInstance().init(this,false,false,1);
        PhotoPickerSDK.getInstance().photoPicker();
    }

    /**
     * 单选有相机
     * @param view
     */
    public void singleCamera(View view) {
        PhotoPickerSDK.getInstance().init(this,false,true,1);
        PhotoPickerSDK.getInstance().photoPicker();
    }

    /**
     * 多选无相机
     * @param view
     */
    public void multiNoCamera(View view) {
        PhotoPickerSDK.getInstance().init(this,true,false,9);
        PhotoPickerSDK.getInstance().photoPicker();
    }

    /**
     * 多选有相机
     * @param view
     */
    public void multiCamera(View view) {
        PhotoPickerSDK.getInstance().init(this,true,true,9);
        PhotoPickerSDK.getInstance().photoPicker();
    }
}
