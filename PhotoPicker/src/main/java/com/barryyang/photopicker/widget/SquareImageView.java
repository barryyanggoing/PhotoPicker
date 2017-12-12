package com.barryyang.photopicker.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.barryyang.photopicker.utils.DensityUtil;
import com.barryyang.photopicker.utils.ScreenUtil;


public class SquareImageView extends AppCompatImageView {

    private Context mContext;
    private int mWidth;

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        int screenWidth = ScreenUtil.getScreenWH(mContext)[0];
        mWidth = (screenWidth - DensityUtil.dip2px(mContext, 4)) / 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mWidth);
    }

}
