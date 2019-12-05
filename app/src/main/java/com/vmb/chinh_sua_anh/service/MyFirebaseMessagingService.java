package com.vmb.chinh_sua_anh.service;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.util.TokenNotiUtil;
import com.vmb.chinh_sua_anh.Config;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData());

            // Handle message within 10 seconds
            TokenNotiUtil.handleNow(getApplicationContext(), remoteMessage,
                    Config.CODE_CONTROL_APP, Config.VERSION_APP, Config.PACKAGE_NAME);

        }// if (remoteMessage.getData().size() > 0)

        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message NotificationHandler Body: " + remoteMessage.getNotification().getBody());
        }
    }
}