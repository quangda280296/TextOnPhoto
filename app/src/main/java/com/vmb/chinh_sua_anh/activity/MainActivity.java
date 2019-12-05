package com.vmb.chinh_sua_anh.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.FireAnaUtil;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.PermissionUtil;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.fragment.Main_Bottom_6_Button_Fragment;
import com.vmb.chinh_sua_anh.fragment.Main_Bottom_Modify_Text_Fragment;
import com.vmb.chinh_sua_anh.fragment.Main_Detail_Font_Text_Fragment;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;
import com.vmb.chinh_sua_anh.utils.FragmentUtil;
import com.vmb.chinh_sua_anh.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import photoeditor.OnSaveBitmap;
import photoeditor.PhotoEditor;
import photoeditor.PhotoEditorView;

public class MainActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener, OnSaveBitmap {

    // Photo Editor View
    private PhotoEditorView mPhotoEditorView;
    //private PhotoEditorView photoEditorView_extra;

    // Action bar
    private View img_back;
    private LinearLayout layout_save;

    private AlertDialog alertDialog;

    @Override
    protected int getResLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        //photoEditorView_extra = findViewById(R.id.photoEditorView_extra);

        img_back = findViewById(R.id.img_back);
        layout_save = findViewById(R.id.layout_save);
    }

    @Override
    protected void initData() {
        img_back.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        layout_save.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        FragmentManager manager = getFragmentManager();
        manager.addOnBackStackChangedListener(this);
        FragmentTransaction transaction = manager.beginTransaction();

        FragmentUtil.getInstance().setManager(manager);
        FragmentUtil.getInstance().setTransaction(transaction);

        transaction.add(R.id.bottom, new Main_Bottom_6_Button_Fragment(), Config.FragmentTag.MAIN_BOTTOM_5_BUTTON_FRAGMENT);
        transaction.commit();

        /*PhotoHandler.getInstance().setPhotoEditor_extra(new PhotoEditor.Builder(getApplicationContext(), photoEditorView_extra)
                .setPinchTextScalable(true)
                .setActive(false)
                .build());*/
        initImage();

        PermissionUtil.requestPermission(MainActivity.this, Config.RequestCode.
                REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void initImage() {
        PhotoHandler.getInstance().setPhotoEditor(new PhotoEditor.Builder(MainActivity.this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .setActive(true)
                .build());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AdsHandler.getInstance().displayInterstitial(MainActivity.this, false);
                AdsHandler.getInstance().initBanner(MainActivity.this);
            }
        }, 1000);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            try {
                mPhotoEditorView.getSource().setImageBitmap(PhotoHandler.getInstance().getSourceBitmap());
            } catch (Exception e) {
                showLoadPhotoFailedDialog();
            }
            return;
        }

        final android.app.AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(MainActivity.this)
                .setMessage(R.string.loading)
                .build();

        String path = bundle.getString(Config.KeyIntentData.KEY_MAIN_ACT_PHOTO_PATH);
        if (!TextUtils.isEmpty(path)) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(path)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Bitmap bitmap = Utils.scaleDownImage(resource);

                                mPhotoEditorView.getSource().setImageBitmap(bitmap);
                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                PhotoHandler.getInstance().setSourceBitmap(bitmap);
                                PhotoHandler.getInstance().setFilterBitmap(bitmap);

                                PhotoHandler.getInstance().setFullScaleBitmap(resource);
                            }
                        });
            } catch (Exception e) {
                dialog.dismiss();
                showLoadPhotoFailedDialog();
            }

        } else {
            String uri = bundle.getString(Config.KeyIntentData.KEY_MAIN_ACT_URI_PATH);
            if (!TextUtils.isEmpty(uri)) {
                try {
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(Uri.parse(uri))
                            .into(new SimpleTarget<Bitmap>() {

                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    Bitmap bitmap = Utils.scaleDownImage(resource);
                                    mPhotoEditorView.getSource().setImageBitmap(bitmap);
                                    try {
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    PhotoHandler.getInstance().setSourceBitmap(bitmap);
                                    PhotoHandler.getInstance().setFilterBitmap(bitmap);

                                    PhotoHandler.getInstance().setFullScaleBitmap(resource);
                                }
                            });
                } catch (Exception e) {
                    dialog.dismiss();
                    showLoadPhotoFailedDialog();
                }
            }
        }
    }

    protected void showLoadPhotoFailedDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.load_photo_failed)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, ChooseImageActivity.class));
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.RequestCode.REQUEST_CODE_FILTER:
                if (resultCode == RESULT_OK) {
                    mPhotoEditorView.getSource().setImageBitmap(PhotoHandler.getInstance().getFilterBitmap());
                    //photoEditorView_extra.getSource().setImageBitmap(PhotoHandler.getInstance().getFullScaleBitmap());
                    //applyFilter();
                }
                break;

            case Config.RequestCode.REQUEST_CODE_CROP:
                if (resultCode == RESULT_OK) {
                    mPhotoEditorView.getSource().setImageBitmap(PhotoHandler.getInstance().getFilterBitmap());
                }
                break;

            case Config.RequestCode.REQUEST_CODE_INSERT_STICKER:
                if (resultCode == RESULT_OK) {
                    PhotoHandler.getInstance().getPhotoEditor().addSticker(PhotoHandler.getInstance().getSticker());
                }
                break;

            case Config.RequestCode.REQUEST_CODE_INSERT_TEXT:
                if (resultCode == RESULT_OK) {
                    String inputText = data.getStringExtra(Config.KeyIntentData.KEY_TEXT_INPUT_ACT);
                    int lineCount = data.getIntExtra(Config.KeyIntentData.KEY_LINE_INPUT_ACT, 0);
                    if (lineCount > 0)
                        PhotoHandler.getInstance().getPhotoEditor().addText(getApplicationContext(), inputText, lineCount);
                }
                break;

            case Config.RequestCode.REQUEST_CODE_EDIT_TEXT:
                if (resultCode == RESULT_OK) {
                    String inputText = data.getStringExtra(Config.KeyIntentData.KEY_TEXT_INPUT_ACT);
                    int lineCount = data.getIntExtra(Config.KeyIntentData.KEY_LINE_INPUT_ACT, 0);
                    PhotoHandler.getInstance().getPhotoEditor().editText(inputText, lineCount);
                    while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                        FragmentUtil.getInstance().getManager().popBackStackImmediate();
                }
                break;

            case Config.RequestCode.REQUEST_CODE_CHANGE_PHOTO:
                PhotoHandler.getInstance().setChangePhotoMode(false);
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        mPhotoEditorView.getSource().setImageBitmap(PhotoHandler.getInstance().getSourceBitmap());
                        return;
                    }

                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        mPhotoEditorView.getSource().setImageBitmap(PhotoHandler.getInstance().getSourceBitmap());
                        return;
                    }

                    String path = bundle.getString(Config.KeyIntentData.KEY_MAIN_ACT_PHOTO_PATH);
                    if (!TextUtils.isEmpty(path)) {
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(path)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Bitmap bitmap = Utils.scaleDownImage(resource);
                                        mPhotoEditorView.getSource().setImageBitmap(bitmap);

                                        PhotoHandler.getInstance().setSourceBitmap(bitmap);
                                        PhotoHandler.getInstance().setFilterBitmap(bitmap);

                                        PhotoHandler.getInstance().setFullScaleBitmap(resource);
                                    }
                                });
                    } else {
                        String uri = bundle.getString(Config.KeyIntentData.KEY_MAIN_ACT_URI_PATH);
                        if (!TextUtils.isEmpty(uri)) {
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(Uri.parse(uri))
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource,
                                                                    @Nullable Transition<? super Bitmap> transition) {
                                            Bitmap bitmap = Utils.scaleDownImage(resource);
                                            mPhotoEditorView.getSource().setImageBitmap(bitmap);

                                            PhotoHandler.getInstance().setSourceBitmap(bitmap);
                                            PhotoHandler.getInstance().setFilterBitmap(bitmap);

                                            PhotoHandler.getInstance().setFullScaleBitmap(resource);
                                        }
                                    });
                        }
                    }
                }

            case Config.RequestCode.REQUEST_CODE_FONT:
                final Main_Detail_Font_Text_Fragment fragment = (Main_Detail_Font_Text_Fragment)
                        FragmentUtil.getInstance().findFragmentByTAG(Config.FragmentTag.MAIN_DETAIL_FONT_TEXT_FRAGMENT);
                if (fragment == null)
                    return;
                if (resultCode == RESULT_OK) {
                    fragment.updateFontList();
                } else
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (fragment == null)
                                return;
                            fragment.updateFontList();
                        }
                    }, 4000);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                showConfirmDialog();
                break;

            case R.id.layout_save:
                showSaveDialog();
                break;

            case R.id.quick_save:
                saveImage(PhotoHandler.getInstance().getPhotoEditor(), true);
                break;

            case R.id.hd_save:
                //photoEditorView_extra.getSource().setImageBitmap(PhotoHandler.getInstance().getFullScaleBitmap());
                saveImage(PhotoHandler.getInstance().getPhotoEditor(), false);
                break;

            default:
                break;
        }
    }

    public void showSaveDialog() {
        if (isFinishing())
            return;

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View alertLayout = inflater.inflate(R.layout.dialog_save, null);

        alertLayout.findViewById(R.id.quick_save).setOnClickListener(this);
        alertLayout.findViewById(R.id.hd_save).setOnClickListener(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setView(alertLayout);
        alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (FragmentUtil.getInstance().countBackstack() == 0)
            showConfirmDialog();
        else {
            Utils.turnOffBrushMode();
            FragmentManager manager = FragmentUtil.getInstance().getManager();
            if (manager != null)
                manager.popBackStackImmediate();
        }
    }

    @Override
    public void onBackStackChanged() {
        int countBackStack = FragmentUtil.getInstance().countBackstack();
        if (countBackStack == 0) {
            Main_Bottom_6_Button_Fragment fragment = (Main_Bottom_6_Button_Fragment) FragmentUtil.getInstance().getManager()
                    .findFragmentByTag(Config.FragmentTag.MAIN_BOTTOM_5_BUTTON_FRAGMENT);
            if (fragment != null) {
                fragment.hideArrow();
                fragment.setBackgroundWhite();
            }
            PhotoHandler.getInstance().getPhotoEditor().clearHelperBox();

        } else if (countBackStack == 1) {
            Main_Bottom_Modify_Text_Fragment fragment = (Main_Bottom_Modify_Text_Fragment) FragmentUtil.getInstance().getManager()
                    .findFragmentByTag(Config.FragmentTag.MAIN_BOTTOM_MODIFY_TEXT_FRAGMENT);
            if (fragment != null)
                fragment.setBackgroundTransparent();
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

    public void saveImage(final PhotoEditor mPhotoEditor, boolean quick_save) {
        if (mPhotoEditor == null)
            return;

        if(quick_save)
            FireAnaUtil.logEvent(getApplicationContext(), Config.Event.SAVED_IMG, "quick_save");
        else
            FireAnaUtil.logEvent(getApplicationContext(), Config.Event.SAVED_IMG, "hd_save");

        alertDialog.dismiss();

        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        Calendar myCalendar = Calendar.getInstance();
        String date = df.format(myCalendar.getTime());
        final String fileName = "Picture" + date + ".png";

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage(getString(R.string.saving));
        dialog.setCancelable(false);
        dialog.show();

        /*final android.app.AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(MainActivity.this)
                .setMessage(R.string.saving)
                .build();
        dialog.setCancelable(false);
        dialog.show();*/

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mPhotoEditor.saveAsFile(fileName, new PhotoEditor.OnSaveListener() {
            @Override
            public void onSuccess(@NonNull String imagePath) {
                Utils.addPhotoToGallery(getApplicationContext(), imagePath);
                final Intent intent = new Intent(MainActivity.this, AfterSavingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Config.KeyIntentData.KEY_AFTER_SAVING_ACT_IMAGE_PATH, imagePath);
                intent.putExtras(bundle);

                if (!isFinishing()) {
                    dialog.dismiss();
                }

                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                if (!isFinishing())
                    dialog.dismiss();
                ToastUtil.shortToast(getApplicationContext(), getString(R.string.save_failed));
            }
        });
    }

    public void showConfirmDialog() {
        Main_Bottom_6_Button_Fragment fragment = (Main_Bottom_6_Button_Fragment)
                FragmentUtil.getInstance().findFragmentByTAG(Config.FragmentTag.MAIN_BOTTOM_5_BUTTON_FRAGMENT);
        if (fragment != null && !fragment.isModifyAnything()) {
            //startActivity(new Intent(MainActivity.this, ChooseImageActivity.class));
            finish();
            return;
        }

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.confirm)
                .setMessage(R.string.confirm_save_before_close)
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.dont_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //startActivity(new Intent(MainActivity.this, ChooseImageActivity.class));
                        finish();
                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        showSaveDialog();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        PhotoHandler.getInstance().destroy();
        TextOnPhotoHandler.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    public void onBitmapReady(Bitmap saveBitmap) {
        PhotoHandler.getInstance().setFullScaleBitmap(saveBitmap);
    }

    /*public void applyFilter() {
        PhotoFilter photoFilter = PhotoHandler.getInstance().getPhotoFilter();
        CustomEffect customEffect;

        try {
            switch (photoFilter) {
                case AUTO_FIX:
                    customEffect = new CustomEffect.Builder(EFFECT_AUTOFIX)
                            .setParameter("scale", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case GAMMA:
                    customEffect = new CustomEffect.Builder(EFFECT_BLACKWHITE)
                            .setParameter("black", PhotoHandler.getInstance().getParam_1())
                            .setParameter("white", PhotoHandler.getInstance().getParam_2())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case BRIGHTNESS:
                    customEffect = new CustomEffect.Builder(EFFECT_BRIGHTNESS)
                            .setParameter("brightness", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case CONTRAST:
                    customEffect = new CustomEffect.Builder(EFFECT_CONTRAST)
                            .setParameter("contrast", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case CROSS_PROCESS:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(CROSS_PROCESS);
                    break;
                case DOCUMENTARY:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(DOCUMENTARY);
                    break;
                case DUE_TONE:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(DUE_TONE);
                    break;
                case FILL_LIGHT:
                    customEffect = new CustomEffect.Builder(EFFECT_FILLLIGHT)
                            .setParameter("strength", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case FISH_EYE:
                    customEffect = new CustomEffect.Builder(EFFECT_FISHEYE)
                            .setParameter("scale", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case GRAIN:
                    customEffect = new CustomEffect.Builder(EFFECT_GRAIN)
                            .setParameter("strength", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case GRAY_SCALE:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(GRAY_SCALE);
                    break;
                case LOMISH:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(LOMISH);
                    break;
                case NEGATIVE:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(NEGATIVE);
                    break;
                case NONE:
                    break;
                case POSTERIZE:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(POSTERIZE);
                    break;
                case SATURATE:
                    customEffect = new CustomEffect.Builder(EFFECT_SATURATE)
                            .setParameter("scale", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case SEPIA:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(SEPIA);
                    break;
                case SHARPEN:
                    customEffect = new CustomEffect.Builder(EFFECT_SHARPEN)
                            .setParameter("scale", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case TEMPERATURE:
                    customEffect = new CustomEffect.Builder(EFFECT_TEMPERATURE)
                            .setParameter("scale", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                case TINT:
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(TINT);
                    break;
                case VIGNETTE:
                    customEffect = new CustomEffect.Builder(EFFECT_VIGNETTE)
                            .setParameter("scale", PhotoHandler.getInstance().getParam_1())
                            .build();
                    PhotoHandler.getInstance().getPhotoEditor_extra().setFilterEffect(customEffect);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("onFilterSelected", "Exception = " + e.getMessage());
        }

        PhotoHandler.getInstance().getPhotoEditor_extra().saveAsBitmap(this);
    }*/
}