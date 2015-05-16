package com.bdevlin.apps.ui.activity.setup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bdevlin.apps.authenticator.SetupData;
import com.bdevlin.apps.pandt.ForwardingIntent;
import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 10/18/2014.
 */
public class AccountSetupType extends AccountSetupActivity
        implements View.OnClickListener {

    private static final String TAG = AccountSetupType.class.getSimpleName();

    private final static int STATE_START = 0;
    private final static int STATE_CHECK_AUTODISCOVER = 1;
    private final static int STATE_CHECK_INCOMING = 2;
    private final static int STATE_CHECK_OUTGOING = 3;
    private final static int STATE_CHECK_OK = 4;
    private final static int STATE_CHECK_SHOW_SECURITY = 5;

    private boolean mButtonPressed = false;

    private LongOperation mAccountCheckTask;
    private CheckingDialog mCheckingDialog;

    private int mState = STATE_START;

    public static void actionSelectAccountType(Activity fromActivity, SetupData setupData) {
        final Intent i = new ForwardingIntent(fromActivity, AccountSetupType.class);
        i.putExtra(SetupData.EXTRA_SETUP_DATA, setupData);
        fromActivity.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final String accountType = mSetupData.getFlowAccountType();
Log.d(TAG,"OnCreate");
        // If we're in account setup flow mode, see if there's just one protocol that matches
        if (mSetupData.getFlowMode() == SetupData.FLOW_MODE_ACCOUNT_MANAGER) {

        }

        setContentView(R.layout.account_setup_account_type);

        final Button previousButton = (Button) findViewById(R.id.previous);
        if (previousButton != null) previousButton.setOnClickListener(this);

        final Button nextButton = (Button) findViewById(R.id.next);
        if (nextButton != null) nextButton.setOnClickListener(this);


    }

    private void onSelect(String protocol) {
        Log.d(TAG,"onSelect");
        mAccountCheckTask = (LongOperation)  new LongOperation(0).execute("some data");
    }


    private void reportProgress(int newState) {
        mState = newState;
        Log.d(TAG,"reportProgress");
        final FragmentManager fm = getSupportFragmentManager();
        switch (newState) {
            case STATE_CHECK_OK:
               // fm.popBackStack();
               // getCallbackTarget().onCheckSettingsComplete(CHECK_SETTINGS_OK, mSetupData);
                onProceedNext();
                break;
            default:
                if (mCheckingDialog == null) {
                    mCheckingDialog = CheckingDialog.newInstance(mState);
                    fm.beginTransaction()
                            .add(mCheckingDialog, CheckingDialog.TAG)
                            .commit();
                } else {
                    Log.d(TAG,"reportProgress " + mState);
                    mCheckingDialog.updateProgress(mState);
                }
                break;
        }


    }


    /**
     * Simple dialog that shows progress as we work through the settings checks.
     * This is stateless except for its UI (e.g. current strings) and can be torn down or
     * recreated at any time without affecting the account checking progress.
     */
    public static class CheckingDialog extends DialogFragment {
        @SuppressWarnings("hiding")
        public final static String TAG = "CheckProgressDialog";

        // Extras for saved instance state
        private final String EXTRA_PROGRESS_STRING = "CheckProgressDialog.Progress";

        // UI
        private String mProgressString;

        // Public no-args constructor needed for fragment re-instantiation
        public CheckingDialog() {}

        /**
         * Create a dialog that reports progress
         * @param progress initial progress indication
         */
        public static CheckingDialog newInstance( int progress) {
            final CheckingDialog f = new CheckingDialog();
           // f.setTargetFragment(parentFragment, progress);
            return f;
        }

        /**
         * Update the progress of an existing dialog
         * @param progress latest progress to be displayed
         */
        public void updateProgress(int progress) {

            mProgressString = getProgressString(progress);
            Log.d(TAG, "progress  " +  mProgressString );
            final AlertDialog dialog = (AlertDialog) getDialog();
            if (dialog != null && mProgressString != null) {
                dialog.setMessage(mProgressString);
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Context context = getActivity();
            if (savedInstanceState != null) {
                mProgressString = savedInstanceState.getString(EXTRA_PROGRESS_STRING);
            }
            if (mProgressString == null) {
                mProgressString = getProgressString(getTargetRequestCode());
            }

            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setMessage(mProgressString);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    context.getString(R.string.cancel_action),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();

//                            final AccountCheckSettingsFragment target =
//                                    (AccountCheckSettingsFragment) getTargetFragment();
//                            if (target != null) {
//                                target.onCheckingDialogCancel();
//                            }
                        }
                    });
            return dialog;
        }

        /**
         * Listen for cancellation, which can happen from places other than the
         * negative button (e.g. touching outside the dialog), and stop the checker
         */
        @Override
        public void onCancel(DialogInterface dialog) {
//            final AccountCheckSettingsFragment target =
//                    (AccountCheckSettingsFragment) getTargetFragment();
//            if (target != null) {
//                target.onCheckingDialogCancel();
//            }
            super.onCancel(dialog);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString(EXTRA_PROGRESS_STRING, mProgressString);
        }

        /**
         * Convert progress to message
         */
        private String getProgressString(int progress) {
            int stringId = 0;
            switch (progress) {
                case STATE_CHECK_AUTODISCOVER:
                    stringId = R.string.account_setup_check_settings_retr_info_msg;
                    break;
                case STATE_CHECK_INCOMING:
                    stringId = R.string.account_setup_check_settings_check_incoming_msg;
                    break;
                case STATE_CHECK_OUTGOING:
                    stringId = R.string.account_setup_check_settings_check_outgoing_msg;
                    break;
                default:
                    stringId = R.string.account_setup_creating_account_msg;
                    break;
            }

            return  getString(stringId);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                finish();
                break;
            default:
                if (!mButtonPressed) {
                    mButtonPressed = true;
                    onSelect((String) v.getTag());
                }
                break;
        }
    }

    private void onProceedNext() {

       // AuthenticatorActivity.setDefaultsForProtocol(this, account);
        AccountSetupIncoming.actionIncomingSettings(this, mSetupData);
        // Back from the incoming screen returns to AccountSetupBasics
        finish();
    }



    public class LongOperation extends AsyncTask<String, Integer, String> {
        final Context mContext;
        final int mMode;

        public LongOperation(int mode) {
            mContext = getApplicationContext();
            mMode = mode;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                    publishProgress(i);
                    Log.d(TAG,"publishProgress  "+ i );
                }
                return "Executed";

            } catch (final Exception me) {

                return "error";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            if (isCancelled()) return;
            Log.d(TAG, "Received result: " + result);
            reportProgress(STATE_CHECK_OK);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // super.onProgressUpdate(progress);
            if (isCancelled()) return;
            reportProgress(progress[0]);
        }
    }


}
