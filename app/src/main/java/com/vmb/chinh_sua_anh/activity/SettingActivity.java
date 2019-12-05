package com.vmb.chinh_sua_anh.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vmb.ads_in_app.LibrayData;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.ads_in_app.Interface.IOnSendFeedback;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.ads_in_app.util.ShareRateUtil;
import com.quangda280296.photoeditor.R;

public class SettingActivity extends BaseActivity implements IOnSendFeedback {
    private View img_back;

//    private View btnFeedbackSetting;
    private View btnRateSetting;
    private View btnShareSetting;
    private View btnFollowFacebookSetting;
    private View btnCheckUpdateSetting;

    private TextView txtCurrentVersionSetting;
    private TextView txtShareSetting;
    private TextView pathSavePhoto;

    /*private View layout_feedback;
    private EditText txt_feedback;
    private Button btn_send;*/

    @Override
    protected int getResLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.img_back);

//        btnFeedbackSetting = findViewById(R.id.btnFeedbackSetting);
        btnRateSetting = findViewById(R.id.btnRateSetting);
        btnShareSetting = findViewById(R.id.btnShareSetting);
        btnFollowFacebookSetting = findViewById(R.id.btnFollowFacebookSetting);
        btnCheckUpdateSetting = findViewById(R.id.btnCheckUpdateSetting);

        txtCurrentVersionSetting = findViewById(R.id.txtCurrentVersionSetting);
        txtShareSetting = findViewById(R.id.txtShareSetting);
        pathSavePhoto = findViewById(R.id.pathSavePhoto);

        /*layout_feedback = findViewById(R.id.layout_feedback);
        txt_feedback = findViewById(R.id.txt_feedback);
        btn_send = findViewById(R.id.btn_send);*/
    }

    @Override
    protected void initData() {
        img_back.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        txtCurrentVersionSetting.setText(String.format(getResources().getString(R.string.text_setting_current_version),
                new Object[]{Config.VERSION_APP}));
        txtShareSetting.setText(String.format(getResources().getString(R.string.text_setting_share_app),
                new Object[]{getResources().getString(R.string.app_name)}));
        pathSavePhoto.append(Config.Directory.SAVE_DIRECTORY);

//        btnFeedbackSetting.setOnClickListener(this);
        btnRateSetting.setOnClickListener(this);
        btnShareSetting.setOnClickListener(this);
        btnFollowFacebookSetting.setOnClickListener(this);
        btnCheckUpdateSetting.setOnClickListener(this);

//        btn_send.setOnClickListener(this);

        AdsHandler.getInstance().displayInterstitial(SettingActivity.this, false);
        AdsHandler.getInstance().initBanner(SettingActivity.this,
                LibrayData.AdsSize.NATIVE_ADS, (ViewGroup)findViewById(R.id.rectangle));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            /*case R.id.btnFeedbackSetting:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (layout_feedback.getVisibility() == View.GONE) {
                    YoYo.with(Techniques.SlideInDown)
                            .duration(500)
                            .playOn(findViewById(R.id.layout_feedback));
                    layout_feedback.setVisibility(View.VISIBLE);
                    txt_feedback.requestFocus();
                    imm.showSoftInput(txt_feedback, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    YoYo.with(Techniques.SlideOutUp)
                            .duration(500)
                            .playOn(findViewById(R.id.layout_feedback));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout_feedback.setVisibility(View.GONE);
                        }
                    }, 500);
                    imm.hideSoftInputFromWindow(txt_feedback.getWindowToken(), 0);
                }
                break;

            case R.id.btn_send:
                if (txt_feedback.getText().toString().length() < 10) {
                    ToastUtil.longToast(getApplicationContext(), getString(R.string.write_at_least));
                    return;
                }
                PushFeedback.push(getApplicationContext(), txt_feedback.getText().toString(), this);
                break;*/

            case R.id.btnRateSetting:
                ShareRateUtil.rateApp(SettingActivity.this);
                break;

            case R.id.btnShareSetting:
                ShareRateUtil.shareApp(SettingActivity.this);
                break;

            case R.id.btnCheckUpdateSetting:
                ShareRateUtil.rateApp(SettingActivity.this);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onSendFeedback() {
        /*txt_feedback.setText("");
        txt_feedback.clearFocus();*/
    }
}