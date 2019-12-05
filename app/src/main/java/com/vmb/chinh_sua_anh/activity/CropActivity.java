package com.vmb.chinh_sua_anh.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.quangda280296.photoeditor.R;

public class CropActivity extends BaseActivity {

    private CropImageView cropImageView;

    private View apply;
    private View cancel;

    private View rotate;

    private ImageView img_rect;
    private ImageView img_oval;

    private ImageView r_custom;
    private ImageView r_1_1;
    private ImageView r_2_3;
    private ImageView r_3_2;
    private ImageView r_3_4;
    private ImageView r_3_5;
    private ImageView r_4_3;
    private ImageView r_4_5;
    private ImageView r_5_3;
    private ImageView r_5_4;
    private ImageView r_9_16;
    private ImageView r_10_16;
    private ImageView r_16_9;
    private ImageView r_16_10;

    @Override
    protected int getResLayout() {
        return R.layout.activity_crop;
    }

    @Override
    protected void initView() {
        cropImageView = findViewById(R.id.cropImageView);

        apply = findViewById(R.id.apply);
        cancel = findViewById(R.id.cancel);

        rotate = findViewById(R.id.rotate);

        img_rect = findViewById(R.id.img_rect);
        img_oval = findViewById(R.id.img_oval);

        r_custom = findViewById(R.id.r_custom);
        r_1_1 = findViewById(R.id.r_1_1);
        r_2_3 = findViewById(R.id.r_2_3);
        r_3_2 = findViewById(R.id.r_3_2);
        r_3_4 = findViewById(R.id.r_3_4);
        r_3_5 = findViewById(R.id.r_3_5);
        r_4_3 = findViewById(R.id.r_4_3);
        r_4_5 = findViewById(R.id.r_4_5);
        r_5_3 = findViewById(R.id.r_5_3);
        r_5_4 = findViewById(R.id.r_5_4);
        r_9_16 = findViewById(R.id.r_9_16);
        r_10_16 = findViewById(R.id.r_10_16);
        r_16_9 = findViewById(R.id.r_16_9);
        r_16_10 = findViewById(R.id.r_16_10);
    }

    @Override
    protected void initData() {
        cropImageView.setImageBitmap(PhotoHandler.getInstance().getFilterBitmap());
        cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        cropImageView.setAutoZoomEnabled(true);
        cropImageView.setShowProgressBar(true);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setAspectRatio(10, 7);

        apply.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        cancel.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        rotate.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        img_rect.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        img_oval.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        r_custom.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_1_1.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_2_3.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_3_2.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_3_4.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_3_5.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_4_3.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_4_5.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_5_3.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_5_4.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_9_16.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_10_16.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_16_9.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        r_16_10.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        AdsHandler.getInstance().initBanner(CropActivity.this);
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply:
                Bitmap bitmap = cropImageView.getCroppedImage();
                PhotoHandler.getInstance().setSourceBitmap(bitmap);
                PhotoHandler.getInstance().setFilterBitmap(bitmap);
                PhotoHandler.getInstance().setFullScaleBitmap(bitmap);
                setResult(Activity.RESULT_OK);
                finish();
                break;

            case R.id.cancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;

            case R.id.img_rect:
                cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
                break;

            case R.id.img_oval:
                cropImageView.setCropShape(CropImageView.CropShape.OVAL);
                break;

            case R.id.rotate:
                cropImageView.rotateImage(90);
                break;

            case R.id.r_custom:
                cropImageView.setAspectRatio(10, 7);
                break;

            case R.id.r_1_1:
                cropImageView.setAspectRatio(1, 1);
                break;

            case R.id.r_2_3:
                cropImageView.setAspectRatio(2, 3);
                break;

            case R.id.r_3_2:
                cropImageView.setAspectRatio(3, 2);
                break;

            case R.id.r_3_4:
                cropImageView.setAspectRatio(3, 4);
                break;

            case R.id.r_3_5:
                cropImageView.setAspectRatio(3, 5);
                break;

            case R.id.r_4_3:
                cropImageView.setAspectRatio(4, 3);
                break;

            case R.id.r_4_5:
                cropImageView.setAspectRatio(4, 5);
                break;

            case R.id.r_5_3:
                cropImageView.setAspectRatio(5, 3);
                break;

            case R.id.r_5_4:
                cropImageView.setAspectRatio(5, 4);
                break;

            case R.id.r_9_16:
                cropImageView.setAspectRatio(9, 16);
                break;

            case R.id.r_10_16:
                cropImageView.setAspectRatio(10, 16);
                break;

            case R.id.r_16_9:
                cropImageView.setAspectRatio(16, 9);
                break;

            case R.id.r_16_10:
                cropImageView.setAspectRatio(16, 10);
                break;
        }
    }
}