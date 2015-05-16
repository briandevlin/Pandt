package com.bdevlin.apps.utils;

/**
 * Created by brian on 7/20/2014.
 */
import com.bdevlin.apps.pandt.R;
import android.content.res.Resources;
public class Utils {
    public static final String EXTRA_ACCOUNT = "account";
    public static final String EXTRA_ACCOUNT_URI = "accountUri";
    public static final String EXTRA_FOLDER_URI = "folderUri";
    public static final String EXTRA_FOLDER = "folder";
    public static final String EXTRA_COMPOSE_URI = "composeUri";
    public static final String EXTRA_CONVERSATION = "conversationUri";
    public static final String EXTRA_FROM_NOTIFICATION = "notification";

    public static boolean useTabletUI(Resources res) {
        return res.getInteger(R.integer.use_tablet_ui) != 0;
    }
}
