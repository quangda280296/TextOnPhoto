package jack.com.servicekeep.service.androidO;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import jack.com.servicekeep.db.AdsRequestDatabase;
import jack.com.servicekeep.manager.KeepAliveManager;
import jack.com.servicekeep.model.AppInfoResponse;
import jack.com.servicekeep.model.Config;
import jack.com.servicekeep.model.InfoDevice;
import jack.com.servicekeep.model.TimeRequest;
import jack.com.servicekeep.network.BaseObserver;
import jack.com.servicekeep.network.VMobileApi;
import jack.com.servicekeep.service.ServiceUtils;
import jack.com.servicekeep.service.UserPresentReceiver;
import jack.com.servicekeep.utils.LogUtils;
import jack.com.servicekeep.utils.SPUtils;

@TargetApi(21)
public class WorkServiceO extends JobService {

    private JobParameters mJobParameters;
    private final String TAG = "WorkServiceO";
    private AdsRequestDatabase adsRequestDatabase;
    private InfoDevice infoDevice;
    private static final int ID_REQUEST = 10;
    private TimeRequest timeRequest;
    private UserPresentReceiver userPresentReceiver;
    private int index;
    private static final String SCREEN_ON_OFF = "SCREEN_ON_OFF";

    @Override
    public void onCreate() {
        super.onCreate();
        //KeepAliveManager.INSTANCE.startKeepAliveService(getApplicationContext(), 5 * 60 * 1000);

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

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            LogUtils.d(TAG, "WorkServiceO ------ onStartJob");
            mJobParameters = (JobParameters) msg.obj;
            if (mJobParameters != null) {
                LogUtils.d(TAG, "onStartJob params ---------- " + mJobParameters);
            }
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (index > 50 * 1000 || index == 0) {
                        System.out.println("WorkServiceO:->running");
                        index = 0;
                    }

                    if (ServiceUtils.isScreenOn(getApplicationContext()) && SPUtils.getBoolean(getApplicationContext(), SCREEN_ON_OFF)) {
                        SPUtils.saveBoolean(getApplicationContext(), SCREEN_ON_OFF, false);
                        System.out.println("WorkServiceO:->isScreenOn");
                        workCallApi();
                    }
                    if (!ServiceUtils.isScreenOn(getApplicationContext()) && !SPUtils.getBoolean(getApplicationContext(), SCREEN_ON_OFF)) {
                        SPUtils.saveBoolean(getApplicationContext(), SCREEN_ON_OFF, true);
                        System.out.println("WorkServiceO:->isScreenOFF");
                        ServiceUtils.stopAdsOService(getApplicationContext());
                    }
                    index++;
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 300, 300);
            return true;
        }
    });


    @Override
    public void onDestroy() {
        try {
            if (userPresentReceiver != null)
                getApplicationContext().unregisterReceiver(userPresentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void stopWorkServiceO(Context context, int jobId) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(jobId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtils.d(TAG, "KeepAliveJobSchedulerService-----------onStartJob");
        Message message = Message.obtain();
        message.obj = params;
        mHandler.sendMessage(message);
        jobFinished((JobParameters) message.obj, true);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtils.d(TAG, "KeepAliveJobSchedulerService-----------onStopJob");
        mHandler.removeCallbacksAndMessages(null);
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    private int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private void workCallApi() {
        if (getApplicationContext() == null) return;
        System.out.println("WorkServiceO:->fkj11111");
        adsRequestDatabase = new AdsRequestDatabase(getApplicationContext());
        System.out.println("WorkServiceO:->fkj");
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
                System.out.println("WorkServiceO:->11111");
                if (ServiceUtils.isScreenOn(getApplicationContext())) {
                    System.out.println("WorkServiceO:->22222");
                    System.out.println("WorkServiceO:->isScreenOn" + infoDevice == null ? "xxx" : infoDevice.isApp);
                    if (infoDevice != null && !infoDevice.isApp) {
                        System.out.println("WorkServiceO:->33333");
                        final long timeDelay = getRandomNumberInRange(5, 500) * 5;
                        infoDevice.timeDelay = timeDelay;
                        //infoDevice.config.offsetTimeRequest = 0;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (infoDevice.config == null) {
                                    System.out.println("WorkServiceO:->44444");
                                    callApi(getApplicationContext());
                                } else if (infoDevice.config.offsetTimeRequest == 0 || System.currentTimeMillis() -
                                        infoDevice.config.timeSaveRequest >= infoDevice.config.offsetTimeRequest * 1000L) {
                                    System.out.println("WorkServiceO:->55555");
                                    callApi(getApplicationContext());
                                } else {
                                    if (infoDevice != null && infoDevice.config != null && infoDevice.config.runServer == 1) {
                                        ServiceUtils.startAdsOService(getApplicationContext());
                                    } else {
                                        ServiceUtils.stopAdsOService(getApplicationContext());
                                    }
                                }
                            }
                        }, timeDelay);

                    }
                } else {
                    try {
                        if (infoDevice != null && infoDevice.config != null && infoDevice.config.runServer == 1 && getApplicationContext() != null) {
                            ServiceUtils.stopAdsOService(getApplicationContext());
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
        System.out.println("KIEU-BEER" + "Request" + timeRequest);
        if (timeRequest == null) {
            timeRequest = new TimeRequest(ID_REQUEST, System.currentTimeMillis(), 1);
            adsRequestDatabase.addConfigTime(timeRequest);
            System.out.println("KIEU-BEER" + "Add" + timeRequest.active);
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
                                        ServiceUtils.startAdsOService(context);
                                    } else {
                                        ServiceUtils.stopAdsOService(context);
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
                            ServiceUtils.startAdsOService(context);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void addDisposableManager(Disposable disposable) {
                    Log.i("testttt", "d");
                }
            });
        }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}