package com.bdevlin.apps.providers2;

import android.content.Context;
import android.content.Intent;

public class UnifiedAccountCacheProvider extends MailAppProvider {
    // The authority of our conversation provider (a forwarding provider)
    // This string must match the declaration in AndroidManifest.authenticator
    private static final String sAuthority = "com.bdevlin.apps.pandt.Provider2.accountcache";
    /**
     * Authority for the suggestions provider. This is specified in AndroidManifest.authenticator and
     * res/authenticator/searchable.authenticator.
     */
    private static final String sSuggestionsAuthority = "com.android.mail.suggestionsprovider";

    public static final String EXTRA_NO_ACCOUNTS = "AccountSettings.no_account";
    @Override
    protected String getAuthority() {
        return sAuthority;
    }

    @Override
    protected Intent getNoAccountsIntent(Context context) {
//        Intent intent = new Intent();
//                 intent.setAction(Intent.ACTION_EDIT);
//                // intent.setData(Uri.parse("content://ui.email.android.com/settings"));
//                 intent.putExtra(EXTRA_NO_ACCOUNTS, true);
//                return intent;
        return null;
    }

    @Override
    public String getSuggestionAuthority() {
        return sSuggestionsAuthority;
    }

    @Override
    public boolean onCreate() {
       return  super.onCreate();
    }
}