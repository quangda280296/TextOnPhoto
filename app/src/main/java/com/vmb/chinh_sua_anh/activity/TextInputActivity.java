package com.vmb.chinh_sua_anh.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.base.BaseActivity;

public class TextInputActivity extends BaseActivity {

    private EditText txt_text_insert;

    private View btn_close;
    private View btn_done;

    private InputMethodManager imm;

    @Override
    protected int getResLayout() {
        return R.layout.activity_text_input;
    }

    @Override
    protected void initView() {
        btn_close = findViewById(R.id.btn_close);
        btn_done = findViewById(R.id.btn_done);
        txt_text_insert = findViewById(R.id.txt_text_insert);
    }

    @Override
    protected void initData() {
        btn_close.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        btn_done.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        AdsHandler.getInstance().initBanner(TextInputActivity.this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        String text = bundle.getString(Config.KeyIntentData.KEY_TEXT_INPUT_ACT, "");
        txt_text_insert.setText(text);
        int padding = getResources().getInteger(R.integer.padding_default_textStatus);
        txt_text_insert.setPadding(padding, padding, padding, padding);
        //txt_text_insert.setSelectAllOnFocus(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;

            case R.id.btn_done:
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                String text = txt_text_insert.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(Config.KeyIntentData.KEY_TEXT_INPUT_ACT, text);
                intent.putExtra(Config.KeyIntentData.KEY_LINE_INPUT_ACT, txt_text_insert.getLineCount());
                setResult(Activity.RESULT_OK, intent);
                finish();
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
}