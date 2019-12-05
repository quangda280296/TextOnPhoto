package com.vmb.chinh_sua_anh.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.Interface.BannerLoaderListener;
import com.vmb.ads_in_app.LibrayData;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.ShareRateUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.handler.Data;

import java.io.File;

public class AfterSavingActivity extends BaseActivity {
    private CallbackManager callbackManager;
    private ScrollView root_scroll;

    private View back;
    private View home;
    private ImageView img_photo;
    private ImageView ratingButton;
    private TextView lbl_save_path;

    private View actionBar;

    private View facebook;
    private View messenger;
    private View zalo;
    private View instagram;
    private View twitter;
    private View whatsapp;
    private View wechat;
    private View more;

    private String path;
    private boolean isScrolled = false;

    @Override
    protected int getResLayout() {
        return R.layout.activity_after_saving;
    }

    @Override
    protected void initView() {
        root_scroll = findViewById(R.id.root_scroll);

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        img_photo = findViewById(R.id.img_photo);
        ratingButton = findViewById(R.id.ratingButton);
        lbl_save_path = findViewById(R.id.lbl_save_path);

        actionBar = findViewById(R.id.actionBar);

        facebook = findViewById(R.id.facebook);
        messenger = findViewById(R.id.messenger);
        zalo = findViewById(R.id.zalo);
        instagram = findViewById(R.id.instagram);
        twitter = findViewById(R.id.twitter);
        whatsapp = findViewById(R.id.whatsapp);
        wechat = findViewById(R.id.wechat);
        more = findViewById(R.id.more);
    }

    @Override
    protected void initData() {
        callbackManager = CallbackManager.Factory.create();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        path = bundle.getString(Config.KeyIntentData.KEY_AFTER_SAVING_ACT_IMAGE_PATH);

        img_photo.setImageBitmap(BitmapFactory.decodeFile(path));
        lbl_save_path.setText(getString(R.string.your_photo_save_to) + " " + path + " )");

        ratingButton.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        back.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        home.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        facebook.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        messenger.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        zalo.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        instagram.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        twitter.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        whatsapp.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        wechat.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        more.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        AdsHandler.getInstance().displayInterstitial(AfterSavingActivity.this, false);
        AdsHandler.getInstance().initBanner(AfterSavingActivity.this,
                LibrayData.AdsSize.NATIVE_ADS, (ViewGroup)findViewById(R.id.rectangle), new BannerLoaderListener() {
                    @Override
                    public void onLoaded() {
                        findViewById(R.id.view_gray).setVisibility(View.VISIBLE);
                        actionBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if(!isScrolled) {
                                    isScrolled = true;
                                    root_scroll.smoothScrollBy(0, actionBar.getHeight() * 4);
                                }
                            }
                        });
                    }
                });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Data.getInstance().scanPhoto(getApplicationContext());
            }
        }).start();
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(AfterSavingActivity.this, ChooseImageActivity.class));
                finish();
                break;

            case R.id.ratingButton:
                ShareRateUtil.rateApp(AfterSavingActivity.this);
                break;

            case R.id.home:
                finish();
                break;

            case R.id.facebook:
                ShareRateUtil.shareFacebook(AfterSavingActivity.this, Uri.fromFile(new File(path)));
                break;

            case R.id.messenger:
                ShareRateUtil.shareMessengerFB(AfterSavingActivity.this, Uri.fromFile(new File(path)),
                        getString(R.string.app_name));
                break;

            case R.id.zalo:
                ShareRateUtil.shareZalo(AfterSavingActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.instagram:
                ShareRateUtil.shareInstagram(AfterSavingActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.twitter:
                ShareRateUtil.shareTwitter(AfterSavingActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.whatsapp:
                ShareRateUtil.shareWhatsApp(AfterSavingActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.wechat:
                ShareRateUtil.shareWeChat(AfterSavingActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.more:
                ShareRateUtil.shareMore(AfterSavingActivity.this, path, getString(R.string.app_name));
                break;

            default:
                break;
        }
    }
}