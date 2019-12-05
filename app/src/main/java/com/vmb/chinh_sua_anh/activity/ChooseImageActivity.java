package com.vmb.chinh_sua_anh.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.fragment.ChooseImage.Fragment_Tab_1_Choose_Package;
import com.vmb.chinh_sua_anh.fragment.ChooseImage.Fragment_Tab_2;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.utils.Utils;

import java.io.File;
import java.util.Calendar;

public class ChooseImageActivity extends BaseActivity {

    private View img_back;
    private boolean isFirstChangeTab = false;

    @Override
    protected int getResLayout() {
        return R.layout.activity_choose_image;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.img_back);
    }

    @Override
    protected void initData() {
        img_back.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.my_image, Fragment_Tab_1_Choose_Package.class)
                .add(R.string.image_store, Fragment_Tab_2.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.i("setOnPageChangeListener", "onPageSelected i = " + i);
                Fragment page;

                switch (i) {
                    case 0:
                        page = adapter.getPage(1);
                        if (page instanceof Fragment_Tab_2) {
                            page.onPause();
                        }
                        break;

                    case 1:
                        page = adapter.getPage(1);
                        if (page instanceof Fragment_Tab_2) {
                            page.onResume();
                            if (!isFirstChangeTab) {
                                isFirstChangeTab = true;
                                ((Fragment_Tab_2) page).initTitle();
                            }
                        }
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AdsHandler.getInstance().displayInterstitial(ChooseImageActivity.this, false);
                AdsHandler.getInstance().initBanner(ChooseImageActivity.this);
            }
        }, 1000);
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*case Config.RequestCode.REQUEST_GALLEY_CHOOSER:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap sourceBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        PhotoHandler.getInstance().setFullScaleBitmap(sourceBitmap);
                        Bitmap bitmap = Bitmap.createScaledBitmap(sourceBitmap, getResources().getInteger(R.integer.bitmap_handler_size),
                                getResources().getInteger(R.integer.bitmap_handler_size), false);
                        PhotoHandler.getInstance().setSourceBitmap(bitmap);
                        PhotoHandler.getInstance().setFilterBitmap(bitmap);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;*/

            case Config.RequestCode.REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider",
                            new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), Data.getInstance().getFilename()));
                    //PhotoHandler.getInstance().setNeedOverrideFilter(true);

                    if (PhotoHandler.getInstance().isChangePhotoMode()) {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString(Config.KeyIntentData.KEY_MAIN_ACT_URI_PATH, photoUri.toString());
                        intent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        return;
                    }

                    Intent intent = new Intent(ChooseImageActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Config.KeyIntentData.KEY_MAIN_ACT_URI_PATH, photoUri.toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Config.RequestCode.REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Data.getInstance().setTypeSendTime(2);
                            Data.getInstance().setStart_time_load_img(Calendar.getInstance().getTimeInMillis());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Data.getInstance().scanPhoto(getApplicationContext());
                                }
                            }).start();
                        }
                    }, 500);
                } else {
                    ToastUtil.longToast(getApplicationContext(), getString(R.string.accept_to_get_images));
                }
                break;

            case Config.RequestCode.REQUEST_CODE_CAMERA:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
                    Utils.startCamera(ChooseImageActivity.this);
                else {
                    ToastUtil.longToast(getApplicationContext(), getString(R.string.accept_to_take_camera));
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            finish();
        else
            getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}