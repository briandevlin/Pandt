package com.bdevlin.apps.utils;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

/**
 * Created by brian on 11/1/2014.
 */
public class PandtAccountUtils {

    private static final String TAG =  PandtAccountUtils.class.getSimpleName();

    private static final String PREF_ACTIVE_ACCOUNT = "pandt_account";

    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static interface AuthenticateCallback {
        public boolean shouldCancelAuthentication();

        public void onAuthTokenAvailable(String authToken);
    }

    public static boolean isAuthenticated(final Context context) {

        // return !TextUtils.isEmpty(getActiveAccountName(context));
        return true;

    }



    public static boolean hasActiveAccount(final Context context) {
        return !TextUtils.isEmpty(getActiveAccountName(context));
    }

    public static String getActiveAccountName(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(PREF_ACTIVE_ACCOUNT, null);
    }

    public static Account getActiveAccount(final Context context) {
        String account = getActiveAccountName(context);
        if (account != null) {
            return new Account(account, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);//FIXME to be an pandt account
        } else {
            return null;
        }
    }

    public static boolean setActiveAccount(final Context context, final String accountName) {
        Log.d(TAG, "Set active account to: " + accountName);
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_ACTIVE_ACCOUNT, accountName).apply();
        return true;
    }

}
