package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.bdevlin.apps.authenticator.SetupData;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.activity.setup.AccountSetupIncoming;

/**
 * Created by brian on 10/18/2014.
 */
public class AccountCheckSettingsFragment extends Fragment {

    public static final String TAG = AccountCheckSettingsFragment.class.getSimpleName();

    // State
    private final static int STATE_START = 0;
    private final static int STATE_CHECK_AUTODISCOVER = 1;
    private final static int STATE_CHECK_INCOMING = 2;
    private final static int STATE_CHECK_OUTGOING = 3;
    private final static int STATE_CHECK_OK = 4;                    // terminal
    private final static int STATE_CHECK_SHOW_SECURITY = 5;         // terminal
    private final static int STATE_CHECK_ERROR = 6;                 // terminal
    private final static int STATE_AUTODISCOVER_AUTH_DIALOG = 7;    // terminal
    private final static int STATE_AUTODISCOVER_RESULT = 8;         // terminal
    private int mState = STATE_START;

    private boolean mAttached;
    private boolean mPaused = false;
    private CheckingDialog mCheckingDialog;
    private LongOperation mAccountCheckTask;
    private SetupData mSetupData;
    AccountSetupIncoming mActivity;

    public final static int CHECK_SETTINGS_OK = 0;

    public interface Callbacks {

        public void onCheckSettingsComplete(int result, SetupData setupData);

        public void onAutoDiscoverComplete(int result, SetupData setupData);
    }

    // Public no-args constructor needed for fragment re-instantiation
    public AccountCheckSettingsFragment() {
    }

    /**
     * Create a retained, invisible fragment that checks accounts
     *
     * @param mode incoming or outgoing
     */
    public static AccountCheckSettingsFragment newInstance(int mode, Fragment parentFragment) {
        final AccountCheckSettingsFragment f = new AccountCheckSettingsFragment();
        f.setTargetFragment(parentFragment, mode);
        return f;
    }

    /**
     * Fragment initialization.  Because we never implement onCreateView, and call
     * setRetainInstance here, this creates an invisible, persistent, "worker" fragment.
     * fragments have the ability to retain their instances, simply by calling: setRetainInstance(true),
     * in one of its callback methods, for example in the onCreate().
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAttached = true;

        // If this is the first time, start the AsyncTask
        if (mAccountCheckTask == null) {
            final int checkMode = getTargetRequestCode();
            final SetupData.SetupDataContainer container =
                    (SetupData.SetupDataContainer) getActivity();
            mSetupData = container.getSetupData();
            // mAccountCheckTask = (LongOperation)
            mAccountCheckTask = (LongOperation)  new LongOperation(checkMode).execute("some data");
        }
    }

    @Override
    public void onAttach(final Activity activity)
    {
        super.onAttach(activity);
        Log.d(TAG, this + ": onAttach(" + activity + ")");
        mActivity = (AccountSetupIncoming)activity;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPaused = false;
        if (mState != STATE_START) {
            reportProgress(mState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAccountCheckTask != null) {

            mAccountCheckTask = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAttached = false;
    }

    /**
     * Kill self if not already killed.
     */
    private void finish() {
        Log.d(TAG,"finishing up");
       final FragmentManager fm = mActivity.getSupportFragmentManager();
        if (fm != null) {
            fm.popBackStack();
            Log.d(TAG,"popBackStack");
        }
    }

    /**
     * Find the callback target, either a target fragment or the activity
     */
    private Callbacks getCallbackTarget() {
        final Fragment target = getTargetFragment();
        if (target instanceof Callbacks) {
            return (Callbacks) target;
        }
        Activity activity = getActivity();
        if (activity instanceof Callbacks) {
            return (Callbacks) activity;
        }
        throw new IllegalStateException();
    }

    private void onCheckingDialogCancel() {

        cancelTask(mAccountCheckTask, true);
        mAccountCheckTask = null;
        // 2. kill self with no report - this is "cancel"
        finish();
    }

    public static void cancelTask(AsyncTask<?, ?, ?> task, boolean mayInterruptIfRunning) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(mayInterruptIfRunning);
        }
    }

    private void reportProgress(int newState) {
        mState = newState;
        if (mAttached && !mPaused) {
            final FragmentManager fm = getActivity().getSupportFragmentManager();
            switch (newState) {
                case STATE_CHECK_OK:
                    fm.popBackStack();
                    getCallbackTarget().onCheckSettingsComplete(CHECK_SETTINGS_OK, mSetupData);
                    break;
                default:
                    if (mCheckingDialog == null) {
                        mCheckingDialog = CheckingDialog.newInstance(this, mState);
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
    }

    public class LongOperation extends AsyncTask<String, Integer, String> {
        final Context mContext;
        final int mMode;

        public LongOperation(int mode) {
            mContext = getActivity().getApplicationContext();
            mMode = mode;
        }

        @Override
        protected String doInBackground(String... params) {

            if ((mMode & SetupData.CHECK_INCOMING) != 0) {
                Log.d(TAG, "doinbackground  check incoming");
                if (isCancelled()) return null;
                publishProgress(STATE_CHECK_INCOMING);
            }

            if ((mMode & SetupData.CHECK_OUTGOING) != 0) {
                Log.d(TAG, "doinbackgroundcheck outgoing");
                if (isCancelled()) return null;
                publishProgress(STATE_CHECK_OUTGOING);
            }

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
            reportProgress(STATE_CHECK_OK);
            Log.d(TAG, "Received result: " + result);
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
        public static CheckingDialog newInstance(AccountCheckSettingsFragment parentFragment,
                                                 int progress) {
            final CheckingDialog f = new CheckingDialog();
            f.setTargetFragment(parentFragment, progress);
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

                            final AccountCheckSettingsFragment target =
                                    (AccountCheckSettingsFragment) getTargetFragment();
                            if (target != null) {
                                target.onCheckingDialogCancel();
                            }
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
            final AccountCheckSettingsFragment target =
                    (AccountCheckSettingsFragment) getTargetFragment();
            if (target != null) {
                target.onCheckingDialogCancel();
            }
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

          return  getActivity().getString(stringId);

        }
    }

}
