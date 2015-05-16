package com.bdevlin.mymodule.lib.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bdevlin.mymodule.lib.Config;
import com.bdevlin.mymodule.lib.R;
import com.google.android.gcm.GCMBaseIntentService;

/**
 * Created by brian on 11/9/2014.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = GCMIntentService.class.getSimpleName();

    public GCMIntentService() {
        super(Config.GCM_SENDER_ID);
        Log.d(TAG, "senderID=" + Config.GCM_SENDER_ID);

    }
    @Override
    protected void onMessage(Context context, Intent intent) {

        String action = intent.getStringExtra("action");
        String extraData = intent.getStringExtra("extraData");
        Log.d(TAG, "Got GCM message, action=" + action + ", extraData=" + extraData);

        String announcement = intent.getStringExtra("announcement");
        Log.d(TAG, "Got GCM message, announcement=" + announcement + "");

        if (announcement != null) {
            displayNotification(context, announcement);
            return;
        }
    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.d(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.w(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.d(TAG, "Device registered: regId=" + registrationId);

        try {
            // now try to register to our cloud service so we can be notified
            ServerUtilities.register(context, registrationId);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onUnregistered(Context context, String regId) {
        Log.d(TAG, "Device unregistered");
        if (ServerUtilities.isRegisteredOnServer(context/*, AccountUtils.getPlusProfileId(this)*/)) {
            ServerUtilities.unregister(context, regId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.d(TAG, "Ignoring unregister callback");
        }
    }

    private void displayNotification(Context context, String message) {
        //    LOGI(TAG, "displayNotification: " + message);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(0, new NotificationCompat.Builder(context)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_stat_gcm)
                        .setTicker(message)
                        .setContentTitle("Pandt")
                        .setContentText(message)
//                        .setContentIntent(
//                                PendingIntent.getActivity(context, 0,
//                                        new Intent(context, HomeActivity.class)
//                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                                        Intent.FLAG_ACTIVITY_SINGLE_TOP),
//                                        0))
                        .setAutoCancel(true)
                        .getNotification());
    }

}
