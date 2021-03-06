showCamera 是否显示相机 true 显示  false 不显示

selectMode 单选还是多选 false 单选 true 多选

maxSelect  大多选择的图片，<0 则为9 >9 则为9

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private GridView mGrideView;
    private MyPhotoBroadcastReceiver mPhotoBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGrideView = findViewById(R.id.gridview);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtil.PHOTO_PICKER);
        mPhotoBroadcastReceiver = new MyPhotoBroadcastReceiver();
        registerReceiver(mPhotoBroadcastReceiver, intentFilter);
    }

    /**
     * 单选无相机
     *
     * @param view
     */
    public void singleNoCamera(View view) {
        PhotoPickerSDK.getInstance().init(this, false, false, 1);
        PhotoPickerSDK.getInstance().photoPicker();
    }

    /**
     * 单选有相机
     *
     * @param view
     */
    public void singleCamera(View view) {
        PhotoPickerSDK.getInstance().init(this, false, true, 1);
        PhotoPickerSDK.getInstance().photoPicker();
    }

    /**
     * 多选无相机
     *
     * @param view
     */
    public void multiNoCamera(View view) {
        PhotoPickerSDK.getInstance().init(this, true, false, 9);
        PhotoPickerSDK.getInstance().photoPicker();
    }

    /**
     * 多选有相机
     *
     * @param view
     */
    public void multiCamera(View view) {
        PhotoPickerSDK.getInstance().init(this, true, true, 9);
        PhotoPickerSDK.getInstance().photoPicker();
    }

    private class MyPhotoBroadcastReceiver extends PhotoBroadcastReceiver {

        @Override
        protected void photoSelectSuccess(ArrayList<String> pathList) {
            LogUtils.d(TAG, pathList.toString());
            PhotoSelectAdapter mPhotoSelectAdapter = new PhotoSelectAdapter(MainActivity.this, pathList);
            mGrideView.setAdapter(mPhotoSelectAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhotoBroadcastReceiver != null) {
            unregisterReceiver(mPhotoBroadcastReceiver);
        }
    }
}
