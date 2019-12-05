package jack.com.servicekeep.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import jack.com.servicekeep.service.alarm.AlarmServiceReceiver;
import jack.com.servicekeep.service.androidO.AdsServiceO;
import jack.com.servicekeep.service.androidO.WorkServiceO;
import jack.com.servicekeep.utils.LogUtils;

import static android.app.job.JobScheduler.RESULT_SUCCESS;
import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class ServiceUtils {

    private final static String ACTION_START = "action_start";

    public static final int ID_WORK_SERVICE = 598;
    public static final int ID_ADS_SERVICE = 985;

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isScreenOn();
        } else {
            isScreenOn = pm.isInteractive();
        }
        return isScreenOn;
    }

    public static void runServiceMobile(Context context) {
        runServiceMobile(context, false);
    }

    public static void runServiceMobile(Context context, boolean reset) {
        try {
            if (reset) {
                System.out.println("WorkServiceO:->stopWorkOService");
                setAlarmService(context);
                stopWorkServiceService(context);
                stopWorkOService(context);
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                if (!ServiceUtils.isMyServiceRunning(context, WorkService.class)) {
                    context.startService(new Intent(context, WorkService.class));
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// && Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1
                if (!ServiceUtils.isMyServiceRunning(context, WorkService.class)) {
                    context.startForegroundService(new Intent(context, WorkService.class));
                }
            }
//            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                System.out.println("WorkServiceO:->runServiceMobile" + ServiceUtils.isMyServiceRunning(context, WorkServiceO.class));
//                if (!ServiceUtils.isMyServiceRunning(context, WorkServiceO.class)) {
//                    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//
//                    ComponentName jobService = new ComponentName(context.getPackageName(),
//                            WorkServiceO.class.getName());
//                    JobInfo jobInfo = new JobInfo.Builder(ID_WORK_SERVICE, jobService)
////                        .setMinimumLatency(3 * 1000) // Wait at least 30s
////                        .setOverrideDeadline(60 * 1000) // Maximum delay 60s
////                        .setPersisted(true)
//                            //.setRequiresDeviceIdle(false) // run not only if the device is idle
//                            //.setRequiresCharging(false)
//                            //.setBackoffCriteria(TimeUnit.MILLISECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR) //线性重试方案
//
//                            .setMinimumLatency(TimeUnit.MILLISECONDS.toMillis(2000))//执行的最小延迟时间
//                            .setOverrideDeadline(TimeUnit.MILLISECONDS.toMillis(60000))  //执行的最长延时时间
//                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) //任何网络状态
//                            .setRequiresCharging(false) // 未充电状态
//                            .build();
//                    int result = jobScheduler.schedule(jobInfo);
//                    if (result == RESULT_SUCCESS) {
//                        LogUtils.d(UserPresentReceiver.class.getName(), "startJobScheduler ------ success!!!");
//                    } else {
//                        LogUtils.d(UserPresentReceiver.class.getName(), "startJobScheduler ------ fail!!!");
//                    }
//                }
//            }
        } catch (java.lang.NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //WorkService
    public static void stopWorkServiceService(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, WorkService.class);
            context.stopService(intent);
        }
    }

    public static void startWorkServiceService(Context context) {
        System.out.println("WorkService:->startService");
        if (context != null) {
            runServiceMobile(context);
        }
    }

    //Android Ads Service

    public static void stopAdsService(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, AdsService.class);
            context.stopService(intent);
        }
    }

    public static void startAdsService(Context context) {
        if (isMyServiceRunning(context, AdsService.class)) {
            stopAdsService(context);
        }
        if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
                if (!ServiceUtils.isMyServiceRunning(context, AdsService.class)) {
                    context.startForegroundService(new Intent(context, AdsService.class));
                }
            } else {
                Intent intent = new Intent(context, AdsService.class);
                intent.setAction(ACTION_START);
                context.startService(intent);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void stopWorkOService(Context context) {
        try {
            if (ServiceUtils.isMyServiceRunning(context, WorkServiceO.class)) {
                stopMyJob(context, ID_WORK_SERVICE);
                Intent intent = new Intent(context, WorkServiceO.class);
                context.stopService(intent);
            }
        } catch (java.lang.NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //AdsServiceO

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void startAdsOService(Context context) {
        stopAdsOService(context);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName jobService = new ComponentName(context.getPackageName(),
                AdsServiceO.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(ID_ADS_SERVICE, jobService)
                .setMinimumLatency(1000)
                .setPersisted(true)
                .setBackoffCriteria(TimeUnit.MILLISECONDS.toMillis(200), JobInfo.BACKOFF_POLICY_LINEAR) //线性重试方案
                .build();
        int result = jobScheduler.schedule(jobInfo);
        if (result == RESULT_SUCCESS) {
            LogUtils.d("startJobScheduler", "startJobScheduler ------ success!!!");
        } else {
            LogUtils.d("startJobScheduler", "startJobScheduler ------ fail!!!");
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void stopAdsOService(Context context) {
        stopService(context, ID_ADS_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void stopService(Context applicationContext, int jobId) {
        try {
            if (ServiceUtils.isMyServiceRunning(applicationContext, AdsServiceO.class)) {
                stopMyJob(applicationContext, jobId);
                Intent intent = new Intent(applicationContext, AdsServiceO.class);
                applicationContext.stopService(intent);
            }
        } catch (java.lang.NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void stopMyJob(Context context, int jobId) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(jobId);
    }

    public static void startAds(Context context) {
        runServiceMobile(context);
        setAlarmService(context);
    }

    //Alarm
    private static final int ONE_DAY_TIME = 1000 * 60 * 60 * 24;

    public static void setAlarmService(final Context context) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmServiceReceiver.class);
//        intent.putExtra(AlarmServiceReceiver.ID_FLAG, Integer.toString(1111));
//        long actualTime = System.currentTimeMillis() + 3 * 60 * 1000;
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(actualTime);
//
//        DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm");
//        String dateTime[] = format.format(c.getTime()).split(" ");
//
//        String dateAlarm[] = dateTime[0].split(":");
//        String timeAlarm[] = dateTime[1].split(":");
//        System.out.println("JACKY:->" + dateAlarm[0] + "->" + dateAlarm[1] + "->" + dateAlarm[2]);
//
//        Calendar calAlarm = new GregorianCalendar();
//        calAlarm.set(Calendar.YEAR, Integer.valueOf(dateAlarm[0]));
//        calAlarm.set(Calendar.MONTH, Integer.valueOf(dateAlarm[1]) - 1);
//        calAlarm.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateAlarm[2]));
//        calAlarm.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeAlarm[0]));
//        calAlarm.set(Calendar.MINUTE, Integer.valueOf(timeAlarm[1]));
//        calAlarm.set(Calendar.SECOND, 0);
//        calAlarm.set(Calendar.MILLISECOND, 0);
//
//        System.out.println("JACKY:->TIME" + Integer.valueOf(timeAlarm[0]) + "SS" + Integer.valueOf(timeAlarm[1]));
//        long actualTimeAlarm = calAlarm.getTimeInMillis() > System.currentTimeMillis()
//                ? calAlarm.getTimeInMillis() : calAlarm.getTimeInMillis() + ONE_DAY_TIME;
//
//        //cancel alarm
//        Intent myIntent = new Intent(context, AlarmServiceReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context, 1111, myIntent, 0);
//        alarmManager.cancel(pendingIntent);
//
//        //create alarm
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, actualTimeAlarm, PendingIntent.getBroadcast(context,
//                    1111, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT));
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, actualTimeAlarm, PendingIntent.getBroadcast(context,
//                    1111, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT));
//        }
        long KEEP_ALIVE_INTERVAL_TIME = 10 * 1000L;
        Intent intent = new Intent(context, AlarmServiceReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 1111, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + KEEP_ALIVE_INTERVAL_TIME,
                KEEP_ALIVE_INTERVAL_TIME, pendingIntent);
    }
}
