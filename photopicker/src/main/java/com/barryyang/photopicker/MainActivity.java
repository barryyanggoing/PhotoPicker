package com.barryyang.photopicker;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.barryyang.photopicker.bean.ImageInfo;
import com.barryyang.photopicker.broadcast.PhotoBroadCastReceiver;
import com.barryyang.photopicker.utils.ConstantsUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyBoradcastReceiver myBoradcastReceiver;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImage = findViewById(R.id.iv_image);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsUtil.RECEIVER_SUBMIT);
        myBoradcastReceiver = new MyBoradcastReceiver();
        registerReceiver(myBoradcastReceiver, intentFilter);
    }

    class MyBoradcastReceiver extends PhotoBroadCastReceiver {

        @Override
        protected void receiverPhoto(ArrayList<ImageInfo> imageSelected) {
            Glide.with(MainActivity.this).load(imageSelected.get(0).getPhotoPath()).into(mImage);
        }
    }

    public void click(View view) {
        startActivity(new Intent(this, PhotoActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBoradcastReceiver != null) {
            unregisterReceiver(myBoradcastReceiver);
        }
    }
}
