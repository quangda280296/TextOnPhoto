package jack.com.servicekeep.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UserPresentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        ServiceUtils.runServiceMobile(context);
    }
}