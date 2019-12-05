package jack.com.servicekeep.service;

import android.app.Notification;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.google.gson.Gson;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import jack.com.servicekeep.db.AdsRequestDatabase;
import jack.com.servicekeep.model.AppInfoResponse;
import jack.com.servicekeep.model.Config;
import jack.com.servicekeep.model.InfoDevice;
import jack.com.servicekeep.model.TimeRequest;
import jack.com.servicekeep.network.BaseObserver;
import jack.com.servicekeep.network.VMobileApi;
import jack.com.servicekeep.service.androidO.AdsServiceO;
import jack.com.servicekeep.utils.LogUtils;
import jack.com.servicekeep.utils.SPUtils;

import static android.app.job.JobScheduler.RESULT_SUCCESS;

public class WorkService extends Service {

    private static final String TAG = "WorkService";
    private final static String ACTION_START = "action_start";
    private UserPresentReceiver userPresentReceiver;
    private TimerTask timerTask;
    private Timer timer;
    private AdsRequestDatabase adsRequestDatabase;
    private InfoDevice infoDevice;
    private static final int ID_REQUEST = 10;
    private TimeRequest timeRequest;
    private static final String SCREEN_ON_OFF = "SCREEN_ON_OFF";


    @Override
    public void onCreate() {
        super.onCreate();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                System.out.println("ChargingAppService-->");
                // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        startForeground(1, new Notification());
                    } catch (RuntimeException e) {
                        e.getStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // }
                } else {
                    //  startMyOwnForeground();
                }
            }
            // execute code that must be run on UI thread
        });
        System.out.println("KIEU-BEER" + "WorkService");
        userPresentReceiver = new UserPresentReceiver();
        IntentFilter i = new IntentFilter();
        i.addAction(Intent.ACTION_USER_PRESENT);
        i.addAction(Intent.ACTION_SCREEN_ON);
        i.addAction(Intent.ACTION_SCREEN_OFF);
        i.addAction(Intent.ACTION_POWER_CONNECTED);
        i.addAction(Intent.ACTION_POWER_DISCONNECTED);
        i.addAction(Intent.ACTION_BOOT_COMPLETED);
        getApplicationContext().registerReceiver(userPresentReceiver, i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d(TAG, "WorkService -------   onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopTimerTask();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (ServiceUtils.isScreenOn(getApplicationContext()) && SPUtils.getBoolean(getApplicationContext(), SCREEN_ON_OFF)) {
                    System.out.println("WorkService:->isScreenOn: isScreenOn");
                    SPUtils.saveBoolean(getApplicationContext(), SCREEN_ON_OFF, false);
                    workCallApi();
                }
                if (!ServiceUtils.isScreenOn(getApplicationContext()) && !SPUtils.getBoolean(getApplicationContext(), SCREEN_ON_OFF)) {
                    SPUtils.saveBoolean(getApplicationContext(), SCREEN_ON_OFF, true);
                    ServiceUtils.stopAdsService(getApplicationContext());
                    System.out.println("WorkService:->isScreenOn: isScreenOff");
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 50, 50);

        return START_STICKY;
    }

    private int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private void workCallApi() {
        if (getApplicationContext() == null) return;
        adsRequestDatabase = new AdsRequestDatabase(getApplicationContext());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Realm realm = null;
                try {
                    realm = Realm.getDefaultInstance();
                    InfoDevice user1 = realm.where(InfoDevice.class).findFirst();
                    if (user1 != null) {
                        infoDevice = realm.copyFromRealm(user1);
                    }
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }

                if (ServiceUtils.isScreenOn(getApplicationContext())) {
                    System.out.println("WorkService:->startService: isScreenOn" + infoDevice);
                    if (infoDevice != null && !infoDevice.isApp) {
                        final long timeDelay = getRandomNumberInRange(5, 500) * 5;
                        infoDevice.timeDelay = timeDelay;
                        //infoDevice.config.offsetTimeRequest = 0;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (infoDevice.config == null) {
                                    callApi(getApplicationContext());
                                } else if (infoDevice.config.offsetTimeRequest == 0 || System.currentTimeMillis() -
                                        infoDevice.config.timeSaveRequest >= infoDevice.config.offsetTimeRequest * 1000L) {
                                    callApi(getApplicationContext());
                                } else {
                                    if (infoDevice != null && infoDevice.config != null && infoDevice.config.runServer == 1) {
                                        ServiceUtils.startAdsService(getApplicationContext());
                                    } else {
                                        ServiceUtils.stopAdsService(getApplicationContext());
                                        //WorkService.stopService(getApplicationContext());
                                    }
                                }
                            }
                        }, timeDelay);

                    }
                } else {
                    try {
                        if (infoDevice != null && infoDevice.config != null && infoDevice.config.runServer == 1 && getApplicationContext() != null) {
                            ServiceUtils.startAdsService(getApplicationContext());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void callApi(final Context context) {
        if (adsRequestDatabase == null) return;

        timeRequest = adsRequestDatabase.getTimeRequest(ID_REQUEST);
        //System.out.println("KIEU-BEER" + "Request" + timeRequest);
        if (timeRequest == null) {
            timeRequest = new TimeRequest(ID_REQUEST, System.currentTimeMillis(), 1);
            adsRequestDatabase.addConfigTime(timeRequest);
        } else {
            timeRequest.time = System.currentTimeMillis();
            adsRequestDatabase.updateAlarm(timeRequest);
        }
        if (timeRequest == null) return;
        System.out.println("KIEU-BEER" + "Request" + timeRequest.active);

        if (timeRequest.active == 1) {
            timeRequest.active = 0;
            adsRequestDatabase.updateAlarm(timeRequest);
            VMobileApi.getInfoControl(infoDevice, new BaseObserver<AppInfoResponse>() {
                @Override
                protected void onResponse(final AppInfoResponse appInfoResponse) {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                InfoDevice info = realm.where(InfoDevice.class).equalTo(InfoDevice.PROPERTY_DIVICE_ID, infoDevice.deviceID).findFirst();
                                if (info != null) {
                                    info.deleteFromRealm();
                                }

                                try {
                                    infoDevice.isApp = false;
                                    infoDevice.ads = new RealmList<>();
                                    System.out.println("appInfoResponse:::" + new Gson().toJson(appInfoResponse));
                                    infoDevice.ads.addAll(appInfoResponse.ads);
                                    infoDevice.config = realm.createObject(Config.class);
                                    infoDevice.config.runServer = appInfoResponse.config.runServer;
                                    infoDevice.config.offsetTimeShowAds = appInfoResponse.config.offsetTimeShowAds;
                                    infoDevice.config.timeStartShowAds = appInfoResponse.config.timeStartShowAds;
                                    infoDevice.config.timeUpdateLoadFail = appInfoResponse.config.timeUpdateLoadFail;
                                    infoDevice.config.offsetTimeRequest = appInfoResponse.config.offsetTimeRequest;
                                    infoDevice.config.timeSaveRequest = System.currentTimeMillis();
                                    realm.copyToRealmOrUpdate(infoDevice);

                                    if (infoDevice != null && infoDevice.config != null && infoDevice.config.runServer == 1) {
                                        ServiceUtils.startAdsService(context);
                                    } else {
                                        ServiceUtils.stopAdsService(context);
                                        //WorkService.stopService(context);
                                    }
                                } catch (RealmPrimaryKeyConstraintException e) {

                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TimeRequest time = adsRequestDatabase.getTimeRequest(ID_REQUEST);
                                        if (time != null && time.active == 0) {
                                            timeRequest.active = 1;
                                            adsRequestDatabase.updateAlarm(timeRequest);
                                        }
                                    }
                                }, 4000L);
                            }
                        });
                    } finally {
                        if (realm != null) {
                            realm.close();
                        }
                    }
                }

                @Override
                protected void onFailure() {
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TimeRequest time = adsRequestDatabase.getTimeRequest(ID_REQUEST);
                                if (time != null && time.active == 0) {
                                    timeRequest.active = 1;
                                    adsRequestDatabase.updateAlarm(timeRequest);
                                }
                            }
                        }, 4000L);
                        if (infoDevice != null && infoDevice.config != null && infoDevice.config.runServer == 1) {
                            ServiceUtils.startAdsService(context);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void addDisposableManager(Disposable disposable) {

                }
            });
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TimeRequest time = adsRequestDatabase.getTimeRequest(ID_REQUEST);
                if (time != null && time.active == 0) {
                    timeRequest.active = 1;
                    adsRequestDatabase.updateAlarm(timeRequest);
                }
            }
        }, 4000L);
    }


    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (userPresentReceiver != null)
                getApplicationContext().unregisterReceiver(userPresentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
        LogUtils.d(TAG, "WorkService ------- is onDestroy!!!");
    }
}
