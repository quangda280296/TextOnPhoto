package jack.com.servicekeep.service.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import io.realm.Realm;
import jack.com.servicekeep.model.InfoDevice;
import jack.com.servicekeep.service.ServiceUtils;
import jack.com.servicekeep.utils.NetworkUtil;

public class AlarmServiceReceiver extends BroadcastReceiver {
    public static final String ID_FLAG = "flag";
    private InfoDevice info;

    @Override
    public void onReceive(final Context context, Intent intent) {
        System.out.println("AlarmServiceReceiver-->Jacky");
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Realm realm = null;

                    try {
                        realm = Realm.getDefaultInstance();
                        final InfoDevice user1 = realm.where(InfoDevice.class).findFirst();
                        info = realm.copyFromRealm(user1);
                    } finally {
                        if (realm != null) {
                            realm.close();
                        }
                    }

                    if (info != null && ServiceUtils.isScreenOn(context) && NetworkUtil.isNetworkAvailable(context)) {
                        if (info != null && !info.isApp) {
                            System.out.println("AlarmServiceReceiver");
                            ServiceUtils.runServiceMobile(context, true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        mainHandler.post(myRunnable);

    }
}
