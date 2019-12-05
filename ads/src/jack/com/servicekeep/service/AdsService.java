package jack.com.servicekeep.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import jack.com.servicekeep.model.InfoDevice;
import jack.com.servicekeep.utils.LogUtils;
import jack.com.servicekeep.utils.NetworkUtil;


public class AdsService extends Service {

    public static final String ACTION = "com.codepath.example.servicesdemo.MyTestService";
    private static final String TAG = "VMobile";
    private TimerTask timerTask;
    private Timer timer;
    private long time_start_show_popup;
    private long offset_time_show_popup;
    private long time_user_start;
    private long last_time_show_ads;
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d(TAG, "VMobile -------   onBind");
        return null;
    }

    private InfoDevice user;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopTimerTask();
        last_time_show_ads = 0;
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Realm realm = null;
                    try {
                        realm = Realm.getDefaultInstance();
                        final InfoDevice user1 = realm.where(InfoDevice.class).findFirst();
                        user = realm.copyFromRealm(user1);

                        if (user != null) {
                            AdmobUtils.newInstance(getApplicationContext(), "ca-app-pub-3940256099942544/1033173712").initiate("ca-app-pub-3940256099942544/1033173712");
                        }
                    } finally {
                        if (realm != null) {
                            realm.close();
                        }
                    }

                    //update run service
                    realm = Realm.getDefaultInstance();
                    try {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                InfoDevice user = realm.where(InfoDevice.class).findFirst();
                                user.isResetServiceAds = true;
                                realm.copyToRealmOrUpdate(user);
                            }
                        });
                    } finally {
                        if (realm != null) {
                            realm.close();
                        }
                    }


                } catch (Exception e) {
                }
            }
        });

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (user != null && !TextUtils.isEmpty(user.deviceID)) {

                    if (user != null && last_time_show_ads == 0 && time_user_start == 0) {
                        time_user_start = System.currentTimeMillis();
                        last_time_show_ads = user.lastTimeShowAds;
                        //Toast.makeText(getApplicationContext(), "last_time_show_ads" + show(last_time_show_ads), Toast.LENGTH_SHORT).show();
                        time_start_show_popup = user.config.timeStartShowAds * 1000L;
                        offset_time_show_popup = user.config.offsetTimeShowAds * 1000L;
                        //Toast.makeText(getApplicationContext(), "IN APP" + user.config.timeStartShowAds + ":" + user.config.offsetTimeShowAds, Toast.LENGTH_LONG).show();
                    }

                    System.out.println("BEER_CODE:LAST->" + last_time_show_ads);
                    long time = System.currentTimeMillis() - last_time_show_ads;
                    LogUtils.d(TAG, "VMobile ---------- onStartCommand Service" + "---->x" + last_time_show_ads);

                    if ((System.currentTimeMillis() - time_user_start) >= time_start_show_popup) {
                        if (last_time_show_ads == 0 || (time > offset_time_show_popup)) {
                            System.out.println("VMobileX: Android 7:::- time:::::::----1" + time + "--->" + offset_time_show_popup);
                            showAds();
                        } else {
                            System.out.println("VMobileX: Android 7:::- time:::::::----1" + time);
                            //System.out.println("jacky: ----->Time show:" + time + ":" + (offset_time_show_popup - 10 * 1000L));
                            if (time >= offset_time_show_popup) {
                                System.out.println("jacky: ----->Time show ADS");
                                showAdsWhenOnOff();
                            } else if (time >= offset_time_show_popup - 10 * 1000L) {
                                loadAdsBefore15s();
                            } else if (time >= offset_time_show_popup - 5 * 1000L) {
                                loadAdsBefore3s();
                            }
                        }
                    } else if ((System.currentTimeMillis() - time_user_start) >= time_start_show_popup - 5 * 1000L && last_time_show_ads == 0) {
                        loadAdsBefore3s();
                    } else if (System.currentTimeMillis() - time_user_start >= time_start_show_popup - 10 * 1000L && last_time_show_ads == 0) {
                        loadAdsBefore15s();
                    } else if (System.currentTimeMillis() - time_user_start >= time_start_show_popup - 5 * 1000L && last_time_show_ads != 0
                            && System.currentTimeMillis() - last_time_show_ads > offset_time_show_popup) {
                        System.out.println("jacky: ----->Time show 3s::::::::::::");
                        loadAdsBefore3s();
                    }
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

        return START_STICKY;
    }

    private void showAdsWhenOnOff() {
        showAds();
    }

    private void saveLastTimeShow() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        InfoDevice user = realm.where(InfoDevice.class).findFirst();
                        //last_time_show_ads = isLoadAds ? System.currentTimeMillis() : (user.lastTimeShowAds + user.config.timeUpdateLoadFail * 1000);
                        //isLoadAdsFirst = isLoadAds;
                        last_time_show_ads = System.currentTimeMillis();
                        user.lastTimeShowAds = last_time_show_ads;
                        System.out.println("BEER_CODE:SAVE->" + last_time_show_ads);
                        user.isResetServiceAds = true;
                        realm.copyToRealmOrUpdate(user);
                    } catch (RealmPrimaryKeyConstraintException e) {

                    }

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

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
    }

    public boolean isLoadAdsFirst;
    public boolean isLoadAdsFree;
    //    private boolean isLoadAds;
    private boolean isShowAds;

    private void loadAdsBefore3s() {
        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Realm realm = null;
                try {
                    realm = Realm.getDefaultInstance();
                    InfoDevice info = realm.where(InfoDevice.class).equalTo(InfoDevice.PROPERTY_DIVICE_ID, user.deviceID).findFirst();

                    if (info != null && ServiceUtils.isScreenOn(getApplicationContext()) && NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                        if (info != null && !info.isApp && info.config != null && info.config.runServer == 1) {
                            if (!isLoadAdsFirst) {
                                Intent intent = new Intent(getApplicationContext(), AdsShowActivity.class);
                                intent.putExtra("isLoadAdsFirst", true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                                isLoadAdsFirst = true;
                            }
                        }
                    }
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    private void loadAdsBefore15s() {
        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Realm realm = null;
                try {
                    realm = Realm.getDefaultInstance();
                    InfoDevice info = realm.where(InfoDevice.class).equalTo(InfoDevice.PROPERTY_DIVICE_ID, user.deviceID).findFirst();

                    if (info != null && ServiceUtils.isScreenOn(getApplicationContext()) && NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                        if (info != null && !info.isApp && info.config != null && info.config.runServer == 1) {

                            if (!isLoadAdsFree) {
                                Intent intent = new Intent(getApplicationContext(), AdsShowActivity.class);
                                intent.putExtra("isLoadAdsFree", true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                                isLoadAdsFree = true;
                            }
                        }
                    }
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    private void showAds() {
        if (!isShowAds) {
            isShowAds = true;
            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {

                    Realm realm = null;
                    try {
                        System.out.println("Save data base");
                        saveLastTimeShow();
                        realm = Realm.getDefaultInstance();
                        InfoDevice info = realm.where(InfoDevice.class).equalTo(InfoDevice.PROPERTY_DIVICE_ID, user.deviceID).findFirst();

                        if (info != null && ServiceUtils.isScreenOn(getApplicationContext()) && NetworkUtil.isNetworkAvailable(getApplicationContext())) {

                            if (info != null && !info.isApp && info.config != null && info.config.runServer == 1) {
                                Intent intent = new Intent(getApplicationContext(), AdsShowActivity.class);
                                intent.putExtra("isLoadAdsFirst", false);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isShowAds = false;
                                    }
                                }, 1000L);

                            } else {
                                isShowAds = false;
                            }
                        }

                    } finally {
                        if (realm != null) {
                            realm.close();
                        }
                    }

                }
            };
            mainHandler.post(myRunnable);
        }
    }

    public void stopTimerTask() {
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
        stopTimerTask();
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    InfoDevice user = realm.where(InfoDevice.class).findFirst();
                    user.isResetServiceAds = false;
                    realm.copyToRealmOrUpdate(user);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        super.onDestroy();
        stopSelf();
        LogUtils.d(TAG, "VMobile ------- is onDestroy!!!");
    }
}
