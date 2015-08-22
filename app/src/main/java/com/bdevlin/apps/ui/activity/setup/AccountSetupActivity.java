package com.bdevlin.apps.ui.activity.setup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import com.bdevlin.apps.authenticator.SetupData;

/**
 * Created by brian on 10/19/2014.
 */
public class AccountSetupActivity extends AppCompatActivity implements SetupData.SetupDataContainer {
    protected SetupData mSetupData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SetupData.EXTRA_SETUP_DATA, mSetupData);
    }

    @Override
    public SetupData getSetupData() {
        return mSetupData;
    }

    @Override
    public void setSetupData(SetupData setupData) {
        mSetupData = setupData;
    }

    public int replaceFragment(Fragment fragment, int transition, String tag, int anchor) {
        final FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(transition);
        fragmentTransaction.replace(anchor, fragment, tag);
        // fragmentTransaction.addToBackStack(tag);
        final int id = fragmentTransaction.commitAllowingStateLoss();
        // fm.executePendingTransactions(); // had to remove this as it crashes because of the layout fragment in the authenticator
        return id;
    }
}
