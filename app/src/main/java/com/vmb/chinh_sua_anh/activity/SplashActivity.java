package com.vmb.chinh_sua_anh.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.vmb.ads_in_app.GetConfig;
import com.vmb.ads_in_app.LibrayData;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.MainApplication;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainApplication) getApplication()).registerReceiver();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);

        GetConfig.callAPI(getApplicationContext(), LibrayData.AdsSize.NATIVE_ADS,
                Config.CODE_CONTROL_APP, Config.VERSION_APP, Config.PACKAGE_NAME);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MenuActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
            }
        }, 1500);
    }
}