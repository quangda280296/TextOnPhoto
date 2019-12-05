package com.vmb.chinh_sua_anh.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.Interface.IFilterListener;
import com.vmb.chinh_sua_anh.adapter.FilterAdapter;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;

import photoeditor.CustomEffect;
import photoeditor.OnSaveBitmap;
import photoeditor.PhotoEditor;
import photoeditor.PhotoEditorView;
import photoeditor.PhotoFilter;

import static android.media.effect.EffectFactory.EFFECT_AUTOFIX;
import static android.media.effect.EffectFactory.EFFECT_BLACKWHITE;
import static android.media.effect.EffectFactory.EFFECT_BRIGHTNESS;
import static android.media.effect.EffectFactory.EFFECT_CONTRAST;
import static android.media.effect.EffectFactory.EFFECT_FILLLIGHT;
import static android.media.effect.EffectFactory.EFFECT_FISHEYE;
import static android.media.effect.EffectFactory.EFFECT_GRAIN;
import static android.media.effect.EffectFactory.EFFECT_SATURATE;
import static android.media.effect.EffectFactory.EFFECT_SHARPEN;
import static android.media.effect.EffectFactory.EFFECT_TEMPERATURE;
import static android.media.effect.EffectFactory.EFFECT_VIGNETTE;

public class FilterActivity extends BaseActivity implements IFilterListener, SeekBar.OnSeekBarChangeListener, OnSaveBitmap {

    private PhotoEditorView mPhotoEditorView;
    private PhotoEditor mPhotoEditor;

    private FrameLayout btn_apply;
    private FrameLayout btn_cancel;

    // Layout filter
    private View adjust;
    private AppCompatSeekBar seekbar_param;
    private TextView lbl_param;

    @Override
    protected int getResLayout() {
        return R.layout.activity_filter;
    }

    @Override
    protected void initView() {
        mPhotoEditorView = findViewById(R.id.photoEditorView);

        btn_apply = findViewById(R.id.btn_apply);
        btn_cancel = findViewById(R.id.btn_cancel);

        adjust = findViewById(R.id.adjust);
        seekbar_param = findViewById(R.id.seekbar_param);
        lbl_param = findViewById(R.id.lbl_param);
    }

    @Override
    protected void initData() {
        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setActive(false)
                .build();
        /*if (PhotoHandler.getInstance().isNeedOverrideFilter())
            mPhotoEditorView.override();*/
        mPhotoEditorView.getSource().setImageBitmap(PhotoHandler.getInstance().getSourceBitmap());

        initImageFilter();

        seekbar_param.setOnSeekBarChangeListener(this);
        btn_apply.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        btn_cancel.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        AdsHandler.getInstance().initBanner(FilterActivity.this);
    }

    public void initImageFilter() {
        RecyclerView mRvFilters = findViewById(R.id.rvFilterView);

        LinearLayoutManager llmFilters = new LinearLayoutManager
                (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);

        FilterAdapter mFilterViewAdapter = new FilterAdapter(this, getApplicationContext());
        mRvFilters.setAdapter(mFilterViewAdapter);
    }

    public void setMode0() {
        adjust.setVisibility(View.INVISIBLE);
    }

    public void setMode1() {
        adjust.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        PhotoHandler.getInstance().setPhotoFilter(photoFilter);
        int progress = 0;
        try {
            switch (photoFilter) {
                case AUTO_FIX:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_AUTOFIX)
                            .setParameter("scale", Config.BitmapFilter.EFFECT_AUTOFIX)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_AUTOFIX * 100.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_AUTOFIX);
                    break;

                case BRIGHTNESS:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_BRIGHTNESS)
                            .setParameter("brightness", Config.BitmapFilter.EFFECT_BRIGHTNESS)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_BRIGHTNESS * 50.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_BRIGHTNESS);
                    break;

                case CONTRAST:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_CONTRAST)
                            .setParameter("contrast", Config.BitmapFilter.EFFECT_CONTRAST)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_CONTRAST * 50.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_CONTRAST);
                    break;

                case FILL_LIGHT:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_FILLLIGHT)
                            .setParameter("strength", Config.BitmapFilter.EFFECT_FILLLIGHT)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_FILLLIGHT * 100.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_FILLLIGHT);
                    break;

                case FISH_EYE:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_FISHEYE)
                            .setParameter("scale", Config.BitmapFilter.EFFECT_FISHEYE)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_FISHEYE * 100.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_FISHEYE);
                    break;

                case GRAIN:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_GRAIN)
                            .setParameter("strength", Config.BitmapFilter.EFFECT_GRAIN)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_GRAIN * 100.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_GRAIN);
                    break;

                case SATURATE:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_SATURATE)
                            .setParameter("scale", Config.BitmapFilter.EFFECT_SATURATE)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) ((Config.BitmapFilter.EFFECT_SATURATE + 1.0f) * 50.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_SATURATE);
                    break;

                case TEMPERATURE:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_TEMPERATURE)
                            .setParameter("scale", Config.BitmapFilter.EFFECT_TEMPERATURE)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_TEMPERATURE * 100.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_TEMPERATURE);
                    break;

                case VIGNETTE:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_VIGNETTE)
                            .setParameter("scale", Config.BitmapFilter.EFFECT_VIGNETTE)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_VIGNETTE * 100.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_VIGNETTE);
                    break;

                case SHARPEN:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_SHARPEN)
                            .setParameter("scale", Config.BitmapFilter.EFFECT_SHARPEN)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);
                    progress = (int) (Config.BitmapFilter.EFFECT_SHARPEN * 100.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");
                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_SHARPEN);
                    break;

                case GAMMA:
                    setMode1();
                    customEffect = new CustomEffect.Builder(EFFECT_BLACKWHITE)
                            .setParameter("black", Config.BitmapFilter.EFFECT_BLACKWHITE.Black)
                            .setParameter("white", Config.BitmapFilter.EFFECT_BLACKWHITE.White)
                            .build();
                    mPhotoEditor.setFilterEffect(customEffect);

                    progress = (int) (Config.BitmapFilter.EFFECT_BLACKWHITE.White * 100.0f);
                    seekbar_param.setProgress(progress);
                    lbl_param.setText(progress + "%");

                    PhotoHandler.getInstance().setParam_1(Config.BitmapFilter.EFFECT_BLACKWHITE.Black);
                    PhotoHandler.getInstance().setParam_2(Config.BitmapFilter.EFFECT_BLACKWHITE.White);
                    break;

                default:
                    setMode0();
                    mPhotoEditor.setFilterEffect(photoFilter);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("onFilterSelected", "Exception = " + e.getMessage());
        }
    }

    CustomEffect customEffect;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbar_param:
                lbl_param.setText(progress + "%");
                try {
                    switch (PhotoHandler.getInstance().getPhotoFilter()) {
                        case AUTO_FIX:
                            PhotoHandler.getInstance().setParam_1((float) progress / 100.0f);
                            customEffect.setParametersMap("scale", (float) progress / 100.0f);
                            break;

                        case BRIGHTNESS:
                            PhotoHandler.getInstance().setParam_1((float) progress / 50.0f);
                            customEffect.setParametersMap("brightness", (float) progress / 50.0f);
                            break;

                        case CONTRAST:
                            PhotoHandler.getInstance().setParam_1((float) progress / 50.0f);
                            customEffect.setParametersMap("contrast", (float) progress / 50.0f);
                            break;

                        case FILL_LIGHT:
                            PhotoHandler.getInstance().setParam_1((float) progress / 100.0f);
                            customEffect.setParametersMap("strength", (float) progress / 100.0f);
                            break;

                        case FISH_EYE:
                            PhotoHandler.getInstance().setParam_1((float) progress / 100.0f);
                            customEffect.setParametersMap("scale", (float) progress / 100.0f);
                            break;

                        case GRAIN:
                            PhotoHandler.getInstance().setParam_1((float) progress / 100.0f);
                            customEffect.setParametersMap("strength", (float) progress / 100.0f);
                            break;

                        case SATURATE:
                            PhotoHandler.getInstance().setParam_1((float) progress / 50.0f - 1.0f);
                            customEffect.setParametersMap("scale", (float) progress / 50.0f - 1.0f);
                            break;

                        case TEMPERATURE:
                            PhotoHandler.getInstance().setParam_1((float) progress / 100.0f);
                            customEffect.setParametersMap("scale", (float) progress / 100.0f);
                            break;

                        case VIGNETTE:
                            PhotoHandler.getInstance().setParam_1((float) progress / 100.0f);
                            customEffect.setParametersMap("scale", (float) progress / 100.0f);
                            break;

                        case SHARPEN:
                            PhotoHandler.getInstance().setParam_1((float) progress / 100.0f);
                            customEffect.setParametersMap("scale", (float) progress / 100.0f);
                            break;

                        case GAMMA:
                            PhotoHandler.getInstance().setParam_2((float) progress / 100.0f);
                            customEffect.setParametersMap("black", PhotoHandler.getInstance().getParam_1() / 100.0f);
                            customEffect.setParametersMap("white", (float) progress / 100.0f);
                            break;

                        default:
                            break;
                    }
                    break;

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("onFilterSelected", "Exception = " + e.getMessage());
                }

            default:
                break;
        }

        mPhotoEditor.setFilterEffect(customEffect);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBitmapReady(Bitmap saveBitmap) {
        PhotoHandler.getInstance().setFilterBitmap(saveBitmap);
        PhotoHandler.getInstance().setFullScaleBitmap(saveBitmap);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFailure(Exception e) {

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
            case R.id.btn_apply:
                ToastUtil.shortToast(getApplicationContext(), getString(R.string.applying_filter));
                mPhotoEditor.saveAsBitmap(FilterActivity.this);
                break;

            case R.id.btn_cancel:
                finish();
                break;

            default:
                break;
        }
    }
}