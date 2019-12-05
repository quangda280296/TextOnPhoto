package com.vmb.chinh_sua_anh.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vm.TextOnPhoto.PhotoEditor.R;

import jack.com.servicekeep.act.BaseVMAppCompatActivity;

public abstract class BaseActivity extends BaseVMAppCompatActivity implements View.OnClickListener {

    protected abstract int getResLayout();
    protected abstract void initView();
    protected abstract void initData();

    protected abstract void onActivity_Result(int requestCode, int resultCode, Intent data);
    protected abstract void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }

        setContentView(getResLayout());
        initView();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivity_Result(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissions_Result(requestCode, permissions, grantResults);
    }
}