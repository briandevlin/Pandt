package com.bdevlin.apps.ui.activity.setup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.bdevlin.apps.authenticator.SetupData;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.fragments.AccountCheckSettingsFragment;
import com.bdevlin.apps.ui.fragments.AccountServerBaseFragment;
import com.bdevlin.apps.ui.fragments.AccountSetupOutgoingFragment;

/**
 * Created by brian on 10/20/2014.
 */
public class AccountSetupOutgoing extends AccountSetupActivity
        implements AccountSetupOutgoingFragment.Callback, View.OnClickListener {

    /* package */ AccountSetupOutgoingFragment accountSetupOutgoingFragment;
    private Button mNextButton;
    /* package */ boolean mNextButtonEnabled;

    public static void actionOutgoingSettings(Activity fromActivity, SetupData setupData) {
        Intent intent = new Intent(fromActivity, AccountSetupOutgoing.class);
        intent.putExtra(SetupData.EXTRA_SETUP_DATA, setupData);
        fromActivity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.outgoing);
        accountSetupOutgoingFragment = AccountSetupOutgoingFragment.newInstance(null, 1);

        replaceFragment(accountSetupOutgoingFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, "some other tag",
                R.id.main_content);

        // Configure fragment
        accountSetupOutgoingFragment.setCallback(this);


        final Button previousButton = (Button) findViewById(R.id.previous);
        if (previousButton != null) previousButton.setOnClickListener(this);

        final Button nextButton = (Button) findViewById(R.id.next);
        if (nextButton != null) nextButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                accountSetupOutgoingFragment.onNext();
                break;
            case R.id.previous:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onEnableProceedButtons(boolean enable) {

    }

    @Override
    public void onProceedNext(int checkMode, AccountServerBaseFragment target) {
        final AccountCheckSettingsFragment checkerFragment =
                AccountCheckSettingsFragment.newInstance(checkMode, target);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(checkerFragment, AccountCheckSettingsFragment.TAG);
        transaction.addToBackStack("back");
        transaction.commit();
    }

    @Override
    public void onCheckSettingsComplete(int result, SetupData setupData) {
        mSetupData = setupData;
        if (result == AccountCheckSettingsFragment.CHECK_SETTINGS_OK) {

            AccountSetupOptions.actionOptions(this, mSetupData);
            finish();
        }
    }
}
