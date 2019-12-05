package com.vmb.chinh_sua_anh.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.PermissionUtil;
import com.vmb.ads_in_app.util.ShareRateUtil;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DetailPhotoActivity extends BaseActivity {
    private String MIME_TYPE_IMAGE = "image/*";

    private View back;
    private ImageView img_photo;
    private View delete;
    private View set_wallpaper;
    private View share;
    private View layout_share;

    private View facebook;
    private View messenger;
    private View zalo;
    private View instagram;
    private View twitter;
    private View whatsapp;
    private View wechat;
    private View more;

    private File file;
    private String path;

    @Override
    protected int getResLayout() {
        return R.layout.activity_watch_photo;
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.back);
        img_photo = findViewById(R.id.img_photo);
        delete = findViewById(R.id.delete);
        set_wallpaper = findViewById(R.id.set_wallpaper);
        share = findViewById(R.id.share);
        layout_share = findViewById(R.id.layout_share);

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
        back.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        delete.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        set_wallpaper.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        share.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        facebook.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        messenger.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        zalo.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        instagram.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        twitter.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        whatsapp.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        wechat.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        more.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (layout_share.getVisibility() == View.VISIBLE) {
                    YoYo.with(Techniques.FadeOutDown)
                            .duration(200)
                            .playOn(findViewById(R.id.layout_share));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout_share.setVisibility(View.GONE);
                        }
                    }, 200);
                }
                return false;
            }
        });

        AdsHandler.getInstance().initBanner(DetailPhotoActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        path = bundle.getString(Config.KeyIntentData.KEY_DETAIL_PHOTO_ACT);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        img_photo.setImageBitmap(bitmap);

        file = new File(path);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.delete:
                if (PermissionUtil.requestPermission(DetailPhotoActivity.this, Config.RequestCode.
                        REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    deletePhoto(file);
                break;

            case R.id.set_wallpaper:
                setWallpaper(file);
                break;

            case R.id.share:
                //shareImage(file);
                /*String pathFolder = Environment.getExternalStorageDirectory() + "/" + Config.Directory.SAVE_DIRECTORY + "/";
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider",
                        new File(pathFolder, file.getName()));
                Share.shareFacebook(DetailPhotoActivity.this, photoUri);*/
                layout_share.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInUp)
                        .duration(200)
                        .playOn(layout_share);
                break;

            case R.id.facebook:
                ShareRateUtil.shareFacebook(DetailPhotoActivity.this, Uri.fromFile(new File(path)));
                break;

            case R.id.messenger:
                ShareRateUtil.shareMessengerFB(DetailPhotoActivity.this, Uri.fromFile(new File(path)),
                        getString(R.string.app_name));
                break;

            case R.id.zalo:
                ShareRateUtil.shareZalo(DetailPhotoActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.instagram:
                ShareRateUtil.shareInstagram(DetailPhotoActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.twitter:
                ShareRateUtil.shareTwitter(DetailPhotoActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.whatsapp:
                ShareRateUtil.shareWhatsApp(DetailPhotoActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.wechat:
                ShareRateUtil.shareWeChat(DetailPhotoActivity.this, path, getString(R.string.app_name));
                break;

            case R.id.more:
                ShareRateUtil.shareMore(DetailPhotoActivity.this, path, getString(R.string.app_name));
                break;

            default:
                break;
        }
    }

    private void setWallpaper(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Intent intent = new Intent("android.intent.action.ATTACH_DATA");
            intent.setDataAndType(Uri.fromFile(file), MIME_TYPE_IMAGE);
            intent.putExtra("png", MIME_TYPE_IMAGE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.wallpaper)), 1);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.longToast(getApplicationContext(), getString(R.string.error_occur));
        }
    }

    private void deletePhoto(final File file) {
        if (file != null) {
            if (file.exists()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setMessage(R.string.confirm_delete);
                builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (file.delete()) {
                            ToastUtil.longToast(getApplicationContext(), getString(R.string.photo_deleted));
                            //startActivity(new Intent(DetailPhotoActivity.this, SavedImageActivity.class));
                            finish();
                        } else
                            ToastUtil.longToast(getApplicationContext(), getString(R.string.delete_fail));
                    }
                });
                builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        }
    }

    private void shareImage(@NotNull File file) {
        String str = "https://play.google.com/store/apps/details?id=" + getPackageName();
        shareImageAndText(file.getAbsolutePath(), getString(R.string.app_name), str);
    }

    public void shareImageAndText(String str, String str2, String str3) {
        //ToastUtil.shortToast(getApplicationContext(), getString(R.string.please_wait));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            File file = new File(str);
            String s = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getName().substring(file.getName().lastIndexOf(".") + 1));
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType(s);
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
            intent.putExtra("android.intent.extra.SUBJECT", str2);
            intent.putExtra("android.intent.extra.TEXT", str3);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.title_share_file)));

        } catch (Exception e) {
            ToastUtil.shortToast(getApplicationContext(), getString(R.string.try_again_later));
            StringBuilder s = new StringBuilder();
            s.append("shareImageAndText error = ");
            s.append(e.toString());
            Log.e("shareImageAndText", s.toString());
        }
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Config.RequestCode.REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    deletePhoto(file);
                else
                    ToastUtil.longToast(getApplicationContext(), getString(R.string.accept_to_delete_image));
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (layout_share.getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.FadeOutDown)
                    .duration(200)
                    .playOn(findViewById(R.id.layout_share));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout_share.setVisibility(View.GONE);
                }
            }, 200);
            return;
        }

        super.onBackPressed();
    }
}