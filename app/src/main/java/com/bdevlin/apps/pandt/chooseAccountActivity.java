package com.bdevlin.apps.pandt;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;

import com.bdevlin.apps.ui.activity.setup.AccountSetupActivity;
import com.bdevlin.apps.ui.fragments.ChooseAccountFragment;
import com.bdevlin.apps.utils.PandtAccountUtils;

/**
 * Created by brian on 10/26/2014.
 */
public class chooseAccountActivity  extends AccountSetupActivity implements PandtAccountUtils.AuthenticateCallback {

    private static final String TAG = chooseAccountActivity.class.getSimpleName();

    public static final String EXTRA_FINISH_INTENT = "com.devlin.pandt.extra.FINISH_INTENT";

    private Intent mFinishIntent;

    FragmentManager mFragmentManager;

    protected static ChooseAccountFragment mChooseFragment;
    public AccountManager mAccountManager;
    public AuthenticatorDescription mChosenDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

        mAccountManager = AccountManager.get(this);
        mFragmentManager = this.getSupportFragmentManager();
        // get the returning activity reference from the incoming intent
        if (getIntent().hasExtra(EXTRA_FINISH_INTENT)) {
            mFinishIntent = getIntent().getParcelableExtra(EXTRA_FINISH_INTENT);
        }

        // if this is the first instance load the fragment container
        // with the choose account fragment
        if (savedInstanceState == null) {
            mChooseFragment =  new ChooseAccountFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, mChooseFragment,
                            "choose_account").commit();
        }


    }

    public void getAuthToken()
    {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(mChosenDescription.type, "com.bdevlin.apps.pandt", null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            // Block until the operation completes
                            bnd = future.getResult();
                            final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            showMessage(((authtoken != null) ? "SUCCESS!\ntoken: " + authtoken : "FAIL"));
                            Log.d("udinic", "GetTokenForAccount Bundle is " + bnd);

                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }
                    }
                }
                , null);

    }

    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean shouldCancelAuthentication() {
        return false;
    }

    @Override
    public void onAuthTokenAvailable(String authToken) {
        // authentication callback method
      //  AppPreferences.setAuthToken(this, authToken);
      //  AppPreferences.setChosenAccountName(this, name);

        if (mFinishIntent != null) {
            Log.d(TAG, "mFinishIntent: " + mFinishIntent);
            mFinishIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            mFinishIntent.setAction(Intent.ACTION_MAIN);
            mFinishIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mFinishIntent);
        }

        finish();
    }
}
