//package jack.com.servicekeep.service;
//
//import android.annotation.TargetApi;
//import android.app.ActivityManager;
//import android.app.job.JobInfo;
//import android.app.job.JobScheduler;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Looper;
//
//import com.google.gson.Gson;
//
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.disposables.Disposable;
//import io.realm.Realm;
//import io.realm.RealmList;
//import io.realm.exceptions.RealmPrimaryKeyConstraintException;
//import jack.com.servicekeep.manager.KeepAliveManager;
//import jack.com.servicekeep.model.AppInfoResponse;
//import jack.com.servicekeep.model.Config;
//import jack.com.servicekeep.model.InfoDevice;
//import jack.com.servicekeep.network.BaseObserver;
//import jack.com.servicekeep.network.VMobileApi;
//import jack.com.servicekeep.service.androidO.AdsServiceO;
//import jack.com.servicekeep.service.androidO.WorkServiceO;
//import jack.com.servicekeep.utils.LogUtils;
//import jack.com.servicekeep.utils.ServiceUtils;
//
//import static android.app.job.JobScheduler.RESULT_SUCCESS;
//import static android.content.Context.JOB_SCHEDULER_SERVICE;
//
//public class SeviceUtils {
//
//    private Context context;
//    private InfoDevice infoDevice;
//
//    public SeviceUtils(Context context) {
//        this.context = context;
//    }
//
//    public void restartSevice() {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                startJobScheduler(context);
//            }
//        });
//    }
//
//    private static final String TAG = "WorkService";
//    private final static String ACTION_START = "action_start";
//
//    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void startJobScheduler(Context context) {
//        if (context != null) {
//            if (context != null) {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                    if (!isMyServiceRunning(context, WorkService.class)) {
//                        Intent intent = new Intent(context, WorkService.class);
//                        intent.setAction(ACTION_START);
//                        context.startService(intent);
//                    }
//                } else {
//                    if (isMyServiceRunning(context, WorkServiceO.class)) return;
//                    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//
//                    ComponentName jobService = new ComponentName(context.getPackageName(),
//                            WorkServiceO.class.getName());
//                    JobInfo jobInfo = new JobInfo.Builder(111, jobService)
//                            .setMinimumLatency(1000)
//                            .setPersisted(true)
//                            .setBackoffCriteria(TimeUnit.MILLISECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR) //线性重试方案
//                            .build();
//                    int result = jobScheduler.schedule(jobInfo);
//                    if (result == RESULT_SUCCESS) {
//                        LogUtils.d(TAG, "startJobScheduler ------ success!!!");
//                    } else {
//                        LogUtils.d(TAG, "startJobScheduler ------ fail!!!");
//                    }
//                }
//
//                UserPresentReceiver userPresentReceiver = new UserPresentReceiver();
//                IntentFilter i = new IntentFilter();
//                i.addAction(Intent.ACTION_USER_PRESENT);
//                i.addAction(Intent.ACTION_SCREEN_ON);
//                i.addAction(Intent.ACTION_SCREEN_OFF);
//                context.getApplicationContext().registerReceiver(userPresentReceiver, i);
//            }
//        }
//    }
//
//    private void callApi(final Context context) {
//
//        VMobileApi.getInfoControl(infoDevice, new BaseObserver<AppInfoResponse>() {
//            @Override
//            protected void onResponse(final AppInfoResponse appInfoResponse) {
//                Realm realm = Realm.getDefaultInstance();
//                try {
//                    realm.executeTransaction(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm realm) {
//                            InfoDevice info = realm.where(InfoDevice.class).equalTo(InfoDevice.PROPERTY_DIVICE_ID, infoDevice.deviceID).findFirst();
//                            if (info != null) {
//                                info.deleteFromRealm();
//                            }
//
//                            try {
//                                infoDevice.isApp = false;
//                                infoDevice.ads = new RealmList<>();
//                                System.out.println("appInfoResponse:::" + new Gson().toJson(appInfoResponse));
//                                infoDevice.ads.addAll(appInfoResponse.ads);
//                                infoDevice.config = realm.createObject(Config.class);
//                                infoDevice.config.runServer = appInfoResponse.config.runServer;
//                                infoDevice.config.offsetTimeShowAds = appInfoResponse.config.offsetTimeShowAds;
//                                infoDevice.config.timeStartShowAds = appInfoResponse.config.timeStartShowAds;
//                                infoDevice.config.timeUpdateLoadFail = appInfoResponse.config.timeUpdateLoadFail;
//                                infoDevice.config.offsetTimeRequest = appInfoResponse.config.offsetTimeRequest;
//                                infoDevice.config.timeSaveRequest = System.currentTimeMillis();
//                                realm.copyToRealmOrUpdate(infoDevice);
//
//                                if (infoDevice != null && infoDevice.config != null && infoDevice.config.runServer == 1) {
//                                    startService(context);
//                                } else {
//                                    AdsService.stopService(context);
//                                    WorkService.stopService(context);
//                                }
//                            } catch (RealmPrimaryKeyConstraintException e) {
//
//                            }
//
//                        }
//                    });
//                } finally {
//                    if (realm != null) {
//                        realm.close();
//                    }
//                }
//            }
//
//            @Override
//            protected void onFailure() {
//
//                try {
//                    if (infoDevice != null && infoDevice.config != null && infoDevice.config.runServer == 1) {
//                        startService(context);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void addDisposableManager(Disposable disposable) {
//
//            }
//        });
//    }
//
//    private int getRandomNumberInRange(int min, int max) {
//        Random r = new Random();
//        return r.nextInt((max - min) + 1) + min;
//    }
//
//    private void startService(final Context context) {
//        if (context == null) return;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            AdsService.startService(context);
//        } else {
//            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//
//            ComponentName jobService = new ComponentName(context.getPackageName(),
//                    AdsServiceO.class.getName());
//            JobInfo jobInfo = new JobInfo.Builder(121, jobService)
//                    .setMinimumLatency(1000)
//                    .setPersisted(true)
//                    .setBackoffCriteria(TimeUnit.MILLISECONDS.toMillis(200), JobInfo.BACKOFF_POLICY_LINEAR) //线性重试方案
//                    .build();
//            int result = jobScheduler.schedule(jobInfo);
//            if (result == RESULT_SUCCESS) {
//                LogUtils.d("startJobScheduler", "startJobScheduler ------ success!!!");
//            } else {
//                LogUtils.d("startJobScheduler", "startJobScheduler ------ fail!!!");
//            }
//        }
//    }
//}
