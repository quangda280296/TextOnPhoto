package com.vmb.chinh_sua_anh;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.ads.AudienceNetworkAds;
import com.google.firebase.FirebaseApp;
import com.vmb.ads_in_app.util.TimeRegUtil;
import com.vmb.chinh_sua_anh.activity.MenuActivity;
import com.vmb.chinh_sua_anh.receiver.ConnectionReceiver;

import io.fabric.sdk.android.Fabric;
import jack.com.servicekeep.app.VMApplication;

public class MainApplication extends VMApplication {

    ConnectionReceiver receiver = new ConnectionReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Fabric.with(getApplicationContext(), new Crashlytics());
                TimeRegUtil.setTimeRegister(getApplicationContext());
                FirebaseApp.initializeApp(getApplicationContext());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    String process = getProcessName();
                    if (!getPackageName().equals(process))
                        WebView.setDataDirectorySuffix(process);
                }

                FacebookSdk.sdkInitialize(getApplicationContext());
                try {
                    // Initialize the Audience Network SDK
                    AudienceNetworkAds.initialize(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity instanceof MenuActivity) {
                    try {
                        unregisterReceiver(receiver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });

        initInfoDevice(Config.CODE_CONTROL_APP, Config.VERSION_APP);
    }

    public void registerReceiver() {
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}