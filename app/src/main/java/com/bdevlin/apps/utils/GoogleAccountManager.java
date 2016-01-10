package com.bdevlin.apps.utils;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.util.Log;


/**
 * Created by brian on 10/11/2014.
 */
public class GoogleAccountManager {

    private static final String TAG = GoogleAccountManager.class.getSimpleName();
    /**
     * Google account type.
     */
    private static final String ACCOUNT_TYPE = "com.google";

    /**
     * Account manager.
     */
    private final AccountManager manager;


    /**
     * @param accountManager account manager
     */
    private GoogleAccountManager(AccountManager accountManager) {
        this.manager = accountManager;
    }

    /**
     * @param context context from which to retrieve the account manager
     */
    public GoogleAccountManager(Context context) {
        this(AccountManager.get(context));
    }

    /**
     * Returns the account manager.
     */
    public AccountManager getAccountManager() {
        return manager;
    }

    /**
     * Returns all Google accounts.
     *
     * @return array of Google accounts
     */
    public Account[] getAccounts() {
        return manager.getAccountsByType(ACCOUNT_TYPE);
    }

    /**
     * Returns the Google account of the given {@link Account#name}.
     *
     * @param accountName Google account baseName or {@code null} for {@code null} result
     * @return Google account or {@code null} for none found or for {@code null} input
     */
    public Account getAccountByName(String accountName) {
        if (accountName != null) {
            for (Account account : getAccounts()) {
                if (accountName.equals(account.name)) {
                    return account;
                }
            }
        }
        return null;
    }

    public Account[] getGoogleAccountByType() {
        return manager.getAccountsByType(ACCOUNT_TYPE);
    }

    /**
     * Invalidates the given Google auth token by removing it from the account manager's cache (if
     * necessary) for example if the auth token has expired or otherwise become invalid.
     *
     * @param authToken auth token
     */
    public void invalidateAuthToken(String authToken) {
        manager.invalidateAuthToken(ACCOUNT_TYPE, authToken);
    }


   public  String getDefaultAccount() {
        // Choose first account on device.
        Log.d(TAG, "Choosing default account (first account on device)");
        // AccountManager am = AccountManager.get(mActivity);
        android.accounts.Account[] accounts = getGoogleAccountByType();
        if (accounts.length == 0) {
            // No Google accounts on device.
            Log.w(TAG, "No Google accounts on device; not setting default account.");
            return null;
        }

        Log.d(TAG, "Default account is: " + accounts[0].name);
        return accounts[0].name;
    }


    public String getActiveOrDefaultAccount(Activity activity) {
        Log.d(TAG, "gets the active or set default as the active account.");
        // AccountUtils.setActiveAccount(mActivity, null); test only

        if (!GoogleAccountUtils.hasActiveAccount(activity)) {
            Log.d(TAG, "No active account, attempting to pick a default.");
            String defaultAccount = getDefaultAccount();
            if (defaultAccount == null) {
                Log.e(TAG, "Failed to pick default account (no accounts). Failing.");
                PlayServicesUtils.complainMustHaveGoogleAccount(activity);
                return null;
            }
            Log.d(TAG, "Default to: " + defaultAccount);
            GoogleAccountUtils.setActiveAccount(activity, defaultAccount);
        }

        if (!GoogleAccountUtils.hasActiveAccount(activity)) {
            Log.d(TAG, "Can't proceed with login -- no account chosen.");
            return null;
        } else {
            Log.d(TAG, "Chosen account: " + GoogleAccountUtils.getActiveAccountName(activity));
        }

        return GoogleAccountUtils.getActiveAccountName(activity);
    }
}
