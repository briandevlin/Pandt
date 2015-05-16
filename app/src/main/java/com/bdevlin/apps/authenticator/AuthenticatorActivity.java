package com.bdevlin.apps.authenticator;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bdevlin.apps.pandt.ForwardingIntent;
import com.bdevlin.apps.ui.activity.setup.AccountSetupType;
import com.bdevlin.apps.pandt.EmailAddressValidator;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.fragments.AccountCheckSettingsFragment;


/**
 * Created by brian on 10/13/2014.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity
        implements SetupData.SetupDataContainer,
        AccountCheckSettingsFragment.Callbacks {

    // <editor-fold desc="Fields">
    private static final String TAG = AuthenticatorActivity.class.getSimpleName();

    private static final String ACTION_CREATE_ACCOUNT = "com.android.email.CREATE_ACCOUNT";
    private static final String EXTRA_FLOW_MODE = "FLOW_MODE";
    private static final String EXTRA_FLOW_ACCOUNT_TYPE = "FLOW_ACCOUNT_TYPE";
    private static final String EXTRA_CREATE_ACCOUNT_EMAIL = "EMAIL";
    private static final String EXTRA_CREATE_ACCOUNT_USER = "USER";
    private static final String EXTRA_CREATE_ACCOUNT_INCOMING = "INCOMING";
    private static final String EXTRA_CREATE_ACCOUNT_OUTGOING = "OUTGOING";
    private static final Boolean DEBUG_ALLOW_NON_TEST_HARNESS_CREATION = false;
    /** The Intent flag to confirm credentials. */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

    /** The Intent extra to store password. */
    public static final String PARAM_PASSWORD = "password";

    /** The Intent extra to store username. */
    public static final String PARAM_USERNAME = "username";

    /** The Intent extra to store username. */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";


    private AccountManager mAccountManager;

    /** Keep track of the login task so can cancel it if requested */
    private UserLoginTask mAuthTask = null;

    /** Keep track of the progress dialog so we can dismiss it */
    private ProgressDialog mProgressDialog = null;

    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password or authToken to be changed on the
     * device.
     */
    private Boolean mConfirmCredentials = false;

    /** for posting authentication attempts back to UI thread */
    private final Handler mHandler = new Handler();

    private TextView mMessage;

    private String mPassword;

    private EditText mPasswordEdit;

    private Button mNextButton;

    /** Was the original caller asking for an entirely new account? */
    protected boolean mRequestNewAccount = false;

    private String mUsername;

    private EditText mUsernameEdit;

    protected SetupData mSetupData;
    private boolean mNextButtonInhibit;

    // this is broken fixme!
    private final EmailAddressValidator mEmailValidator = new EmailAddressValidator();

    private boolean mReportAccountAuthenticatorError;

    // </editor-fold>

    // <editor-fold desc="Intents">

    public static void actionNewAccount(Activity fromActivity) {
        final Intent i = new Intent(fromActivity, AuthenticatorActivity.class);
        i.putExtra(EXTRA_FLOW_MODE, SetupData.FLOW_MODE_NORMAL);
        fromActivity.startActivity(i);
    }

    public static void actionNewAccountWithResult(Activity fromActivity) {
        final Intent i = new ForwardingIntent(fromActivity, AuthenticatorActivity.class);
        i.putExtra(EXTRA_FLOW_MODE, SetupData.FLOW_MODE_NO_ACCOUNTS);
        fromActivity.startActivity(i);
    }

// called from Authenticator.addAccount()
    public static Intent actionGetCreateAccountIntent(Context context, String accountManagerType) {
        final Intent i = new Intent(context, AuthenticatorActivity.class);
        i.putExtra(EXTRA_FLOW_MODE, SetupData.FLOW_MODE_ACCOUNT_MANAGER);
        i.putExtra(EXTRA_FLOW_ACCOUNT_TYPE, accountManagerType);
        return i;
    }


    public static void actionAccountCreateFinishedAccountFlow(Activity fromActivity) {
        // TODO: handle this case - modifying state on SetupData when instantiating an Intent
        // is not safe, since it's not guaranteed that an Activity will run with the Intent, and
        // information can get lost.

        final Intent i= new ForwardingIntent(fromActivity, AuthenticatorActivity.class);
        // If we're in the "account flow" (from AccountManager), we want to return to the caller
        // (in the settings app)
        i.putExtra(SetupData.EXTRA_SETUP_DATA, new SetupData(SetupData.FLOW_MODE_RETURN_TO_CALLER));
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(i);
    }

    public static void actionAccountCreateFinishedWithResult(Activity fromActivity) {
        // TODO: handle this case - modifying state on SetupData when instantiating an Intent
        // is not safe, since it's not guaranteed that an Activity will run with the Intent, and
        // information can get lost.

        final Intent i= new ForwardingIntent(fromActivity, AuthenticatorActivity.class);
        // If we're in the "no accounts" flow, we want to return to the caller with a result
        i.putExtra(SetupData.EXTRA_SETUP_DATA,
                new SetupData(SetupData.FLOW_MODE_RETURN_NO_ACCOUNTS_RESULT));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(i);
    }

//    public static void actionAccountCreateFinished(final Activity fromActivity, Account account) {
//        final Intent i = new Intent(fromActivity, AuthenticatorActivity.class);
//        // If we're not in the "account flow" (from AccountManager), we want to show the
//        // message list for the new inbox
//        i.putExtra(SetupData.EXTRA_SETUP_DATA,
//                new SetupData(SetupData.FLOW_MODE_RETURN_TO_MESSAGE_LIST, account));
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        fromActivity.startActivity(i);
//    }

    // </editor-fold>


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mSetupData = savedInstanceState.getParcelable(SetupData.EXTRA_SETUP_DATA);
        } else {
            final Bundle b = getIntent().getExtras();
            if (b != null) {
                mSetupData = b.getParcelable(SetupData.EXTRA_SETUP_DATA);
            }
        }
        if (mSetupData == null) {
            mSetupData = new SetupData();
        }


        final Intent intent = getIntent();
        final String action = intent.getAction();

        final int intentFlowMode =
                intent.getIntExtra(EXTRA_FLOW_MODE, SetupData.FLOW_MODE_UNSPECIFIED);

        if (intentFlowMode != SetupData.FLOW_MODE_UNSPECIFIED) {
            mSetupData = new SetupData(intentFlowMode,
                    intent.getStringExtra(EXTRA_FLOW_ACCOUNT_TYPE));
        }

        // coming from the addAccount activity
        // mSetupData is set FLOW_MODE_ACCOUNT_MANAGER and account type com.bdevlin.pandt
        final int flowMode = mSetupData.getFlowMode();

        if (flowMode == SetupData.FLOW_MODE_RETURN_TO_CALLER) {
            // Return to the caller who initiated account creation
            finish();
            return;
        } else if (flowMode == SetupData.FLOW_MODE_RETURN_NO_ACCOUNTS_RESULT) {
//            if (EmailContent.count(this, Account.CONTENT_URI) > 0) {
//                setResult(RESULT_OK);
//            } else {
                setAccountAuthenticatorResult(intent.getExtras());
                setResult(RESULT_CANCELED);
//            }
            finish();
            return;
        } else if (flowMode == SetupData.FLOW_MODE_RETURN_TO_MESSAGE_LIST) {
//            final Account account = mSetupData.getAccount();
//            if (account != null && account.mId >= 0) {
//                // Show the message list for the new account
//                //***
//                //Welcome.actionOpenAccountInbox(this, account.mId);
//                finish();
//                return;
//            }
        }

        setContentView(R.layout.login_activity);

        mMessage = (TextView) findViewById(R.id.message);

        mUsernameEdit = (EditText) findViewById(R.id.username_edit);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);
        // the onClick is defined in the layout
        mNextButton = (Button)  findViewById(R.id.next_button);

        mPasswordEdit
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId,
                                                  KeyEvent keyEvent) {
                        if (keyEvent==null) {
                            if (actionId==EditorInfo.IME_ACTION_DONE) {
                                // Capture soft enters in a singleLine EditText that is the last EditText.
                                validateFields();
                            }
                            else if (actionId==EditorInfo.IME_ACTION_NEXT) {
                                // Capture soft enters in other singleLine EditTexts
                                validateFields();
                            }
                            else return false;  // Let system handle all other null KeyEvents
                        }
                        else if (actionId==EditorInfo.IME_NULL) {
                            // Capture most soft enters in multi-line EditTexts and all hard enters.
                            // They supply a zero actionId and a valid KeyEvent rather than
                            // a non-zero actionId and a null event like the previous cases.
                            if (keyEvent.getAction()==KeyEvent.ACTION_DOWN) {
                                // We capture the event when key is first pressed.
                                validateFields();
                            }
                            else  return true;   // We consume the event when the key is released.
                        }
                        return false;
                    }

                });

        onEnableProceedButtons(false);
        mNextButtonInhibit = false;

        // Set aside incoming AccountAuthenticatorResponse, if there was any
        final AccountAuthenticatorResponse authenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        mSetupData.setAccountAuthenticatorResponse(authenticatorResponse);
        if (authenticatorResponse != null) {
            // When this Activity is called as part of account authentification flow,
            // we are responsible for eventually reporting the result (success or failure) to
            // the account manager.  Most exit paths represent an failed or abandoned setup,
            // so the default is to report the error.  Success will be reported by the code in
            // AccountSetupOptions that commits the finally created account.
            mReportAccountAuthenticatorError = true;
        }


        final String userName = mSetupData.getUsername();
        if (userName != null) {
            mUsernameEdit.setText(userName);
            mSetupData.setUsername(null);
        }
        final String password = mSetupData.getPassword();
        if (userName != null) {
            mPasswordEdit.setText(password);
            mSetupData.setPassword(null);
        }

        mMessage.setText(getMessage());
    }

    public void onNext(View view) {
        Log.v(TAG, "handleLogin");

        mUsername = mUsernameEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();

        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
            mMessage.setText(getMessage());
        } else {
           //AccountSetupType.actionSelectAccountType(this, mSetupData);
            showProgress();
            mAuthTask = new UserLoginTask();
            mAuthTask.execute();
        }
    }

    private void showProgress() {
        showDialog(0);
    }

    private void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


    private void validateFields() {
        final boolean valid = !TextUtils.isEmpty(mUsernameEdit.getText())
                && !TextUtils.isEmpty(mPasswordEdit.getText())
                && mEmailValidator.isValid(mUsernameEdit.getText().toString().trim());
        onEnableProceedButtons(valid);
        mMessage.setText(getMessage());

        // Warn (but don't prevent) if password has leading/trailing spaces
       // AccountSettingsUtils.checkPasswordSpaces(this, mPasswordView);
    }

    private CharSequence getMessage() {
        getString(R.string.login_username_label);
        if (TextUtils.isEmpty(mUsernameEdit.getText())) {
            // If no username, then we ask the user to log in using an
            // appropriate service.
            final CharSequence msg = getText(R.string.login_username_label);
            return msg;
        }
        if (TextUtils.isEmpty(mPasswordEdit.getText())) {
            // We have an account but no password
            return getText(R.string.login_password_label);
        }
        return null;
    }

    @Override
    public SetupData getSetupData() {
        return mSetupData;
    }

    @Override
    public void setSetupData(SetupData setupData) {
        mSetupData = setupData;
    }

    private void onEnableProceedButtons(boolean enabled) {
        mNextButton.setEnabled(enabled);
    }



    @Override
    public void finish() {
        // If the account manager initiated the creation, and success was not reported,
        // then we assume that we're giving up (for any reason) - report failure.
        if (mReportAccountAuthenticatorError) {
            final AccountAuthenticatorResponse authenticatorResponse =
                    mSetupData.getAccountAuthenticatorResponse();
            if (authenticatorResponse != null) {
                authenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED, "canceled");
                mSetupData.setAccountAuthenticatorResponse(null);
            }
        }
        super.finish();
    }

    @Override
    public void onCheckSettingsComplete(int result, SetupData setupData) {

    }

    @Override
    public void onAutoDiscoverComplete(int result, SetupData setupData) {
        throw new IllegalStateException();
    }

    public void onAuthenticationResult(String authToken) {
        Log.v(TAG, "onAuthenticationResult(" + authToken + ")");
        boolean success = ((authToken != null) && (authToken.length() > 0));
        Log.v(TAG, "onAuthenticationResult(" + success + ")");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();

        if (success) {
            AccountSetupType.actionSelectAccountType(this, mSetupData);
        } else {
            Log.v(TAG, "onAuthenticationResult: failed to authenticate");
            if (mRequestNewAccount) {
                // "Please enter a valid username/password.
                mMessage.setText(getText(R.string.login_password_label));
            } else {
                // "Please enter a valid password." (Used when the
                // account is already in the database but the password
                // doesn't work.)
                mMessage.setText(getText(R.string.login_username_label));
            }
        }
    }


    public void onAuthenticationCancel() {
        Log.v(TAG, "onAuthenticationCancel()");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();
    }


    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Log.v(TAG, "doInBackground");

            try {
//                PMAuthAPI authApi = AccountUtils.getAuthApi(getBaseContext(),mUsername, mPassword);
//                // it's already defaulting to BasicAuthorizer but this is how to change it
//                authApi.setAuthorizer(new BasicAuthorizer(mUsername,mPassword ));
                String authToken = "com.bdevlin.apps.pandt";
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }

                    Log.d(TAG,"publishProgress  "+ i );
                }
//                try {
//                    //authToken = authApi.authenticate(mUsername, mPassword);
//                    Log.v(TAG, "Logged in! authTokenType : " + authToken );
//                } catch (AuthenticationException e) {
//                    e.printStackTrace();
//                    return null;
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
                return authToken;
            } catch (Exception ex) {
                Log.v(TAG, "UserLoginTask.doInBackground: failed to authenticate");
                Log.v(TAG, ex.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String authToken) {
            Log.v(TAG, "onPostExecute");
            // On a successful authentication, call back into the Activity to
            // communicate the authToken (or null for an error).
            onAuthenticationResult(authToken);
        }

        @Override
        protected void onCancelled() {
            // If the action was canceled (by the user clicking the cancel
            // button in the progress dialog), then call back into the
            // activity to let it know.
            onAuthenticationCancel();
        }
    }

}
