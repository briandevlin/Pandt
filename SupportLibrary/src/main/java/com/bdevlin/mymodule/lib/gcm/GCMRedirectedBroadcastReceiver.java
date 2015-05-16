package com.bdevlin.mymodule.lib.gcm;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/**
 * Created by brian on 11/9/2014.
 */
/**
 * Created by brian on 9/28/2014.
 */
public class GCMRedirectedBroadcastReceiver  extends GCMBroadcastReceiver {

    /**
     * Gets the class name of the intent service that will handle GCM messages.
     *
     * Used to override class name, so that GCMIntentService can live in a
     * subpackage.
     */
    @Override
    protected String getGCMIntentServiceClassName(Context context) {
        return GCMIntentService.class.getCanonicalName();
    }
}
