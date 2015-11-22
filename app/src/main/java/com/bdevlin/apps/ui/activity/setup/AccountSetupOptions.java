package com.bdevlin.apps.ui.activity.setup;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bdevlin.apps.authenticator.SetupData;
import com.bdevlin.apps.utils.ForwardingIntent;
import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 10/22/2014.
 */
public class AccountSetupOptions extends AccountSetupActivity implements View.OnClickListener {

    private static final String TAG = AccountSetupOptions.class.getSimpleName();

    private static final String EXTRA_IS_PROCESSING_KEY = "com.bdevlin.apps.pandt.is_processing";

    private boolean mDonePressed = false;
    private boolean mIsProcessing = false;
    private ProgressDialog mCreateAccountDialog;


    public static void actionOptions(Activity fromActivity, SetupData setupData) {
        final Intent intent = new ForwardingIntent(fromActivity, AccountSetupOptions.class);
        intent.putExtra(SetupData.EXTRA_SETUP_DATA, setupData);
        fromActivity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.options);

        final Button previousButton = (Button) findViewById(R.id.previous);
        if (previousButton != null) previousButton.setOnClickListener(this);

        final Button nextButton = (Button) findViewById(R.id.next);
        if (nextButton != null) nextButton.setOnClickListener(this);

        mIsProcessing = savedInstanceState != null &&
                savedInstanceState.getBoolean(EXTRA_IS_PROCESSING_KEY, false);
        if (mIsProcessing) {
            // We are already processing, so just show the dialog until we finish
            showCreateAccountDialog();
        }

    }

    private static AsyncTask<Void, Void, Void> runAsync(final Runnable r) {
        return new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... params) {
                r.run();
                return null;
            }
        }.execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_IS_PROCESSING_KEY, mIsProcessing);
    }

    @Override
    public void finish() {
        // If the account manager initiated the creation, and success was not reported,
        // then we assume that we're giving up (for any reason) - report failure.
        final AccountAuthenticatorResponse authenticatorResponse =
                mSetupData.getAccountAuthenticatorResponse();
        if (authenticatorResponse != null) {
            authenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED, "canceled");
            mSetupData.setAccountAuthenticatorResponse(null);
        }
        super.finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.next:
                // Don't allow this more than once (Exchange accounts call an async method
                // before finish()'ing the Activity, which allows this code to potentially be
                // executed multiple times
                if (!mDonePressed) {
                    onDone();
                    mDonePressed = true;
                }
                break;
            case R.id.previous:
                onBackPressed();
                break;
        }
    }


    @SuppressWarnings("deprecation")
    private void onDone() {
        mIsProcessing = true;
      //  showCreateAccountDialog();
        runAsync(new Runnable() {
            @Override
            public void run() {
                final Context context = AccountSetupOptions.this;
                // what should happen here.
                // 1. send data to AccountManager
                // 2. add a AccountManagerCallback
                // 3. once the callback is invoked run optionsComplete

                // then this would bwe run within that callback
                AccountSetupOptions.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        optionsComplete();
                    }
                });
            }
        });
       // optionsComplete();
    }


    private void showCreateAccountDialog() {
        /// Show "Creating account..." dialog
        mCreateAccountDialog = new ProgressDialog(this);
        mCreateAccountDialog.setIndeterminate(true);
        mCreateAccountDialog.setMessage(getString(R.string.account_setup_creating_account_msg));
        mCreateAccountDialog.show();
    }

    private void showErrorDialog(final int msgResId, final Object... args) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(AccountSetupOptions.this)
                        //.setIconAttribute(android.R.attr.alertDialogIcon)
                        .setTitle(getString(R.string.account_setup_failed_dlg_title))
                        .setMessage(getString(msgResId, args))
                        .setCancelable(true)
                        .setPositiveButton(
                                getString(R.string.account_setup_failed_dlg_edit_details_action),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                        .show();
            }
        });
    }

    private void optionsComplete() {
        // If the account manager initiated the creation, report success at this point
        final AccountAuthenticatorResponse authenticatorResponse =
                mSetupData.getAccountAuthenticatorResponse();
        if (authenticatorResponse != null) {
            authenticatorResponse.onResult(null);
            mSetupData.setAccountAuthenticatorResponse(null);
        }

        saveAccountAndFinish();

    }

    private void saveAccountAndFinish() {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final AccountSetupOptions context = AccountSetupOptions.this;
                Log.d(TAG, "saveAccountAndFinish");

                AccountSetupNames.actionSetNames(context, mSetupData);
                finish();
                return null;
            }
        };
        asyncTask.execute();
    }
}
