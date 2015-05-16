package com.bdevlin.apps.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.bdevlin.apps.pandt.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by brian on 10/11/2014.
 */
public class PlayServicesUtils {

    public static boolean checkGooglePlaySevices(final Activity activity) {
        final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        switch (googlePlayServicesCheck) {
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, activity, 0);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        activity.finish();
                    }
                });
                dialog.show();
        }
        return false;
    }



    public static void complainMustHaveGoogleAccount(final Activity activity) {

        new AlertDialog.Builder(activity)
                .setTitle(R.string.google_account_required_title)
                .setMessage(R.string.google_account_required_message)
                .setPositiveButton(R.string.add_account, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        promptAddAccount(activity);
                    }
                })
                .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .show();
    }


    private static void promptAddAccount(final Activity activity) {
        Intent intent = new Intent(Settings.ACTION_ADD_ACCOUNT);
        intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[]{"com.google"});
        activity.startActivity(intent);
        activity.finish();
    }

}
