package com.bdevlin.apps.ui.activity.setup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.bdevlin.apps.authenticator.SetupData;
import com.bdevlin.apps.ui.fragments.AccountCheckSettingsFragment;
import com.bdevlin.apps.ui.fragments.AccountServerBaseFragment;
import com.bdevlin.apps.ui.fragments.AccountSetupIncomingFragment;
import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 10/18/2014.
 */
public class AccountSetupIncoming extends AccountSetupActivity
        implements AccountServerBaseFragment.Callback, View.OnClickListener {

    private static final String TAG = AccountSetupIncoming.class.getSimpleName();

    private AccountSetupIncomingFragment accountSetupIncomingFragment;

    // called from AccountSetupType
    public static void actionIncomingSettings(Activity fromActivity, SetupData setupData) {
        final Intent intent = new Intent(fromActivity, AccountSetupIncoming.class);
        // Add the additional information to the intent, in case the Email process is killed.
        intent.putExtra(SetupData.EXTRA_SETUP_DATA, setupData);
        fromActivity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming);

        accountSetupIncomingFragment = AccountSetupIncomingFragment.newInstance(null, 1);

        replaceFragment(accountSetupIncomingFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, "some tag",
                R.id.main_content);

        // Configure fragment
        accountSetupIncomingFragment.setCallback(this);

        final Button previousButton = (Button) findViewById(R.id.previous);
        if (previousButton != null) previousButton.setOnClickListener(this);

        final Button nextButton = (Button) findViewById(R.id.next);
        if (nextButton != null) nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.next:
                accountSetupIncomingFragment.onNext();
                break;
            case R.id.previous:
                onBackPressed();
                break;
        }
    }


    // <editor-fold desc="AccountServerBaseFragment callbacks">
    /**
     * Implements AccountServerBaseFragment.Callback
     */
    @Override
    public void onEnableProceedButtons(boolean enable) {

    }

    /**
     * Implements AccountServerBaseFragment.Callback
     */
    @Override
    public void onProceedNext(int checkMode, AccountServerBaseFragment target) {
        AccountCheckSettingsFragment checkerFragment =
                AccountCheckSettingsFragment.newInstance(checkMode, target);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(checkerFragment, AccountCheckSettingsFragment.TAG);
        transaction.addToBackStack("back");
        transaction.commit();
    }

    /**
     * Implements AccountServerBaseFragment.Callback
     */
    @Override
    public void onCheckSettingsComplete(int result, SetupData setupData) {
        mSetupData = setupData;
        if (result == AccountCheckSettingsFragment.CHECK_SETTINGS_OK) {
//            if (mServiceInfo.usesSmtp) {
              // AccountSetupOutgoing.actionOutgoingSettings(this, mSetupData);
//            } else {
               AccountSetupOptions.actionOptions(this, mSetupData);
               finish();
//            }
        }

    }

    // </editor-fold>
}
