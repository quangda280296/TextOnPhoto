package com.vmb.chinh_sua_anh;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;

public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Fabric.with(getApplicationContext(), new Crashlytics());
                //TimeRegUtil.setTimeRegister(getApplicationContext());
                //AlarmUtil.setAlarm(getApplicationContext());

                FirebaseApp.initializeApp(getApplicationContext());
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
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
                }*/
            }
        }).start();

        /*registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
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
                if (activity instanceof MainActivity)
                    android.os.Process.killProcess(android.os.Process.myPid());
            }
        });*/

        //initInfoDevice(Config.CODE_CONTROL_APP, Config.VERSION_APP);
    }
}