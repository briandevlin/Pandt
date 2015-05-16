package com.bdevlin.mymodule.lib.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by brian on 11/9/2014.
 */
public class TriggerSyncReceiver extends BroadcastReceiver
{
    private static final String TAG = TriggerSyncReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("TriggerSyncReceiver", "intent=" + intent);
        String message = intent.getStringExtra("message");
        Log.d("TriggerSyncReceiver", message);
    }
}