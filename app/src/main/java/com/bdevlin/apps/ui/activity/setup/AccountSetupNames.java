package com.bdevlin.apps.ui.activity.setup;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bdevlin.apps.authenticator.AuthenticatorActivity;
import com.bdevlin.apps.authenticator.SetupData;
import com.bdevlin.apps.pandt.ForwardingIntent;
import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 10/23/2014.
 */
public class AccountSetupNames extends AccountSetupActivity  {
    private static final String TAG = AccountSetupNames.class.getSimpleName();

    private Button mNextButton;
    private boolean mIsCompleting = false;


    public static void actionSetNames(Activity fromActivity, SetupData setupData) {
        ForwardingIntent intent = new ForwardingIntent(fromActivity, AccountSetupNames.class);
        intent.putExtra(SetupData.EXTRA_SETUP_DATA, setupData);
        fromActivity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.names);

        mNextButton = (Button) findViewById(R.id.next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
    }

    private void onNext() {
        mNextButton.setEnabled(false); // Protect against double-tap.
        mIsCompleting = true;


        // Launch async task for final commit work
        // Sicne it's a write task, use the serial executor so even if we ran the task twice
        // with different values the result would be consistent.
        new LongOperation().execute();
    }


    private void finishActivity() {
        if (mSetupData.getFlowMode() == SetupData.FLOW_MODE_NO_ACCOUNTS) {
            AuthenticatorActivity.actionAccountCreateFinishedWithResult(this);
        } else if (mSetupData.getFlowMode() != SetupData.FLOW_MODE_NORMAL) {
            AuthenticatorActivity.actionAccountCreateFinishedAccountFlow(this);
        } else {
            Log.d("finishActivity", " flow mode = " + mSetupData.getFlowMode());
            //final Account account = mSetupData.getAccount();
//            if (account != null) {
//                AuthenticatorActivity.actionAccountCreateFinished(this, account);
//            }
        }
        finish();
    }

    public class LongOperation extends AsyncTask<Void, Void, Boolean> {



        public LongOperation() {


        }

        @Override
        protected Boolean doInBackground(Void... params) {



            try {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                   // publishProgress(i);
                }
                return true;

            } catch (final Exception me) {

                return false;
            }




        }

        @Override
        protected void onPostExecute(Boolean isSecurityHold) {

            if (!isCancelled()) {
                finishActivity();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // displayProgressBar("Downloading...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (isCancelled()) return;
            // updateProgressBar(values[0]);
        }
    }
}
