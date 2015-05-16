package com.bdevlin.apps.pandt;

import android.content.Context;
import android.content.Intent;

/**
 * An intent that forwards results
 */
public class ForwardingIntent extends Intent {
    public ForwardingIntent(Context activity, Class klass) {
        super(activity, klass);
        setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
    }
}
